package playground.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IPHelper {
	
	// Conversion
	// -----------------------------------------------------------------------------------------------------------------
	
	/**
	 * IP -> int
	 * 
	 * @param address
	 * @return
	 */
	public static int toInt(InetAddress address) {
		int result = 0;
		for (byte b : address.getAddress()) result = result << 8 | (b & 0xFF);
		return result;
	}
	
	/**
	 * int -> InetAddress
	 * 
	 * @param n
	 * @return
	 * @throws UnknownHostException
	 */
	public static InetAddress fromInt(int n) throws UnknownHostException {
		byte[] result = new byte[4];
		for (int i = 0; i < 4; i++) result[i] = (byte) (n >> (3-i)*8 & 0xFF);
		return InetAddress.getByAddress(result);
	}
	
	// Range
	// -----------------------------------------------------------------------------------------------------------------
	
	/**
	 * "IP/mask" -> List
	 * 
	 * @param str "192.168.0.0/16"
	 * @return
	 * @throws UnknownHostException
	 */
	public static List<Integer> getRange(String str) throws UnknownHostException {
		String strs[] = str.split("\\/");
		int address = toInt(InetAddress.getByName(strs[0]));
		byte mask = Byte.parseByte(strs[1]);
		int start = address >> mask << mask;
		int finish = start | ((1 << mask) - 1);
		return IntStream.rangeClosed(start, finish).boxed().collect(Collectors.toList());
	}
	
	
}
