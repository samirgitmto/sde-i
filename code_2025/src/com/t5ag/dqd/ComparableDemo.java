package com.t5ag.dqd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//public class Comparisons implements Comparable<Integer> {
public class ComparableDemo implements Comparable<ComparableDemo> {
	
	private int value;
	
	public ComparableDemo(int x) {
		this.value = x;
	}
	
	@Override
	public int compareTo(ComparableDemo o) {
		return o.value - this.value;
	}
	
	public static void main(String[] args) {
		List<ComparableDemo> list = new ArrayList<>();
		list.add(new ComparableDemo(10));
        list.add(new ComparableDemo(5));
        list.add(new ComparableDemo(20));
        System.out.println(list);
        for (ComparableDemo comparisons : list) {
			System.out.print(comparisons.value + " ");
		}
        System.out.println();
        Collections.sort(list); // Sorts in descending order
        for (ComparableDemo comparisons : list) {
			System.out.print(comparisons.value + " ");
		}
        System.out.println();
	}
	
}