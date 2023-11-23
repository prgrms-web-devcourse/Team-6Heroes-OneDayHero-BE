package com.sixheroes.onedayheroapi.auth;


import com.sixheroes.onedayheroapplication.auth.infra.AuthService;
import com.sixheroes.onedayheroapplication.auth.infra.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCon {

    @Autowired
    private AuthService authService;

    @GetMapping("/save/{userId}")
    public String save(@PathVariable Long userId, HttpServletResponse response) {
        var refreshToken = authService.generateRefreshToken(userId);
        createCookie(response, refreshToken);

        return refreshToken;
    }

    @GetMapping("/find/{key}")
    public ResponseEntity<RefreshToken> find(@PathVariable String key) {
        var refreshToken = authService.findRefreshToken(key);

        return ResponseEntity.ok(refreshToken);
    }

    public void createCookie(
            HttpServletResponse response,
            String refreshToken
    ) {
        Cookie cookie = new Cookie("refreshToken",  refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
