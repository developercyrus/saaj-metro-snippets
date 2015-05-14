package javase.saaj.metro.example1;

import java.io.StringWriter;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Client {

    public static void main(String args[]) throws Exception {
        System.out.println(USD2HKD());
    }
   
    public static double USD2HKD() throws Exception {
    	SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        String url = "http://www.webservicex.net/CurrencyConvertor.asmx";
        SOAPMessage soapRequest = createSOAPRequest();
        SOAPMessage soapResponse = soapConnection.call(soapRequest, url);

        System.out.println("Request SOAP Message: " + format(soapRequest));
        System.out.println("Response SOAP Message: " + format(soapResponse));
        

        double rate = Double.parseDouble(soapResponse.getSOAPBody().getElementsByTagName("ConversionRateResult").item(0).getFirstChild().getNodeValue());    

        /*
         	1. soapMessage request constructed as follows:
         	2. able to paste in soapUI, and run successfully
         */
        /*
			<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:anything="http://www.webserviceX.NET/">
			    <SOAP-ENV:Header/>
			    <SOAP-ENV:Body>
			        <anything:ConversionRate>
			            <anything:FromCurrency>USD</anything:FromCurrency>
			            <anything:ToCurrency>HKD</anything:ToCurrency>
			        </anything:ConversionRate>
			    </SOAP-ENV:Body>
			</SOAP-ENV:Envelope>     
         */
        
        
        /*
			<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			    <soap:Body>
			        <ConversionRateResponse xmlns="http://www.webserviceX.NET/">
			            <ConversionRateResult>7.7517</ConversionRateResult>
			        </ConversionRateResponse>
			    </soap:Body>
			</soap:Envelope>
         */
        
        return rate;
    }
    
    private static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // This is case sensitive
        String serverURI = "http://www.webserviceX.NET/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("anything", serverURI);
        
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("ConversionRate", "anything");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("FromCurrency", "anything");
        soapBodyElem1.addTextNode("USD");        
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("ToCurrency", "anything");
        soapBodyElem2.addTextNode("HKD");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + "ConversionRate");
        
        soapMessage.saveChanges();

        return soapMessage;
    }
    
    
    public static String format(SOAPMessage message) {
        StringWriter sw = new StringWriter();
        try {
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(message.getSOAPPart()), new StreamResult(sw));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        // after transform , it appends with <?xml version="1.0" encoding="UTF-8" standalone="no"?> at the very beginning
        return sw.toString();
    }
    
    

}