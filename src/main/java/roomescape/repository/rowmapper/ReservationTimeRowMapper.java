package roomescape.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.ReservationTime;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class ReservationTimeRowMapper implements RowMapper<ReservationTime> {
    @Override
    public ReservationTime mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return ReservationTime.of(
                    resultSet.getLong("id"),
                    resultSet.getString("start_at")
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}