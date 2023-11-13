package com.sixheroes.onedayherodomain.user.repository;

import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.repository.dto.UserQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail_Email(String email_email);

    @Query("""
        select new com.sixheroes.onedayherodomain.user.repository.dto.UserQueryDto(
            u.id, u.userBasicInfo.nickname, u.userBasicInfo.gender, u.userBasicInfo.birth, u.userBasicInfo.introduce,
            u.userFavoriteWorkingDay.favoriteDate, u.userFavoriteWorkingDay.favoriteStartTime, u.userFavoriteWorkingDay.favoriteEndTime,
            ui.id, ui.originalName, ui.uniqueName, ui.path,
            u.heroScore, u.isHeroMode
        )
        from User u
        join UserImage ui on u.id = ui.user.id
        where u.id = :userId
    """)
    Optional<UserQueryDto> findByIdJoinUserImage(
        @Param("userId") Long userId
    );
}
