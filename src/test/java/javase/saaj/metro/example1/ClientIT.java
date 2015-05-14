package javase.saaj.metro.example1;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClientIT {
	@Test
    public void testUSD2HK() throws Exception {
		double actual = Client.USD2HKD();
        System.out.println(actual);
        assertTrue(actual > 0.0D);
    }
}