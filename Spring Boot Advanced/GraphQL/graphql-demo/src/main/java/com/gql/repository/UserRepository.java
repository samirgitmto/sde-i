package com.gql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gql.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
