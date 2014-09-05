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
			vote.setIdSessionMetadata(resultSet.getLong("ID_SESSION_METADATA"));
			vote.setUserName(resultSet.getString("USER_NAME"));
			return vote;
		}
	}

	private static interface Queries {
		@SqlUpdate("insert into VOTE(ID_SESSION_METADATA, USER_NAME, VOTE_DATE) values (:idSessionMetadata, :userName, now())")
		void insertVote(@Bind("idSessionMetadata") long idSessionMetadata, @Bind("userName") String userName);

		@SqlUpdate("delete from VOTE where ID_SESSION_METADATA = :idSessionMetadata and USER_NAME = :userName")
		void deleteVote(@Bind("idSessionMetadata") long idSessionMetadata, @Bind("userName") String userName);

		//TODO not used
		@SqlQuery("select * from VOTE where USER_NAME = :userName")
		@Mapper(VoteMapper.class)
		List<Vote> findForUsername(@Bind("userName") String userName);

		@SqlQuery("select * from VOTE")
		@Mapper(VoteMapper.class)
		List<Vote> findAll();
	}

	public void create(long idSessionMetadata, String userName) {
		try (Handle handle = dbi.open()) {
			Queries queries = handle.attach(Queries.class);
			queries.insertVote(idSessionMetadata, userName);
		}
	}

	public void delete(long idSessionMetadata, String userName) {
		try (Handle handle = dbi.open()) {
			Queries queries = handle.attach(Queries.class);
			queries.deleteVote(idSessionMetadata, userName);
		}
	}

	//TODO not used
	public List<Vote> findForUserName(String userName) {
		try (Handle handle = dbi.open()) {
			Queries queries = handle.attach(Queries.class);
			return queries.findForUsername(userName);
		}
	}

	public List<Vote> findAll() {
		try (Handle handle = dbi.open()) {
			Queries queries = handle.attach(Queries.class);
			return queries.findAll();
		}
	}

}
