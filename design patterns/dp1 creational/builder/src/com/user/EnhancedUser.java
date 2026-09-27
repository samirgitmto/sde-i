package com.user;

import java.util.Objects;

/**
 * Immutable EnhancedUser class using the Builder pattern.
 * Demonstrates best practices: immutability, validation, required/optional fields, and documentation.
 */
public final class EnhancedUser {
    private final String name; // required
    private final String email; // required
    private final String role; // optional

    private EnhancedUser(Builder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.role = builder.role;
    }

    /**
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the user's role (may be null)
     */
    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "EnhancedUser [name=" + name + ", email=" + email + ", role=" + role + "]";
    }

    /**
     * Builder for EnhancedUser. Name and email are required.
     */
    public static class Builder {
        private final String name;
        private final String email;
        private String role;

        /**
         * Builder constructor with required fields.
         * @param name the user's name (required)
         * @param email the user's email (required)
         */
        public Builder(String name, String email) {
            this.name = Objects.requireNonNull(name, "Name is required");
            this.email = Objects.requireNonNull(email, "Email is required");
        }

        /**
         * Set the user's role (optional).
         * @param role the user's role
         * @return this builder
         */
        public Builder role(String role) {
            this.role = role;
            return this;
        }

        /**
         * Build the EnhancedUser instance, performing validation.
         * @return EnhancedUser
         * @throws InvalidUserException if validation fails
         */
        public EnhancedUser build() {
            validate();
            return new EnhancedUser(this);
        }

        private void validate() {
            StringBuilder errors = new StringBuilder();
            if (name.trim().isEmpty()) {
                errors.append("Name cannot be empty. ");
            }
            if (!email.contains("@")) {
                errors.append("Email must contain '@'. ");
            }
            if (errors.length() > 0) {
                throw new InvalidUserException(errors.toString().trim());
            }
        }
    }

    /**
     * Custom exception for user validation errors.
     */
    public static class InvalidUserException extends RuntimeException {
        public InvalidUserException(String message) {
            super(message);
        }
    }
}
