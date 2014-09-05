package com.iorga.bblorganizer.ws;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.security.Principal;

@ApplicationScoped
@Path("/authentication")
public class AuthenticationWS {

	@GET
	@Path("/userPrincipal")
	public Principal getUserPrincipal(@Context HttpServletRequest httpRequest) {
		return httpRequest.getUserPrincipal();
	}
}
