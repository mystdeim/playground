package playground.util;

import java.net.UnknownHostException;

import playground.helper.IPHelper;

public class NetworkScanner {

	public NetworkScanner() {
		
	}
	
	public void setSubnet(String str) throws UnknownHostException {
		int[] range = IPHelper.getRange(str);
		start = range[0];
		finish = range[1];
	}
	
	private int start;
	private int finish;
	
}
