package com.t5ag.dqd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentAgeComparator implements Comparator<Student> {

	@Override
	public int compare(Student o1, Student o2) {
		return o1.getAge() - o2.getAge();
	}
	
	
	public static void main(String[] args) {
		// java.lang.UnsupportedOperationException on sort
		List<Student> list0 = List.of(
				new Student(50),
				new Student(25),
				new Student(35),
				new Student(40),
				new Student(21));
		List<Student> list = new ArrayList<Student>(list0);
		
//		List<Student> list = Arrays.asList(new Student(50),
//				new Student(25),
//				new Student(35),
//				new Student(40),
//				new Student(21));
		
		System.out.println(list);
		
		Collections.sort(list, new StudentAgeComparator());
		System.out.println(list);
		
	}
	
}

class Student {
	private int age;
	public Student(int age) {
		this.age = age;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return this.age + "";
	}
}