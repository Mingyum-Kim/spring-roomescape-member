package roomescape.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.jwt.JwtTokenExtractor;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public class AdminCheckInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public AdminCheckInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws AccessDeniedException {
        String token = JwtTokenExtractor.extractTokenFromCookies(request.getCookies());
        Member member = authService.findMemberByToken(token);
        if (member == null || !member.getRole().equals(Role.ADMIN)) {
            throw new SecurityException("[ERROR] 접근 권한이 없습니다.");
        }
        return true;
    }
}