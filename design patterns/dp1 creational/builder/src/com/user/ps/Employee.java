package com.user.ps;

public class Employee {

	private String name;
	private String email;
	
	private Employee(Builder builder) {
		this.name = builder.name;
		this.email = builder.email;
	}
	
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}

	public static class Builder {
		private String name;
		private String email;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Employee build() {
			return new Employee(this);
		}
	}
}
