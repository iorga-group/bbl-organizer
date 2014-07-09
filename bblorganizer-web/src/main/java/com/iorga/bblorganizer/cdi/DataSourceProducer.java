package com.iorga.bblorganizer.cdi;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;

import com.iorga.bblorganizer.service.ConfigurationService;

@ApplicationScoped
public class DataSourceProducer {
	@Inject
	private ConfigurationService configurationService;

	private JdbcDataSource dataSource;
	private Server server;

	@Produces @ApplicationScoped
	public DataSource getDataSource() throws NamingException, SQLException {
		dataSource = new JdbcDataSource();
		dataSource.setUrl("jdbc:h2:"+configurationService.getProperty("db.path"));
		dataSource.setUser("sa");
		dataSource.setPassword("");
		// start the server
		server = Server.createTcpServer("-tcpAllowOthers").start();
		return dataSource;
	}

	public void disposeDataSource(@Disposes DataSource dataSource) throws SQLException {
		server.stop();
	}
}
