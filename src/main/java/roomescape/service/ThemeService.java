package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ThemeDao;

@Service
public class ThemeService {
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ThemeService(final ThemeDao themeDao, final ReservationDao reservationDao) {
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }

    public ThemeResponse save(final ThemeRequest themeRequest) {
        Theme theme = themeRequest.toEntity();
        return ThemeResponse.from(themeDao.save(theme));
    }

    public List<ThemeResponse> getAll() {
        return themeDao.getAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void delete(final long id) {
        themeDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 테마 번호 입력입니다."));

        if (!reservationDao.findByThemeId(id).isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 해당 테마를 사용 중인 예약이 있어 삭제할 수 없습니다.");
        }
        themeDao.deleteById(id);
    }
}