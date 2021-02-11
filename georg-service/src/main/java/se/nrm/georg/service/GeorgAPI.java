package se.nrm.georg.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import java.io.File;
import java.io.FileInputStream; 
import java.io.IOException; 
import java.io.OutputStream;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam; 
import javax.ws.rs.core.MediaType; 
import javax.ws.rs.core.Response; 
import javax.ws.rs.core.StreamingOutput;
import lombok.extern.slf4j.Slf4j; 
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import se.nrm.georg.service.logic.GeorgLogic;
import se.nrm.georg.service.logic.exceptions.ErrorMessageBuilder;

/**
 *
 * @author idali
 */
@Path("/")
@Api(tags = {"georg"})
@SwaggerDefinition(
        info = @Info(
                title = "Georg API",
                version = "2.1"
        ),
        tags = {
          @Tag(name = "georg", description = "Georeference tool")
        })
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class GeorgAPI {

  private final String file = "file";

  @Inject
  private GeorgLogic logic;
  @Inject
  private ErrorMessageBuilder errorBuilder;

  @GET
  @Path("/geoCoding")
  @ApiOperation(value = "Get geocoding",
          notes = "Return search results in json",
          response = String.class
  )
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGeoCode(@QueryParam("address") String address,
          @QueryParam("source") String source, @QueryParam("layer") String layer,
          @QueryParam("countryCode") String countryCode,
          @QueryParam("size") int size) {
    log.info("getGeoCode: {}, {}", address, source);
   
    return Response.ok(logic.searchAddress(address, source, layer, countryCode, size)).build();
  }

  @GET
  @Path("/search")
  @ApiOperation(value = "Search",
          notes = "Return search results in json",
          response = String.class
  )
  @Produces(MediaType.APPLICATION_JSON)
  public Response search(@QueryParam("text") String text,
          @QueryParam("sources") String sources,
          @QueryParam("layers") String layers,
          @QueryParam("countryCode") String countryCode,
          @QueryParam("size") int size) {
    log.info("search: {}, {}", text, countryCode);

    return Response.ok(logic.runAutocompleteSearch(text, sources, layers, countryCode, size)).build();
  }

  @GET
  @Path("/reverse")
  @ApiOperation(value = "Get reverse geocoding",
          notes = "Return search results in json",
          response = String.class
  )
  @Produces(MediaType.APPLICATION_JSON)
  public Response getReverseGeoCode(@QueryParam("lat") double lat,
          @QueryParam("lng") double lon) {
    log.info("getReverseGeoCode: {}, {}", lat, lon);

    return Response.ok(logic.reverseSearch(lat, lon)).build();
  }

  @GET
  @Path("/coordinates")
  @ApiOperation(value = "Search coordinates in different formate",
          notes = "Return search results in json",
          response = String.class
  )
  @Produces(MediaType.APPLICATION_JSON)
  public Response searchCoordinates(@QueryParam("coordinates") String coordinates) {
    log.info("searchCoordinates: {}", coordinates);

    try {
      return Response.ok(logic.coordinatesSearch(coordinates)).build();
    } catch (NumberFormatException ex) {
      log.info(ex.getMessage());
      return Response.ok(errorBuilder.buildInvalidCoordinatesMessage()).build();
    }
  }

  @POST
  @Path("/upload")
  @ApiOperation(value = "Batch upload",
          notes = "Upload csv file with minimum two columns (Id, SourceLocality). Return search results in json",
          response = String.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "File uploaded")})
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response uploadFile(MultipartFormDataInput input, @QueryParam("type") String returnType) throws IOException {
    log.info("upload : {}", returnType);

    InputPart uploadFile = input.getFormDataMap().get(file).get(0); 
 
    String filePath = logic.processBatch(uploadFile, returnType); 
    if(filePath == null) {  
      return Response.ok(errorBuilder.buildInvalidCSVFileMessage()).build(); 
    }
     
    log.info("file path... {}", filePath);  
    File fileDownload = new File(filePath);
    StreamingOutput stream = (OutputStream out) -> { 
      try(FileInputStream inp = new FileInputStream(fileDownload)) {
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = inp.read(buff)) >= 0) {
          out.write(buff, 0, len);
        }
        out.flush();
      } catch (Exception e) {
        log.error(e.getMessage());
      } finally {
        log.info("Remove stream file: " + fileDownload);
        fileDownload.delete();
      } 
    };
    return Response.ok(stream).build(); 
  } 
}
