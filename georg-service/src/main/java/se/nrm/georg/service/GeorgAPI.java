package se.nrm.georg.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Info;
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
import se.nrm.georg.service.logic.GeorgLogic;

/**
 *
 * @author idali
 */
@Path("/")
@Api(tags = {"georg"})
@SwaggerDefinition(
  info = @Info(
                title = "George api",
                version = "0.2.0"
        ),
  tags = {
          @Tag(name = "georg", description = "Georeference tool")
        })
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class GeorgAPI {

  @Inject
  private GeoCoding geoCoding;
  @Inject
  private GeorgLogic logic;

  @GET
  @Path("/geoCoding")
  @ApiOperation(value = "Get geocoding",
          notes = "Returns status",
          response = String.class
  )
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGeoCode(@QueryParam("address") String address, @QueryParam("source") String source, 
          @QueryParam("layer") String layer, @QueryParam("size") int size) {
    log.info("getGeoCode: {}, {}", address, source);

    return Response.ok(geoCoding.getGeoCode(address, source, layer, size)).build();
  }
  
  
  @GET
  @Path("/autocomplete")
  @ApiOperation(value = "Get geocoding",
          notes = "Returns status",
          response = String.class
  )
  @Produces(MediaType.APPLICATION_JSON)
  public Response autocomplete(@QueryParam("text") String text, @QueryParam("size") int size) {
    log.info("autocomplete: {}, {}", text, size);

    return Response.ok(logic.runAutocompleteSearch(text, size)).build();
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
