package net.collaud.hashcode.common.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Gaetan Collaud
 */
@RequiredArgsConstructor
@Getter
public class InputReader {
	private final String file;
	
	public List<List<Integer>> getLines(){
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			List<List<Integer>> ret = new ArrayList<>();
			while((line=br.readLine())!=null){
				ret.add(parseLine(line));
			}
			
			return ret;
		} catch (IOException ex) {
			throw new RuntimeException("Cannot read file "+file, ex);
		}
	}
	
	private List<Integer> parseLine(String line){
		return Arrays.stream(line.split(" "))
				.map(s -> Integer.parseInt(s))
				.collect(Collectors.toList());
	}
}
