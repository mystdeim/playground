package playground.helper;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

public class IPHelperTest {

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
	
	@Test
	public void toStringTest() throws UnknownHostException {
		assertEquals("0.0.0.0", IPHelper.toString(0));
		assertEquals("10.0.0.0", IPHelper.toString(167772160));
		assertEquals("10.0.0.255", IPHelper.toString(167772415));
		assertEquals("192.168.0.0", IPHelper.toString((int)(3232235520L)));
	}
	
	@Test
	public void rangeTest() throws UnknownHostException {
		assertArrayEquals(
				IPHelper.getRange("10.0.0.1"), 
				new int[] {IPHelper.toInt("10.0.0.1"), IPHelper.toInt("10.0.0.1")});
		
		assertArrayEquals(
				IPHelper.getRange("10.0.0.1-10.0.0.1"),
				new int[] {IPHelper.toInt("10.0.0.1"), IPHelper.toInt("10.0.0.1")});
		assertArrayEquals(
				IPHelper.getRange("10.0.0.1-10.0.0.5"), 
				new int[] {IPHelper.toInt("10.0.0.1"), IPHelper.toInt("10.0.0.5")});
		assertArrayEquals(
				IPHelper.getRange("0.0.0.0-255.255.255.255"), 
				new int[] {IPHelper.toInt("0.0.0.0"), IPHelper.toInt("255.255.255.255")});
		
		assertArrayEquals(
				IPHelper.getRange("10.0.0.0/0"), 
				new int[] {IPHelper.toInt("0.0.0.0"), IPHelper.toInt("255.255.255.255")});
		assertArrayEquals(
				IPHelper.getRange("10.0.0.0/1"), 
				new int[] {IPHelper.toInt("0.0.0.0"), IPHelper.toInt("127.255.255.255")});
		assertArrayEquals(
				IPHelper.getRange("10.0.0.0/24"), 
				new int[] {IPHelper.toInt("10.0.0.0"), IPHelper.toInt("10.0.0.255")});
		assertArrayEquals(
				IPHelper.getRange("0.0.0.0/32"), 
				new int[] {IPHelper.toInt("0.0.0.0"), IPHelper.toInt("0.0.0.0")});
	}	
	
	@Test
	public void maskToIntTest() {
		// 11111111 11111111 11111111 11111111
		assertEquals(-1, IPHelper.maskToInt(32));
		// 11111111 11111111 11111111 00000000
		assertEquals(-256, IPHelper.maskToInt(24));
		// 11111111 11111111 00000000 00000000
		assertEquals(-65_536, IPHelper.maskToInt(16));
		// 11111111 00000000 00000000 00000000
		assertEquals(-16_777_216, IPHelper.maskToInt(8));
		// 00000000 00000000 00000000 00000000
		assertEquals(0, IPHelper.maskToInt(0));
	}
}
