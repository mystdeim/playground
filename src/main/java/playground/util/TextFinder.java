package playground.util;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

public class TextFinder {
	
	enum SEARCH_TYPE { REGEXP, SUBSTRING }
	
	// Constructor
	// -----------------------------------------------------------------------------------------------------------------
	
	public TextFinder() {
		search_type = SEARCH_TYPE.SUBSTRING;
		scanningCounter = new LongAdder();
		acceptedCounter = new LongAdder();
		matchedCounter = new LongAdder();
	}
	
	// API
	// -----------------------------------------------------------------------------------------------------------------
	
	public void start() {
		
	}
	
	public void run() throws IOException {
		reset();
		try (Stream<Path> files = Files.walk(Paths.get(path), FileVisitOption.FOLLOW_LINKS)) {
			files.parallel().forEach(path -> {
//				System.out.println(path);
				fireScanning(path);
				if (path.getFileName().toString().matches(filter)) {
					fireAccepted(path);
					try (Stream<String> lines = Files.lines(path)) {
//						lines.forEach(System.out::println);
						long matches = lines.filter(line -> {
							if (SEARCH_TYPE.REGEXP.equals(search_type)) return line.matches(pattern);
							else return line.contains(pattern);
						}).count();
//						long matches = lines.filter(line -> line.contains(pattern)).count();
						if (matches > 0) fireMatches(path, matches);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Getters & setters
	// -----------------------------------------------------------------------------------------------------------------
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public long getScannedCounter() {
		return scanningCounter.longValue();
	}
	public long getAcceptedCounter() {
		return acceptedCounter.longValue();
	}
	public long getMatchedCounter() {
		return matchedCounter.longValue();
	}
	
	// Properties
	// -----------------------------------------------------------------------------------------------------------------
	
	private SEARCH_TYPE search_type;
	private String path;
	private String filter;
	private String pattern;
	private LongAdder scanningCounter;
	private LongAdder acceptedCounter;
	private LongAdder matchedCounter;
	
	// Helpers
	// -----------------------------------------------------------------------------------------------------------------
	
	private void reset() {
		scanningCounter.reset();
		acceptedCounter.reset();
		matchedCounter.reset();
	}
	
	private void fireScanning(Path path) {
		scanningCounter.increment();
		listeners.forEach(l -> l.scanning(path.toString()));
	}
	
	private void fireAccepted(Path path) {
		acceptedCounter.increment();
		listeners.forEach(l -> l.accepted(path.toString()));
	}
	
	private void fireMatches(Path path, long count) {
		matchedCounter.add(count);
		listeners.forEach(l -> l.matched(path.toString(), count));
	}
	
	// Listeners
	// -----------------------------------------------------------------------------------------------------------------
	
	private List<Listener> listeners = new ArrayList<>();
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	public interface Listener {
		void scanning(String path);
		void accepted(String path);
		void matched(String path, long count);
	}
	
	// Item
	// -----------------------------------------------------------------------------------------------------------------
	
	public class Item {
		
	}
}
