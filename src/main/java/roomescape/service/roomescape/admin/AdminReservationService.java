package roomescape.service.roomescape.admin;

import org.springframework.stereotype.Service;
import roomescape.controller.dto.request.AdminReservationSaveRequest;
import roomescape.domain.member.Member;
import roomescape.domain.roomescape.Reservation;
import roomescape.domain.roomescape.ReservationTime;
import roomescape.domain.roomescape.Theme;
import roomescape.repository.member.MemberDao;
import roomescape.repository.roomescape.ReservationDao;
import roomescape.repository.roomescape.ReservationTimeDao;
import roomescape.repository.roomescape.ThemeDao;

@Service
public class AdminReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final MemberDao memberDao;

    public AdminReservationService(
            final ReservationDao reservationDao,
            final ReservationTimeDao reservationTimeDao,
            final ThemeDao themeDao,
            final MemberDao memberDao
    ) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.memberDao = memberDao;
    }

    public Reservation save(final AdminReservationSaveRequest reservationSaveRequest) {
        ReservationTime reservationTime = findReservationTimeById(reservationSaveRequest);
        Theme theme = findThemeById(reservationSaveRequest);
        Member member = findMemberById(reservationSaveRequest);

        return new Reservation()
    }

    private ReservationTime findReservationTimeById(final AdminReservationSaveRequest reservationSaveRequest) {
        return reservationTimeDao.findById(reservationSaveRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 예약 가능 시간 번호를 입력하였습니다."));
    }

    private Theme findThemeById(final AdminReservationSaveRequest reservationSaveRequest) {
        return themeDao.findById(reservationSaveRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 테마 번호를 입력하였습니다."));
    }

    private Member findMemberById(final AdminReservationSaveRequest reservationSaveRequest) {
        return memberDao.findById(reservationSaveRequest.memberId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 회원 번호를 입력하였습니다."));
    }
}