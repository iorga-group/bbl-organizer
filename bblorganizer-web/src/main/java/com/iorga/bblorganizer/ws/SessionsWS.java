package com.iorga.bblorganizer.ws;

import com.iorga.bblorganizer.model.entity.SessionMetadata;
import com.iorga.bblorganizer.service.SessionMetadataService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@ApplicationScoped
@Path("/sessions")
public class SessionsWS {
    @Inject
    private SessionMetadataService sessionMetadataService;

    @GET
    @Path("/list")
    public List<SessionMetadata> list() {
        return sessionMetadataService.findAll();
    }

    @POST
    @Path("/create")
    public List<SessionMetadata> create(List<SessionMetadata> sessionMetadatas) {
        return sessionMetadataService.create(sessionMetadatas);
    }
}
