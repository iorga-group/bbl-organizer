package com.iorga.bblorganizer.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iorga.cas.client.AbstractAutoLogOnceCASFilter;

public class CASFilter extends AbstractAutoLogOnceCASFilter {

	@Override
	protected boolean tryLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return true;
	}

	@Override
	protected boolean haveToCasFilter(HttpServletRequest request, ServletResponse response, FilterChain filterChain) {
		return true;
	}

	@Override
	protected void doAfterCASFiltersAndLoggedOnce(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain, boolean loggedIn) throws IOException, ServletException {
		if (loggedIn) {
			super.doAfterCASFiltersAndLoggedOnce(httpRequest, httpResponse, chain, loggedIn);
		} else {
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must log in.");
		}
	}

}
