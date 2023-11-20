package com.sixheroes.onedayheroapplication.user;

import com.sixheroes.onedayheroapplication.user.response.UserAuthResponse;
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
public class UserSignUpService {

    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserAuthResponse singUp(
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
        System.out.println("로그인6");
        var singUpUser = userRepository.save(createdUser);
        System.out.println("로그인8");
        return UserAuthResponse.from(singUpUser);
    }
}
