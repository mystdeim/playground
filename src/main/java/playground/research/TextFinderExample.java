package playground.research;

import java.util.concurrent.atomic.LongAdder;

import playground.util.TextFinder;
import playground.util.TextFinder.Listener;

public class TextFinderExample {

	public static void main(String[] args) {
		try {
			
			TextFinder tf = new TextFinder();
//			tf.setPath("D:/tmp/javafxwebview");
//			tf.setPath("c:/workspace/SOC-Sources");
			tf.setPath("c:/workspace");
			tf.setFilter(".*\\.java");
//			tf.setPattern("private");
			tf.setPattern("random");
			
			tf.addListener(new Listener() {
				@Override
				public void scanning(String path) {
					// TODO Auto-generated method stub
				}
				@Override
				public void accepted(String path) {
//					System.out.printf("File: %s : %d \n", path, count);
				}
				@Override
				public void matched(String path, long count) {
					System.out.printf("Matches: %s : %d \n", path, count);
				}
			});
			
			long ns = System.nanoTime();
			tf.run();
			double d = (System.nanoTime() - ns) / 1_000_000.0;
			System.out.printf("Scanned: %d Accepted: %d Macthes: %d for %.2f s \n", 
					tf.getScannedCounter(), tf.getAcceptedCounter(), tf.getMatchedCounter(), d);
			
		} catch (Exception e) {
			
		}
	}

}
