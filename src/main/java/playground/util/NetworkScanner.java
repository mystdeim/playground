package playground.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import playground.helper.IPHelper;

public class NetworkScanner {

	public static final int DEFAULT_TIMEOUT = 1_000;
	public static final int DEFAULT_MAX_THREADS = 1;
	
	// Construct
	// -----------------------------------------------------------------------------------------------------------------
	
	public NetworkScanner() {
		pool = new ForkJoinPool(max_threads);
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
	
	public int getMaxThreads() {
		return max_threads;
	}
	
	public long getRangeSize() {
		return Integer.toUnsignedLong(finish) - Integer.toUnsignedLong(start) + 1;
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
			firePong(ip, ping(ip));
		}
	}
	
	public void runParallel() {
		for (int ip = start; Integer.compareUnsigned(ip, finish) < 1; ip++) {
//			firePing(ip);
//			if (ping(ip)) firePong(ip);
			Task task = new Task(ip);
//			pool.invoke(task);
			pool.submit(task);
		}
		pool.awaitQuiescence(1, TimeUnit.SECONDS);
//		Stream stream = Stream.of(1, 2);
//		pool.invoke(stream);
	}
	
	// Properties
	// -----------------------------------------------------------------------------------------------------------------
	
	private int start;
	private int finish;
	private int timeout = DEFAULT_TIMEOUT;
	private int max_threads = DEFAULT_MAX_THREADS;
	private ForkJoinPool pool;
	
	// Working with IP
	// -----------------------------------------------------------------------------------------------------------------
	
	protected boolean ping(int ip) {
		return IPHelper.isReachable(ip, getTimeout());
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
	
	private void firePong(int ip, boolean isReachable) {
		listeners.forEach(l -> l.pong(ip, isReachable));
	}
	
	// Interfaces
	// -----------------------------------------------------------------------------------------------------------------
	
	public static interface Listener {
		void ping(int ip);
		void pong(int ip, boolean isReachable);
	}
	
	// Tasks
	//------------------------------------------------------------------------------------------------------------------
	
	private class Task extends RecursiveAction {
		
		private final int ip;
		
		public Task(int ip) {
			this.ip = ip;
		}

		@Override
		protected void compute() {
			firePing(ip);
			firePong(ip, ping(ip));
		}

		
	}
	
	
}
