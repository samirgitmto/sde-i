package com.t2jl.nvo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Test {

	// Swiggy
	
	public static Boolean isPossibleToEarn(List<Integer> list, int target) {
		for (int i=0; i<list.size(); i++) {
			int earning = 0;
			for (int j=i; j<list.size(); j++) {
				earning += list.get(j);
				if (earning==target)
					return true;
			}
			
		}
		
		return false;
	}
	
	public static Boolean isPossible(List<Integer> list, int target) {
		List<List<Integer>> listOfXEarning = new ArrayList<List<Integer>>();
		determine(list, target, new ArrayList<Integer>(), listOfXEarning);
		return listOfXEarning.size()!=0;
		
	}
	
	private static void determine(List<Integer> list, int target, List<Integer> temp, List<List<Integer>> mainList) {
		
		if (temp.size()!=0) {
			int sum = temp.stream().mapToInt(Integer::intValue).sum();
			if (sum==target) {
				mainList.add(new ArrayList<Integer>(temp));
			}
		}
		for (int i=0; i<list.size(); i++) {
			temp.add(list.get(i));
			determine(list, target, temp, mainList);
		}
		
	}
	
	public static Boolean isPossible(List<Integer> list, int target, int sum, int index) {
		if (index==list.size()) {
			System.out.println("list ended");
			return false;
		}
		if (sum==target) {
			System.err.println("possible");
			return true;
		}
		if (sum+list.get(index)>target) {
			System.out.println("greater than target " + index);
			return false;
		}
		sum += list.get(index);
		Boolean result;
		result = isPossible(list, target, sum, index);
		if (!result)
			isPossible(list, target, sum, index+1);
		return result;
	}
	
	public static boolean isPossibleNewWithoutRepeatation(List<Integer> list, int target, int sum, int index) {
	    // Base cases
	    if (sum == target) {
	        System.err.println("possible");
	        return true;
	    }
	    if (index == list.size()) {
	        System.out.println("list ended");
	        return false;
	    }

	    // Case 1: Include current element
	    if (sum + list.get(index) <= target) {
	        if (isPossibleNewWithoutRepeatation(list, target, sum + list.get(index), index + 1)) {
	            return true;
	        }
	    }

	    // Case 2: Exclude current element
	    return isPossibleNewWithoutRepeatation(list, target, sum, index + 1);
	}

	
	public static boolean isPossibleNewWithRepeatation(List<Integer> list, int target, int sum, int index) {
	    if (sum == target) {
	        System.err.println("possible");
	        return true;
	    }
	    if (sum > target || index == list.size()) {
	        return false;
	    }

	    // Case 1: Include current element again (reuse)
	    if (isPossibleNewWithRepeatation(list, target, sum + list.get(index), index)) {
	        return true;
	    }

	    // Case 2: Skip current element
	    return isPossibleNewWithRepeatation(list, target, sum, index + 1);
	}

	
	
	
	public static void main(String[] args) {
		// list of earning data
		// target  == 36
		List<Integer> list = List.of(10, 15, 20, 13);
//		List<Integer> list = List.of(36);
//		System.out.println(isPossibleToEarn(list, 36));
//		System.out.println(isPossible(list, 36, 0, 0));
//		System.out.println(isPossibleNewWithoutRepeatation(list, 36, 0, 0));
		System.out.println(isPossibleNewWithRepeatation(list, 36, 0, 0));
//		isPossible(list, 36, 0, 0);
//		System.out.println(isPossibleToEarn(list, 35));
	}
}
