package com.iorga.bblorganizer.ws;

import com.iorga.bblorganizer.model.entity.Vote;
import com.iorga.bblorganizer.service.VoteService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.List;

@ApplicationScoped
@Path("/votes")
public class VotesWS {
	@Inject
	private VoteService voteService;

	public static class VotesVoteRequest {
		private long idSessionMetadata;

        public long getIdSessionMetadata() {
            return idSessionMetadata;
        }
        public void setIdSessionMetadata(long idSessionMetadata) {
            this.idSessionMetadata = idSessionMetadata;
        }
    }
	@POST
	@Path("/vote")
	public void vote(@Context HttpServletRequest httpRequest, VotesVoteRequest request) {
		voteService.create(request.getIdSessionMetadata(), httpRequest.getUserPrincipal().getName());
	}

	@POST
	@Path("/unvote")
	public void unvote(@Context HttpServletRequest httpRequest, VotesVoteRequest request) {
		voteService.delete(request.getIdSessionMetadata(), httpRequest.getUserPrincipal().getName());
	}

	//TODO not used
	@GET
	@Path("/mine")
	public List<Vote> listVotesForCurrentUser(@Context HttpServletRequest httpRequest) {
		return voteService.findForUserName(httpRequest.getUserPrincipal().getName());
	}

	@GET
	@Path("/list")
	public List<Vote> listAllVotes() {
		return voteService.findAll();
	}
}
