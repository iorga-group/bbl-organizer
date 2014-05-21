package com.iorga.bblorganizer.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.iorga.bblorganizer.model.entity.AchievedSession;


@ApplicationScoped
public class AchievedSessionService {
	@Inject @RequestScoped
	private DBI dbi;

	public static class AchievedSessionMapper implements ResultSetMapper<AchievedSession> {
		@Override
		public AchievedSession map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
			final AchievedSession achievedSession = new AchievedSession();
			achievedSession.setBaggerName(resultSet.getString("BAGGER_NAME"));
			achievedSession.setTitle(resultSet.getString("TITLE"));
			achievedSession.setAchievementDate(resultSet.getDate("ACHIEVEMENT_DATE"));
			return achievedSession;
		}

	}
	private static interface Queries {
		@SqlQuery("select * from ACHIEVED_SESSION")
		@Mapper(AchievedSessionMapper.class)
		List<AchievedSession> findAll();
	}

	public List<AchievedSession> findAll() {
		try (Handle handle = dbi.open()) {
			Queries queries = handle.attach(Queries.class);
			return queries.findAll();
		}
	}

}
