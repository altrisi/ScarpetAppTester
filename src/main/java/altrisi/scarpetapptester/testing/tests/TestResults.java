package altrisi.scarpetapptester.testing.tests;

import java.util.Map;
import java.util.Map.Entry;

public interface TestResults {

	Map<String, String> getResultsMap();
	
	default String toPrettyString() {
		StringBuilder string = new StringBuilder();
		for (Entry<String, String> e : getResultsMap().entrySet()) {
			string.append(e.getKey());
			string.append(": ");
			string.append(e.getValue());
			string.append('\n');
		}
		return string.toString();
	}
}
