package roomescape.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.UserResponse;
import roomescape.service.auth.AuthService;

@RestController
public class TokenLoginController {
    private final AuthService authService;

    public TokenLoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, final HttpServletResponse response) {
        Cookie cookie = new Cookie("token", authService.createToken(loginRequest));
        response.addCookie(cookie);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<UserResponse> getUserInformation(final HttpServletRequest request) {
        String token = extractTokenFromCookies(request.getCookies());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.findUserByToken(token));
    }

    private String extractTokenFromCookies(final Cookie[] cookies) {
        return Arrays.asList(cookies)
                .stream()
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 쿠키에 토큰 정보를 입력해주세요."))
                .getValue();
    }
}
