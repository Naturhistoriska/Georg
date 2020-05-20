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
  
  public GeoCoding(InitialProperties props) {
    this.props = props;
  }
  
  @PostConstruct 
  public void init() {
    peliasPath = props.getPeliasPath();   
  }
  
  public String getGeoCode(String address, String source, String layer, int size) { 
    
    client = ClientBuilder.newClient(); 
    String peliaUrl = Util.getInstance()
            .buildGeoCodePath(peliasPath, address, source, layer, size);  
    target = client.target(peliaUrl);
     
    Response response = target.request(MediaType.APPLICATION_JSON).get();
    String jsonString = response.readEntity(String.class);   
    return jsonString;
  }
  
  public String getReverseGeoCode(double lat, double lon) {  

    client = ClientBuilder.newClient(); 
    target = client.target(Util.getInstance()
            .buildReverseGeoCodePath(peliasPath, lat, lon));
    Response response = target.request(MediaType.APPLICATION_JSON).get();
    String jsonString = response.readEntity(String.class);  
    return jsonString;
  }
  
}
