package roomescape.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.roomescape.ReservationTime;

public record ReservationTimeResponse(
        long timeId,

        @JsonFormat(pattern = "kk:mm")
        LocalTime startAt
) {
    public static ReservationTimeResponse from(final ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt()
        );
    }
}