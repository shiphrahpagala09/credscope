package com.credscope.credscope.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.credscope.credscope.entity.User;

@Repository
public interface UserRepository extends JpaRepository <User,Long>{
	Optional<User> findByEmail(String email);
}
