package org.acme.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.acme.model.Visits;
import org.acme.service.VisitsService;
import org.jboss.logging.Logger;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class VisitsResource {

    private static final Logger LOG = Logger.getLogger(VisitsService.class);

    @Inject
    VisitsService visitsService;
    
    // Note: To test in brower or curl:
    //       $ curl "http://localhost:6060/pets/visits?petIds=8&petIds=7"
    //
    @GET
    @Path("pets/visits")
    public List<Visits> visitsMultiGet(@QueryParam("petIds") List<Long> petIds) {
        return visitsService.findByMultiPetIds(petIds);
    }

    @GET
    @Path("owners/*/pets/{petId}/visits")
    public List<Visits> visits(@PathParam("petId") long petId) {
        return visitsService.findByPetId(petId);
    }

    @POST
    @Path("owners/*/pets/{petId}/visits")
    public Response create(@PathParam("petId") long petId, Visits theVisits,  @Context UriInfo uriInfo) {
        theVisits.petId = petId;

        LOG.info("Persisting: " + theVisits);
        visitsService.save(theVisits);

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();

        return Response.created(uriBuilder.build()).entity(theVisits).build();
    }

}