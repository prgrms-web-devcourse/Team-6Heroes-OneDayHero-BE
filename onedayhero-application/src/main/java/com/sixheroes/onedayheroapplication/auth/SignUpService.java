package com.sixheroes.onedayheroapplication.auth;

import com.sixheroes.onedayheroapplication.auth.response.UserLoginResponse;
import com.sixheroes.onedayherodomain.user.Email;
import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.UserRole;
import com.sixheroes.onedayherodomain.user.UserSocialType;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserLoginResponse singUp(
            String userSocialType,
            String email
    ) {
        var createdEmail = Email.builder()
                .email(email)
                .build();

        var createdUser = User.singUpUser(
                createdEmail,
                UserSocialType.findByName(userSocialType),
                UserRole.MEMBER
        );

        var savedUser = userRepository.save(createdUser);

        return UserLoginResponse.from(savedUser);
    }
}
