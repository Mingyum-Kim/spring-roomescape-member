package roomescape.dto.request;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeSaveRequest(
        LocalTime startAt
) {
    public ReservationTime toEntity() {
        return new ReservationTime(startAt);
    }
}
