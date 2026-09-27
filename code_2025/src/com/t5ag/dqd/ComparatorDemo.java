package com.t5ag.dqd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorDemo implements Comparator<ComparatorDemo> {

	@Override
	public int compare(ComparatorDemo o1, ComparatorDemo o2) {
		
		return 0;
	}
	
	int value;
	public ComparatorDemo(int x) {
		this.value = x;
	}
    // Multiple comparator factories:
    public static Comparator<ComparatorDemo> asc() {
        return (o1, o2) -> o1.value - o2.value;
    }
    public static Comparator<ComparatorDemo> desc() {
        return (o1, o2) -> o2.value - o1.value;
    }
	
	public static void main(String[] args) {
		List<ComparatorDemo> list = new ArrayList<>();
		list.add(new ComparatorDemo(10));
        list.add(new ComparatorDemo(5));
        list.add(new ComparatorDemo(20));
		
        for (ComparatorDemo comparatorDemo : list) {
			System.out.print(comparatorDemo.value + " ");
		}
        System.out.println();
		Collections.sort(list, ComparatorDemo.asc());  // Flexible ordering
		for (ComparatorDemo comparatorDemo : list) {
			System.out.print(comparatorDemo.value + " ");
		}
		System.out.println();
		Collections.sort(list, ComparatorDemo.desc());
		for (ComparatorDemo comparatorDemo : list) {
			System.out.print(comparatorDemo.value + " ");
		}
		System.out.println();
	}

}
