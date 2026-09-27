package xtracapfintech;

import java.util.LinkedHashMap;
import java.util.Map;

public class Test4 {

	static Character getFirstNonRepeatChar(String str) {
		if (str == null || str.isEmpty())	return null;
		
		Map<Character, Integer> map = new LinkedHashMap<Character, Integer>();
		
		for (char c : str.toLowerCase().toCharArray()) {
			map.put(c, map.getOrDefault(c, 0) + 1);
		}
		
		for (Map.Entry<Character, Integer> m : map.entrySet()) {
			if (m.getValue() == 1)
				return m.getKey();
		}
		return null;
	}
	
	public static void main(String[] args) {
		Character c = getFirstNonRepeatChar("carca");
		System.out.println(c);
	}
	
}
