package roomescape.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.member.Member;

public record SignupRequest(
        @NotNull
        String name,

        @NotNull
        @Email
        String email,

        @NotNull
        String password
) {
    public Member toEntity() {
        return Member.of(name, email, password, "member");
    }
}
