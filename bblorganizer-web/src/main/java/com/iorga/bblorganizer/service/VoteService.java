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
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.iorga.bblorganizer.model.entity.Vote;

@ApplicationScoped
public class VoteService {
	@Inject @RequestScoped
	private DBI dbi;

	public static class VoteMapper implements ResultSetMapper<Vote> {
		@Override
		public Vote map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
			final Vote vote = new Vote();
			vote.setBaggerName(resultSet.getString("BAGGER_NAME"));
			vote.setSessionTitle(resultSet.getString("SESSION_TITLE"));
			vote.setUserName(resultSet.getString("USER_NAME"));
			return vote;
		}
	}

	private static interface Queries {
		@SqlUpdate("insert into VOTE(BAGGER_NAME, SESSION_TITLE, USER_NAME, VOTE_DATE) values (:baggerName, :sessionTitle, :userName, now())")
		void insertVote(@Bind("baggerName") String baggerName, @Bind("sessionTitle") String sessionTitle, @Bind("userName") String userName);

		@SqlUpdate("delete from VOTE where BAGGER_NAME = :baggerName and SESSION_TITLE = :sessionTitle and USER_NAME = :userName")
		void deleteVote(@Bind("baggerName") String baggerName, @Bind("sessionTitle") String sessionTitle, @Bind("userName") String userName);

		@SqlQuery("select * from VOTE where USER_NAME = :userName")
		@Mapper(VoteMapper.class)
		List<Vote> findForUsername(@Bind("userName") String userName);
	}

	public void create(String baggerName, String sessionTitle, String userName) {
		try (Handle handle = dbi.open()) {
			Queries queries = handle.attach(Queries.class);
			queries.insertVote(baggerName, sessionTitle, userName);
		}
	}

	public void delete(String baggerName, String sessionTitle, String userName) {
		try (Handle handle = dbi.open()) {
			Queries queries = handle.attach(Queries.class);
			queries.deleteVote(baggerName, sessionTitle, userName);
		}
	}

	public List<Vote> findForUserName(String userName) {
		try (Handle handle = dbi.open()) {
			Queries queries = handle.attach(Queries.class);
			return queries.findForUsername(userName);
		}
	}

}
