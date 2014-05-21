package com.iorga.bblorganizer.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

import org.skife.jdbi.v2.DBI;

@ApplicationScoped
public class DBIProducer {
	@Produces @Dependent
	public DBI createDBI(DataSource dataSource) {
		return new DBI(dataSource);
	}
}
