package playground.research;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import playground.helper.IPHelper;
import playground.util.NetworkScanner;
import playground.util.NetworkScanner.Listener;

public class NetworkExamples {

	public static void main(String[] args) {
		try {
////			InetAddress address = Inet4Address.getByAddress(new byte[] {10, 1, 1, 12});
//			InetAddress address = InetAddress.getByName("10.1.1.12");
//			
//			System.out.println("----- test!");
//			InetAddress ia = InetAddress.getByName("192.168.200.200");
//			System.out.println(ia);
//			System.out.println(Integer.toUnsignedLong(IPHelper.toInt(ia)));
//			ia = IPHelper.fromInt(IPHelper.toInt(ia));
//			System.out.println(ia);
//			
////			System.out.println(Arrays.toString(InetAddress.getAllByName("192.168.0.0/16")));
//			System.out.println("----- range!");
//			String str = "10.0.0.7/3";
//			List<Integer> list = IPHelper.getRangeList(str);
//			for (Integer a : list) {
//				System.out.println(IPHelper.fromInt(a));
//			}
//			
////			int a = -1;
////			System.out.println(Integer.toBinaryString(~(-1 >> 3 << 3)));
////			System.out.println(Integer.toBinaryString((1 << 3) - 1));
//			
//			
//			if (address.isReachable(0)) System.out.println("reach!");
			
			NetworkScanner ns = new NetworkScanner();
			ns.addListener(new Listener() {
				@Override
				public void ping(int ip) {
					try {
						InetAddress ia = IPHelper.fromInt(ip);
						System.out.printf("%s (%d) \n", ia, ip);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void pong(int ip) {
					try {
						InetAddress ia = IPHelper.fromInt(ip);
						System.out.printf("OK %s (%d) \n", ia, ip);
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				}
			});
			ns.setSubnet("10.1.1.36-10.1.1.38");
//			ns.setSubnet("192.168.0.0/2");
			
//			ns.setSubnet("127.255.255.255-128.0.0.1");
			
			ns.run();
			
//			System.out.println(IPHelper.fromInt(-1_000_000));
//			System.out.println(IPHelper.fromInt(-2));
//			System.out.println(IPHelper.fromInt(0));
//			System.out.println(IPHelper.fromInt(1));
//			System.out.println(IPHelper.fromInt(Integer.MAX_VALUE));
//			System.out.println(IPHelper.fromInt(-Integer.MAX_VALUE));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
