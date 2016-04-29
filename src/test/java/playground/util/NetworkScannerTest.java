package playground.util;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import playground.util.NetworkScanner.Listener;

public class NetworkScannerTest {

	@Test
	public void scanIPtest() {
		NetworkScanner ns = new NetworkScannerMock();

		List<Integer> ping_list = new ArrayList<>();
		List<Integer> pong_list = new ArrayList<>();
		
		ns.addListener(new Listener() {
			@Override
			public void ping(int ip) {ping_list.add(ip);}
			@Override
			public void pong(int ip, boolean isReachable) {pong_list.add(ip);}
		});
		
		ns.setSubnet("10.0.0.0");
		ns.run();
		assertEquals(ping_list.size(), 1);
		assertEquals(pong_list.size(), 1);
		
		ping_list.clear();
		pong_list.clear();
		ns.setSubnet("10.0.0.0/8");
		ns.run();
		assertEquals(ping_list.size(), 256);
		assertEquals(pong_list.size(), 256);
	}
	
}
