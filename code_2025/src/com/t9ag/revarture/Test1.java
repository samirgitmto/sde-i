package com.t9ag.revarture;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/*
Sanjay Gupta

Implement a function to find the longest substring without repeating characters. Input --> pwwkew Output -->wke
 */
public class Test1 {

	static String getLongestSubstring(String input) {
		
		Set<Character> included = new LinkedHashSet<Character>();
		char[] chars = input.toCharArray();
		String longest = "";
		String current = "";
		
		for (int i = 0; i < chars.length; i++) {
			if (included.contains(chars[i])) {
				
				
				if (current.length()>longest.length()) {
					longest = current;
				}
				included.clear();
				current = "";
			}
			included.add(chars[i]);
			current = current + chars[i];
		}
		
		return longest;
	}
	
	public static void main(String[] args) {
//		String str = "pwwkew";
		String str = "pwwkewop";
		String res = getLongestSubstring(str);
		System.out.println(res);
	}
	
}