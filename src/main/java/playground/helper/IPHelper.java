package playground.helper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
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
	
	public static int toInt(String str) {
		int[] bs = Arrays.stream(str.split("\\.")).mapToInt(s -> Integer.parseInt(s)).toArray();
		int a = 0;
		for (int i = 0; i < bs.length; i++) a += (bs[i] & 0xff) << (8*(3-i));
		return a;
		
	}
	
	public static String toString(int ip) {
		byte[] b = toByteArray(ip);
		return String.format("%d.%d.%d.%d",	0xff & b[0], 0xff & b[1], 0xff & b[2], 0xff & b[3]);
	}
	
	/**
	 * int -> InetAddress
	 * 
	 * @param n
	 * @return
	 * @throws UnknownHostException
	 */
	public static InetAddress fromInt(int n) throws UnknownHostException {
		return InetAddress.getByAddress(toByteArray(n));
	}
	
	public static byte[] toByteArray(int n) {
		byte[] result = new byte[4];
		for (int i = 0; i < 4; i++) result[i] = (byte) (n >> (3-i)*8 & 0xFF);
		return result;
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
	public static boolean isReachable(int ip, int timeout) {
		try {
			if (IPHelper.fromInt(ip).isReachable(timeout)) return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static int ping(int ip) throws IOException, InterruptedException {
		boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
		ProcessBuilder pb = new ProcessBuilder("ping", isWindows ? "-n" : "-c", "1", toString(ip));
		Process proc = pb.start();
		int value = proc.waitFor();
		return value;
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
				byte reverse_mask = (byte) (32 - Byte.parseByte(strs[1]));
				if (32 == reverse_mask) return new int[] {0, -1};
				else {
					int start = address >> reverse_mask << reverse_mask;
					int finish = start | ((1 << reverse_mask) - 1);
					return new int[] {start, finish};			
				}
				
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
	
	public static int maskToInt(int mask) {
		if (mask < 0 && mask > 32) throw new IllegalArgumentException("Should be in range [0, 32]");
		if (0 == mask) return 0;
		int reverse_mask = 32 - mask;
		int result = -1 << reverse_mask;
		return result;
	}
}
