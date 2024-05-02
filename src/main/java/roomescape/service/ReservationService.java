package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.SelectableTimeResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(
            final ReservationDao reservationDao,
            final ReservationTimeDao reservationTimeDao,
            final ThemeDao themeDao
    ) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse save(final ReservationRequest reservationRequest) {
        ReservationTime reservationTime = findReservationTimeById(reservationRequest);
        Theme theme = findThemeById(reservationRequest);

        if (hasDuplicateReservation(reservationRequest.date(), reservationRequest.timeId(), reservationRequest.themeId())) {
            throw new IllegalArgumentException("[ERROR] 중복된 예약이 존재합니다.");
        }
        Reservation reservation = reservationRequest.toEntity(reservationTime, theme);
        return ReservationResponse.from(reservationDao.save(reservation));
    }

    private ReservationTime findReservationTimeById(ReservationRequest reservationRequest) {
        return reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 예약 가능 시간 번호를 입력하였습니다."));
    }

    private Theme findThemeById(ReservationRequest reservationRequest) {
        return themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 잘못된 테마 번호를 입력하였습니다."));
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = getAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private List<Reservation> getAll() {
        List<Reservation> reservations = reservationDao.getAll();
        if (reservations.isEmpty()) {
            throw new IllegalStateException("[ERROR] 방탈출 예약 내역이 없습니다.");
        }
        return reservations;
    }

    public List<SelectableTimeResponse> findSelectableTime(final LocalDate date, final long themeId) {
        List<Long> usedTimeId = reservationDao.findTimeIdByDateAndThemeId(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeDao.getAll();

        return reservationTimes.stream()
                .map(time -> new SelectableTimeResponse(
                        time.getId(),
                        time.getStartAt(),
                        isAlreadyBooked(time, usedTimeId)
                ))
                .toList();
    }

    private boolean isAlreadyBooked(ReservationTime reservationTime, List<Long> usedTimeId) {
        return usedTimeId.contains(reservationTime.getId());
    }

    private boolean hasDuplicateReservation(final LocalDate date, final long timeId, final long themeId) {
        return !reservationDao.findByDateAndTimeIdAndThemeId(date, timeId, themeId).isEmpty();
    }

    public void delete(final long id) {
        findReservationById(id);
        reservationDao.delete(id);
    }

    private Reservation findReservationById(long id) {
        return reservationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 삭제할 예약 데이터가 없습니다."));
    }
}
