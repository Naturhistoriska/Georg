package se.nrm.georg.service.logic.services;

import java.io.Serializable;
import java.net.ConnectException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import se.nrm.georg.service.logic.exceptions.GeorgException;

/**
 *
 * @author idali
 */
@Slf4j
public class ExternalServices implements Serializable {

//  private Client client;
//  private WebTarget target;

  public ExternalServices() {

  }

  public String search(String servicePath) throws GeorgException {
    log.info("getResult : {}", servicePath);

//    Client client = ClientBuilder.newClient();
    Response response = null;
    try {
      response = ClientBuilder.newClient()
              .target(servicePath)
              .request(MediaType.APPLICATION_JSON)
              .get();

//      target = client.target(servicePath);
//      Response response = target.request(MediaType.APPLICATION_JSON).get(); 
      return response.readEntity(String.class);
    } catch (ProcessingException ex) {
      if (ex.getCause() instanceof ConnectException) {
        throw new GeorgException("Pelias is not available");
      }
      throw ex;
    } 
  }
}
