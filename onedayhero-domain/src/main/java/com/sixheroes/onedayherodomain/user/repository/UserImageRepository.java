package com.sixheroes.onedayherodomain.user.repository;

import com.sixheroes.onedayherodomain.user.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    Optional<UserImage> findUserImageByUser_Id(Long userId);
}
