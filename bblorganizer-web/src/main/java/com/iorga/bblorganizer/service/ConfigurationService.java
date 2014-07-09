package com.iorga.bblorganizer.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigurationService {
	private static final String CLASSPATH_PREFIX = "classpath:";
	private Properties config;

	@PostConstruct
	public void init() throws IOException {
		config = new Properties();
		config.load(getClass().getResourceAsStream("/config.properties"));
	}

	public String getProperty(String key) {
		return config.getProperty(key);
	}

	public InputStream getInputStream(String key) throws MalformedURLException, IOException {
		String url = getProperty(key);
		if (url.startsWith(CLASSPATH_PREFIX)) {
			return getClass().getResourceAsStream(url.substring(CLASSPATH_PREFIX.length()));
		} else {
			return new URL(url).openStream();
		}
	}
}
