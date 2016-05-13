package playground.helper;

public class Formatter {

	public static final String numberToHumanable(long value) {
		StringBuilder b = new StringBuilder();
		char[] chars = Long.toString(value).toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[chars.length - i - 1];
			b.append(c);
			if (i > 0 && i < chars.length-1 && (i+1) % 3 == 0) {
				b.append(' ');
			}
		}
		return b.reverse().toString();
	}
	
	/**
	 * (1,1) -> 1
	 * (1,4) -> 0001
	 * 
	 * @param number
	 * @param frame
	 * @return
	 */
	public static final String toBinary(long number, int frame) {
		StringBuilder result = new StringBuilder();
	    for(int i = 0; i < frame ; i++) {
	        int mask = 1 << i;
	        result.append((number & mask) != 0 ? '1' : '0');
	    }
	    return result.reverse().toString();
	}
	
}
