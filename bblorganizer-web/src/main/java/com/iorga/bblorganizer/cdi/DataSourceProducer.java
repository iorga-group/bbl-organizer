package com.iorga.bblorganizer.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@ApplicationScoped
public class DataSourceProducer {
	@Produces @ApplicationScoped
	public DataSource createDataSource() throws NamingException {
		return (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/BBLOrganizer");
	}
}
