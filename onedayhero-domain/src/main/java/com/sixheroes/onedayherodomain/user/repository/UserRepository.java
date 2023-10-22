package com.sixheroes.onedayherodomain.user.repository;

import com.sixheroes.onedayherodomain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}