package com.user;

public class User {
	
	private String name;
	private String email;
	private String role;
	
	private User(Builder builder) {
		this.name = builder.name;
		this.email = builder.email;
		this.role = builder.role;
	}
	
	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", role=" + role + "]";
	}

	public static class Builder {
		private String name;
		private String email;
		private String role;
	
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		public Builder email(String email) {
			if (email==null || !email.contains("@")) {
				throw new IllegalArgumentException("invalid email");
			}
			this.email = email;
			return this;
		}
		public Builder role(String role) {
			this.role = role;
			return this;
		}
		public User build() {
			return new User(this);
		}
	}
	
}