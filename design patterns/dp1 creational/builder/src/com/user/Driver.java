package com.user;

public class Driver {

	public static void main(String[] args) {
		
		User user = new User.Builder()
				.name("John Doe")
				.email("john@gmail.com")
				.role("Dev")
				.build();
		System.out.println(user.toString());
		
		User user2 = new User.Builder()
				.name("  ")
				.email("john@gmail.com")
				.role("Dev")
				.build();
		System.out.println(user2.toString());
		
		
		EnhancedUser enhancedUser = new EnhancedUser.Builder(" ", "abc@gmail.com")
				.role("Dev").build();
		System.out.println(enhancedUser.toString());
		
	}
	
}