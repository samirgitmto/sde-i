package com.t1jl.qsr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConcurrentModification {

	public static void main(String[] args) {
		
		List<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
		
		for (int i = 0; i < list.size(); i++) {
			if (i==3)
				list.remove(i);
		}
		System.out.println(list);
		for (Integer integer : list) {
//			list.remove(integer);
			if (integer == 3) {
				System.out.println(integer);
//				list.remove(3);
				list.set(0, integer);
			}
		}
		System.out.println(list);
		
		
		// Set
		Set<Integer> set = new HashSet<Integer>();
		for (int i=0; i<5; i++) {
			set.add(i);
		}
//		set.
		for (Integer integer : set) {
			if (integer == 3) {
				System.out.println("set: " + integer);
//				set.remove(integer);
			}
		}
		System.out.println(set);
		
		// map
		Map<String, Integer> hm = new HashMap<String, Integer>();
		for (int i=0; i<10; i++) {
			hm.put("value"+i, i);
		}
		for (int i=0; i<10; i++) {
//			hm.put("value"+i, i);
			hm.get(i);
			if (i==3) {
				System.out.println(hm.get(i));
				hm.remove("value"+3);
			}
		}
		System.out.println(hm);
		for (Map.Entry<String, Integer> entry : hm.entrySet()) {
//			System.out.println(entry.getKey() + " = " + entry.getValue());
			System.out.println(entry);
			hm.remove(entry.getKey());
		}
		System.out.println(hm);
	}
	
}