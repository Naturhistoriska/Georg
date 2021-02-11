package se.nrm.georg.service.logic.services;

import java.io.Serializable;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Slf4j
public class ExternalServices implements Serializable {
  
  private Client client;
  private WebTarget target;
  
  public ExternalServices() {
    
  }
  
  public String search(String servicePath) {
    log.info("getResult : {}", servicePath);
    
    client = ClientBuilder.newClient();  
    target = client.target(servicePath);
     
    Response response = target.request(MediaType.APPLICATION_JSON).get(); 
    return response.readEntity(String.class);   
  } 
}
