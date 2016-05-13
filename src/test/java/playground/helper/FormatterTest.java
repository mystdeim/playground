package playground.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class FormatterTest {
	
	@Test
	public void testNumberToHumanable() {
		assertEquals("1", Formatter.numberToHumanable(1L));
		assertEquals("100", Formatter.numberToHumanable(100L));
		assertEquals("1 000", Formatter.numberToHumanable(1_000L));
		assertEquals("100 000", Formatter.numberToHumanable(100_000L));
		assertEquals("1 000 000 000", Formatter.numberToHumanable(1_000_000_000L));
	}
	
	@Test
	public void testToBinaty() {
		assertEquals("0", Formatter.toBinary(0, 1));
		assertEquals("001", Formatter.toBinary(1, 3));
		assertEquals("101", Formatter.toBinary(5, 3));
	}
	
}
