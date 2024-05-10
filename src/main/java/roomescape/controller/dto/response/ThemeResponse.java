package roomescape.controller.dto.response;

import roomescape.domain.roomescape.Theme;

public record ThemeResponse(
        long id,
        String name,
        String description,
        String thumbnail
) {
    public static ThemeResponse from(final Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getThemeNameValue(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }
}
