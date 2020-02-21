package se.nrm.georg.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Path("/")
@Api(tags = {"georg"})
@SwaggerDefinition(tags = {
  @Tag(name = "georg", description = "Georeference tool")
})
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class GeorgAPI {
  
  @GET
  @Path("/run")
  @ApiOperation(value = "Run Hello World",
          notes = "Returns status",
          response = String.class
  )
  @Produces(MediaType.APPLICATION_JSON)
  public Response run() {  
    return Response.ok("Hello world").build();
  }
  
}
