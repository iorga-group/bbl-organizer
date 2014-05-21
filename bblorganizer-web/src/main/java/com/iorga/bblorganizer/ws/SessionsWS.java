package com.iorga.bblorganizer.ws;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.iorga.bblorganizer.model.entity.AchievedSession;
import com.iorga.bblorganizer.service.AchievedSessionService;

@ApplicationScoped
@Path("/sessions")
public class SessionsWS {
	@Inject
	private AchievedSessionService achievedSessionService;

	@GET
	@Path("/achieved")
	public List<AchievedSession> listAchievedSessions() {
		return achievedSessionService.findAll();
	}
}
