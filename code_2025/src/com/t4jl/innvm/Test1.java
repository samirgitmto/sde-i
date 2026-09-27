package com.t4jl.innvm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Test1 {

	static List<Integer> getResult(int[] nums) {
		List<Integer> result = new ArrayList<Integer>();
		// 1st duplicate
		// 2nd missing
		int missing = 0;
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < nums.length; i++) {
			if (set.contains(nums[i])) {
				result.add(nums[i]);
			}
			else {
				set.add(nums[i]);
			}
		}
		
		for (int i=1; i<=nums.length; i++) {
			if (!set.contains(i)) {
				result.add(i);
				return result;
			}
		}
		
		return result;
	}
	
	static List<List<String>> getAnagrams(List<String> strings) {
		List<List<String>> anagrams = new ArrayList<List<String>>();
		
		Set<String> anagramsSet = new HashSet<String>();
		for (int i = 0; i < strings.size(); i++) {
			
			
			String current = strings.get(i);
			if (anagramsSet.contains(current)) {
				continue;
			}

			List<String> currentList = new ArrayList<String>();
			currentList.add(current);
			anagramsSet.add(current);
			for (int j=i+1; j<strings.size(); j++) {
				if (areAnagrams(current, strings.get(j))) {
					currentList.add(strings.get(j));
					anagramsSet.add(strings.get(j));
				}
				
			}
			
			anagrams.add(currentList);
			
		}
		
		return anagrams;
	}
	private static Boolean areAnagrams(String str1, String str2) {
		if (str1.length()!=str2.length()) {
			return false;
		}
		
		char[] chars = str1.toCharArray();
		List<Character> list = new ArrayList<Character>();
		for (int i=0; i<chars.length; i++) {
			list.add(chars[i]);
		}
		
		for (int i = 0; i < chars.length; i++) {
			if (!list.contains(str2.charAt(i)))
				return false;
			
		}
		
		return true;
	}
	private static Boolean areAnagramsWithWildcards(String str1, String str2) {
		if (str1.length()!=str2.length()) {
			return false;
		}
		
		String checker = "";
		String stringWithWildCardMaybe = "";
		if (str1.contains("*")) {
			checker = str2;
			stringWithWildCardMaybe = str1;
			
		}
		else {
			checker = str1;
			stringWithWildCardMaybe = str2;
		}
		
		
		
		char[] chars = checker.toCharArray();
		List<Character> list = new ArrayList<Character>();
		for (int i=0; i<chars.length; i++) {
			list.add(chars[i]);
		}
		
		for (int i = 0; i < chars.length; i++) {
			if (!list.contains(str2.charAt(i)))
				return false;
			
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		
//		List<String> list = List.of("abc", "cba", "xy", "mn", "yx");
//		List<String> list = List.of("ac", "cba", "xyz", "mno", "yxz");
		List<String> list = List.of("ac*", "cbaa", "xyz*", "mno", "ayxz");
		System.out.println(getAnagrams(list));
		
		int[] nums = {1, 2, 3, 4, 4, 7, 6};
		
		List<Integer> res = getResult(nums);
		System.out.println(res);
		
		
		// abc, cba, xy, mn, yx
		// abc, cba
		// List<List<String>>
				
		
	}
	
}
