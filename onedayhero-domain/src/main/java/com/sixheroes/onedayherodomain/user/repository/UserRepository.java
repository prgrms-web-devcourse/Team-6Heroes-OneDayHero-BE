package com.sixheroes.onedayherodomain.user.repository;

import com.sixheroes.onedayherodomain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.userImage where u.id = :userId")
    Optional<User> findByIdJoinUserImage(
        @Param("userId") Long userId
    );
}