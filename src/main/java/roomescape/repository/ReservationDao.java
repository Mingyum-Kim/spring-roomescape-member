package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper;

    public ReservationDao(
            final DataSource dataSource,
            final RowMapper<Reservation> reservationRowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("ID");
        this.reservationRowMapper = reservationRowMapper;
    }

    public Reservation save(final Reservation reservation) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(reservation);
        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return reservation.assignId(id);
    }

    public List<Reservation> getAll() {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, time.id AS time_id, time.start_at AS time_value, 
                    theme.id AS theme_id, theme.name AS theme_name, theme.description, theme.thumbnail 
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id
                INNER JOIN theme ON r.theme_id = theme.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public List<Reservation> findByTimeId(final long timeId) {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, time.id AS time_id, time.start_at AS time_value, 
                theme.id AS theme_id, theme.name AS theme_name, theme.description, theme.thumbnail 
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id 
                INNER JOIN theme ON r.theme_id = theme.id
                WHERE r.time_id = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, timeId);
    }

    public List<Reservation> findByThemeId(final long themeId) {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, time.id AS time_id, time.start_at AS time_value, 
                theme.id AS theme_id, theme.name AS theme_name, theme.description, theme.thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS time ON r.time_id = time.id 
                INNER JOIN theme ON r.theme_id = theme.id
                WHERE r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, themeId);
    }


    public List<Long> findByDateAndTimeIdAndThemeId(final LocalDate date, final long timeId, final long themeId) {
        String sql = "SELECT id FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum) -> resultSet.getLong("time_id"),
                date.toString(), timeId, themeId
        );
    }

    public List<Long> findTimeIdsByDateAndThemeId(final LocalDate date, final long themeId) {
        String sql = "SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?";
        return jdbcTemplate.query(
                sql, (resultSet, rowNum) -> resultSet.getLong("id"),
                date.toString(), themeId
        );
    }

    public List<Long> findRanking(final LocalDate from, final LocalDate to, final int count) {
        String sql = """
                SELECT theme_id, count(*) AS count FROM reservation
                WHERE date BETWEEN ? AND ?
                GROUP BY theme_id
                ORDER BY count DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> resultSet.getLong("theme_id"),
                from, to, count
        );
    }

    public int delete(final long id) {
        return jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", Long.valueOf(id));
    }
}
