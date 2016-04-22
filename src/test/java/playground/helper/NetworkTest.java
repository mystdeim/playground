package playground.helper;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

public class NetworkTest {

	@Test
	public void toIntTest() throws UnknownHostException {
		assertEquals(0, IPHelper.toInt(InetAddress.getByName("0.0.0.0")));
		assertEquals(-1, IPHelper.toInt(InetAddress.getByName("255.255.255.255")));
		assertEquals(((long)Math.pow(2, 32) - 1), 
			Integer.toUnsignedLong(IPHelper.toInt(InetAddress.getByName("255.255.255.255"))));
		assertEquals(167837953, IPHelper.toInt(InetAddress.getByName("10.1.1.1")));
		assertEquals(3232235777L, 
				Integer.toUnsignedLong(IPHelper.toInt(InetAddress.getByName("192.168.1.1"))));
	}
	
	@Test
	public void fromIntTest() throws UnknownHostException {
		assertEquals(IPHelper.fromInt(0), InetAddress.getByName("0.0.0.0"));
		assertEquals(IPHelper.fromInt(-1), InetAddress.getByName("255.255.255.255"));
		assertEquals(IPHelper.fromInt(167837953), InetAddress.getByName("10.1.1.1"));
		assertEquals(IPHelper.fromInt((int) 3232235777L), InetAddress.getByName("192.168.1.1"));
	}
	
}
