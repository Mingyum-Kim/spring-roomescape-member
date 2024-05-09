package roomescape.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

@Component
public class MemberRowMapper implements RowMapper<Member> {
    @Override
    public Member mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return Member.of(
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}