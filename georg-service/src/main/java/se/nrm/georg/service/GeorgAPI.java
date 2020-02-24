package se.nrm.georg.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag; 
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j; 
import se.nrm.georg.service.logic.GeoCoding;

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
  
  @Inject
  private GeoCoding geoCoding;
 
  @GET
  @Path("/geoCoding")
  @ApiOperation(value = "Get geocoding",
          notes = "Returns status",
          response = String.class
  )
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGeoCode(@QueryParam("address") String address) {  
    log.info("getGeoCode: {}, {}", address, geoCoding);
     
    return Response.ok(geoCoding.getGeoCode(address)).build();
  }
   
  @GET
  @Path("/reverse")
  @ApiOperation(value = "Get reverse geocoding",
          notes = "Returns status",
          response = String.class
  )
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReverseGeoCode(@QueryParam("lat") double lat, 
          @QueryParam("lng") double lon) {  
    log.info("getReverseGeoCode: {}, {}", lat, lon);
     
    return Response.ok(geoCoding.getReverseGeoCode(lat, lon)).build();
  }
}
