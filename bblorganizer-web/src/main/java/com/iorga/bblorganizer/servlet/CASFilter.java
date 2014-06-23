package com.iorga.bblorganizer.servlet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iorga.cas.client.AbstractAutoLogOnceCASFilter;

public class CASFilter extends AbstractAutoLogOnceCASFilter {

	@Override
	protected boolean tryLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return false;
	}

	@Override
	protected boolean haveToCasFilter(HttpServletRequest request, ServletResponse response, FilterChain filterChain) {
		return true;
	}

}
