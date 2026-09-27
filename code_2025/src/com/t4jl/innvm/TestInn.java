package com.t4jl.innvm;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestInn {

	
	public static void main(String[] args) {
		Employee emp1 = new Employee("sales", 1000);
		Employee emp2 = new Employee("sales", 1200);
		Employee emp3 = new Employee("it", 500);
		Employee emp4 = new Employee("dev", 1500);
		Employee emp5 = new Employee("dev", 1200);
		
		List<Employee> list = List.of(emp1, emp2, emp3, emp4, emp5);
		
		
		Map<String, List<Employee>> collect = list.stream().collect(Collectors.groupingBy(e->e.department));
		String departmentWithMaxSalary = "";		
		double maxAvg = 0.0;
		for (Map.Entry<String, List<Employee>> entry : collect.entrySet()) {
			String key = entry.getKey();
			List<Employee> val = entry.getValue();
						
			double max = (val.stream().mapToInt(e->e.salary).sum())/val.size();
			if (max>maxAvg) {
				maxAvg = max;
				departmentWithMaxSalary = key;
			}
		}
		
		System.out.println("departmentWithMaxSalary: " + departmentWithMaxSalary);
		
	}
	
	
}

class Employee {
	String department;
	int salary;
	
	public Employee(String department, int salary) {
		this.department = department;
		this.salary = salary;
	}
}