package com.user.ps;

public class ClientPs {
	
	public static void main(String[] args) {
		
		Employee employee = new Employee.Builder()
									.name("Mohammad Samir")
									.email("md.samir@gmail.com")
									.build();
		System.out.println(employee.getName());
		System.out.println(employee.getEmail());
		
	}

}
