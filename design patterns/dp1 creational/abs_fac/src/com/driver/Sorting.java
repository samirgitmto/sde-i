package com.driver;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Sorting {

	public static void main(String[] args) {
        List<String> strings = Arrays.asList("apple", "Apple", "banana", "kiwi", "orange", "pear");
        
        strings.sort(Comparator
            .comparingInt(String::length)  // Sort by length first
//            .thenComparing(String::compareTo)  // Then lexicographically
            .thenComparing(String::compareToIgnoreCase)  // Then lexicographically case insensitive
        );
        
        System.out.println(strings);  // [kiwi, pear, apple, banana, orange]
    }
	
}