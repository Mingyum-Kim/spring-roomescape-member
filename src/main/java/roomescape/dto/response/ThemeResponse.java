package roomescape.dto.response;

import roomescape.domain.Theme;

public record ThemeResponse(
        long themeId,
        String name,
        String description,
        String thumbnail
) {
    public static ThemeResponse from(final Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }
}