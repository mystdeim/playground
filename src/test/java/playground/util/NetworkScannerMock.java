package playground.util;

public class NetworkScannerMock extends NetworkScanner {

	@Override
	protected boolean ping(int ip) {
		return true;
	}
	
}
