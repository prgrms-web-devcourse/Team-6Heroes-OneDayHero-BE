package com.sixheroes.onedayheroapplication.auth;


import com.sixheroes.onedayheroapplication.auth.response.UserLoginResponse;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final SignUpService signUpService;

    @Transactional(readOnly = true)
    public UserLoginResponse login(
            String userSocialType,
            String email
    ) {
        return userRepository.findByEmail_Email(email)
                .map(UserLoginResponse::from)
                .orElseGet(() -> signUpService.singUp(
                        userSocialType,
                        email
                ));
    }
}
