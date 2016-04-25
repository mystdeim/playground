package playground.helper;

import java.io.IOException;
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
	
	public static int toInt(String str) throws UnknownHostException {
		return toInt(InetAddress.getByName(str));
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
	
	//
	// -----------------------------------------------------------------------------------------------------------------
	
	/**
	 * Not a real ping, send echo on TCP 7 port
	 * 
	 * @param ip
	 * @param timeout
	 * @return
	 */
	public static boolean ping(int ip, int timeout) {
		try {
			if (IPHelper.fromInt(ip).isReachable(timeout)) return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// Range
	// -----------------------------------------------------------------------------------------------------------------
	
	/**
	 * "IP/mask" -> List
	 * 
	 * @deprecated
	 * @param str "192.168.0.0/16"
	 * @return
	 * @throws UnknownHostException
	 */
	public static List<Integer> getRangeList(String str) throws UnknownHostException {
		int[] range = getRange(str);
		return IntStream.rangeClosed(range[0], range[1]).boxed().collect(Collectors.toList());
	}
	
	public static int[] getRange(String str) {
		
		if (null == str) throw new IllegalArgumentException("Shouldn't be null");
		
		try {
			if (str.contains("/")) {
				String strs[] = str.split("\\/");
				int address = toInt(InetAddress.getByName(strs[0]));
				byte mask = Byte.parseByte(strs[1]);
				int start = address >> mask << mask;
		
				// If mask = 32 force set -1 due to overflow int type
				int finish = mask > 31 ? -1 : start | ((1 << mask) - 1);
				return new int[] {start, finish};			
			} else if (str.contains("-")) {
				String strs[] = str.split("-");
				int start = toInt(InetAddress.getByName(strs[0]));
				int finish = toInt(InetAddress.getByName(strs[1]));
				return new int[] {start, finish};
			} else {
				int start = toInt(InetAddress.getByName(str));
				return new int[] {start, start};
			} 
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
}
