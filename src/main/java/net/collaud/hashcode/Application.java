package net.collaud.hashcode;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Gaetan Collaud
 */
public class Application {
	
	
	
	public static void main(String[] args) {
		
		List<String> files = Arrays.asList(
				"mother_of_all_warehouses",
				"redundancy",
				"busy_day");
		files.stream().forEach(f -> {
			String in = "data/inputs/"+f+".in";
			String out = "data/outputs/"+f+".out";
			new HashCode(in, out).solve();
		});
	}
	
}
