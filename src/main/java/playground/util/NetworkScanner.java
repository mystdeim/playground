package playground.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import playground.helper.IPHelper;

public class NetworkScanner {

	public static final int DEFAULT_TIMEOUT = 1_000;
	
	// Construct
	// -----------------------------------------------------------------------------------------------------------------
	
	public NetworkScanner() {
		
	}
	
	// Getters & setters
	// -----------------------------------------------------------------------------------------------------------------
	
	public void setSubnet(String str) {
		int[] range = IPHelper.getRange(str);
		setSubnet(range[0], range[1]);
	}
	
	public void setSubnet(int start, int finish) {
		if (Integer.compareUnsigned(start, finish) > 0) throw new IllegalArgumentException("Illegal range of subnet");
		this.start = start;
		this.finish = finish;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	// API
	// -----------------------------------------------------------------------------------------------------------------
	
	
	public void run() {
//		long s = Integer.toUnsignedLong(start);
//		long f = Integer.toUnsignedLong(finish);	
//		for (long l_ip = s; l_ip <= f ; l_ip++) {
//			firePing((int) l_ip);
//		}
		
//		for (int ip = start; ip <= finish; ip++) {
//			firePing(ip);
//		}
		
		for (int ip = start; Integer.compareUnsigned(ip, finish) < 1; ip++) {
			firePing(ip);
			if (ping(ip)) firePong(ip);
		}
	}
	
	private int start;
	private int finish;
	private int timeout = DEFAULT_TIMEOUT;
	
	// Working with IP
	// -----------------------------------------------------------------------------------------------------------------
	
	protected boolean ping(int ip) {
		return IPHelper.ping(ip, getTimeout());
	}
	
	// Listeners
	// -----------------------------------------------------------------------------------------------------------------
	
	public void addListener(Listener listener) {
		if (null == listeners) listeners = new ArrayList<>();
		listeners.add(listener);
	}
	
	public void removeListener(Listener listener) {
		if (null == listeners) throw new NullPointerException("There is no listener yet");
	}
	
	private List<Listener> listeners;
	
	private void firePing(int ip) {
		listeners.forEach(l -> l.ping(ip));
	}
	
	private void firePong(int ip) {
		listeners.forEach(l -> l.pong(ip));
	}
	
	// Interfaces
	// -----------------------------------------------------------------------------------------------------------------
	
	public static interface Listener {
		void ping(int ip);
		void pong(int ip);
	}
	
}
