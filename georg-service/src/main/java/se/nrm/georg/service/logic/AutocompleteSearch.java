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
public class AutocompleteSearch implements Serializable {
   
  private Client client;
  private WebTarget target;
 
  public String search(String text, int size, String peliasPath) {
    
    client = ClientBuilder.newClient(); 
    String peliaUrl = Util.getInstance()
            .bunildAutoCompleteSearchPath(peliasPath, text, size);  
    target = client.target(peliaUrl);
     
    Response response = target.request(MediaType.APPLICATION_JSON).get();
    String jsonString = response.readEntity(String.class);   
    return jsonString; 
  }
}
