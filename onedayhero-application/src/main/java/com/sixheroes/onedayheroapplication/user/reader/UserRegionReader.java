package com.sixheroes.onedayheroapplication.user.reader;

import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.UserRegion;
import com.sixheroes.onedayherodomain.user.repository.UserRegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserRegionReader {

    private final UserRegionRepository userRegionRepository;

    public List<UserRegion> findAll(
        User user
    ) {
        return userRegionRepository.findAllByUser(user);
    }
}
