package com.sixheroes.onedayherodomain.user.repository;

import com.sixheroes.onedayherodomain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail_Email(String email_email);
    
    @Query("""
                select u
                from User u
                where u.userBasicInfo.nickname like :nickname and u.isHeroMode = true
                order by u.userBasicInfo.nickname
            """)
    Slice<User> findByNicknameAndIsHeroMode(
            @Param("nickname") String nickname,
            Pageable pageable
    );
}
