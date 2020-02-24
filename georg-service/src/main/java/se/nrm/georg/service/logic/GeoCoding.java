package se.nrm.georg.service.logic;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j; 
import se.nrm.georg.service.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class GeoCoding implements Serializable {
     
  @Inject  
  private InitialProperties props; 
  private String peliasPath; 
  private Client client;
  private WebTarget target;
  
  public GeoCoding() {
    
  }
  
  @PostConstruct 
  public void init() {
    peliasPath = props.getPeliasPath(); 
    client = ClientBuilder.newClient(); 
  }
  
  public String getGeoCode(String address) {
    log.info("getGeoCoding: address = {}", address);  
    target = client.target(Util.getInstance().buildGeoCodePath(peliasPath, address));
    Response response = target.request(MediaType.APPLICATION_JSON).get();
    String jsonString = response.readEntity(String.class); 
    log.info("jsonString: {}", jsonString);
    return jsonString;
  }
  
  public String getReverseGeoCode(double lat, double lon) { 
    log.info("getReverseGeoCode : {} -- {}", lat, lon);

    target = client.target(Util.getInstance()
            .buildReverseGeoCodePath(peliasPath, lat, lon));
    Response response = target.request(MediaType.APPLICATION_JSON).get();
    String jsonString = response.readEntity(String.class); 
    log.info("jsonString: {}", jsonString);
    return jsonString;
  }
  
}
