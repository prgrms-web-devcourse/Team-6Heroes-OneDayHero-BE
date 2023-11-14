package com.sixheroes.onedayherodomain.user.repository;

import com.sixheroes.onedayherodomain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail_Email(String email_email);
}