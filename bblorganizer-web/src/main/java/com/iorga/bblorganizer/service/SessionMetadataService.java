package com.iorga.bblorganizer.service;

import com.google.common.collect.Lists;
import com.iorga.bblorganizer.model.entity.SessionMetadata;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@ApplicationScoped
public class SessionMetadataService {
    @Inject
    @RequestScoped
    private DBI dbi;

    public static class SessionMetadataMapper implements ResultSetMapper<SessionMetadata> {
        @Override
        public SessionMetadata map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
            final SessionMetadata sessionMetadata = new SessionMetadata();
            sessionMetadata.setId(resultSet.getLong("ID"));
            sessionMetadata.setBaggerName(resultSet.getString("BAGGER_NAME"));
            sessionMetadata.setTitle(resultSet.getString("TITLE"));
            sessionMetadata.setAchievementDate(resultSet.getDate("ACHIEVEMENT_DATE"));
            sessionMetadata.setCreationDate(resultSet.getDate("CREATION_DATE"));
            sessionMetadata.setPlannedDate(resultSet.getDate("PLANNED_DATE"));
            return sessionMetadata;
        }

    }
    private static interface Queries {
        @SqlQuery("select * from SESSION_METADATA")
        @Mapper(SessionMetadataMapper.class)
        List<SessionMetadata> findAll();

        @SqlUpdate("insert into SESSION_METADATA(BAGGER_NAME, TITLE) values (:baggerName, :title)")
        void insert(@Bind("baggerName") String baggerName, @Bind("title") String title);

        @SqlQuery("select * from SESSION_METADATA where BAGGER_NAME = :baggerName and TITLE = :title")
        @Mapper(SessionMetadataMapper.class)
        SessionMetadata findForUsername(@Bind("baggerName") String baggerName, @Bind("title") String title);
    }

    public List<SessionMetadata> findAll() {
        try (Handle handle = dbi.open()) {
            Queries queries = handle.attach(Queries.class);
            return queries.findAll();
        }
    }

    public List<SessionMetadata> create(List<SessionMetadata> sessionMetadatas) {
        try (Handle handle = dbi.open()) {
            Queries queries = handle.attach(Queries.class);
            List<SessionMetadata> returnedSessionMetadatas = Lists.newLinkedList();
            for(SessionMetadata sessionMetadata : sessionMetadatas) {
                String baggerName = sessionMetadata.getBaggerName();
                String title = sessionMetadata.getTitle();
                queries.insert(baggerName, title);
                returnedSessionMetadatas.add(queries.findForUsername(baggerName, title));
            }
            return returnedSessionMetadatas;
        }
    }
}
