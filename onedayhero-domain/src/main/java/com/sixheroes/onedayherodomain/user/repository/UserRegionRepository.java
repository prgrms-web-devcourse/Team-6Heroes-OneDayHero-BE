package com.sixheroes.onedayherodomain.user.repository;

import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRegionRepository extends JpaRepository<UserRegion, Long> {

    List<UserRegion> findAllByUser(User user);
}
