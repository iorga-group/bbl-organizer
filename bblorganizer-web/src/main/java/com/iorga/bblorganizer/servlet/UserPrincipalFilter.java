package com.iorga.bblorganizer.servlet;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@WebFilter("/*")
public class UserPrincipalFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request) {
			@Override
			public Principal getUserPrincipal() {
				return new UserPrincipal() {
					@Override
					public String getName() {
						return "toto";
					}
				};
			}
		}, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
