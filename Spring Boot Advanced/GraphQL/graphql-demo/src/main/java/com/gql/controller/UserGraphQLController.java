package com.gql.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.gql.model.User;
import com.gql.repository.UserRepository;

@Controller
public class UserGraphQLController {

	private final UserRepository userRepository;

	public UserGraphQLController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@QueryMapping
	public User user(@Argument Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@QueryMapping
	public List<User> users() {
		return userRepository.findAll();
	}

	@MutationMapping
	public User createUser(@Argument String name, @Argument String email) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		return userRepository.save(user);
	}

	/** PUT equivalent — all fields required, full replace */
	@MutationMapping
	public User updateUser(@Argument Long id, @Argument String name, @Argument String email) {
		return userRepository.findById(id).map(user -> {
			user.setName(name);
			user.setEmail(email);
			return userRepository.save(user);
		}).orElse(null);
	}

	/** PATCH equivalent — only non-null args are applied */
	@MutationMapping
	public User patchUser(@Argument Long id, @Argument String name, @Argument String email) {
		return userRepository.findById(id).map(user -> {
			if (name != null) {
				user.setName(name);
			}
			if (email != null) {
				user.setEmail(email);
			}
			return userRepository.save(user);
		}).orElse(null);
	}

	/**
	 * Explicit mapping: Java method is removeUser, but SDL field is deleteUser.
	 * Spring does the lookup, starting from the Java method.
	 * Without name = "deleteUser", Spring would look for a Mutation field named removeUser and fail.
	 */
	@MutationMapping(name = "deleteUser")
	public Boolean removeUser(@Argument Long id) {
		if (!userRepository.existsById(id)) {
			return false;
		}
		userRepository.deleteById(id);
		return true;
	}
}
