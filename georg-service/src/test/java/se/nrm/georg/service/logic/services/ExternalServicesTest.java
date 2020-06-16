package se.nrm.georg.service.logic.services;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;  

/**
 *
 * @author idali
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Client.class, ClientBuilder.class})
@PowerMockIgnore({"javax.management.*", 
  "org.apache.http.conn.ssl.*", 
  "com.amazonaws.http.conn.ssl.*", 
  "javax.net.ssl.*"})  
public class ExternalServicesTest {
  
  private ExternalServices instance;
   
  @Mock
  private Client client;
  @Mock
  private WebTarget target;
  @Mock
  private Response response;
  @Mock
  private Invocation.Builder builder;
   
  private String servicePath; 
  private String jsonString;
 
  public ExternalServicesTest() {
  }
  
  @Before
  public void setUp() {
    servicePath = "http://api.nrm.se";  
    jsonString = "\"data\": {\"test\"}"; 
      
    when(response.readEntity(String.class)).thenReturn(jsonString);
    
    when(target.request(MediaType.APPLICATION_JSON)).thenReturn(builder);
    when(builder.get()).thenReturn(response);
    when(client.target(any(String.class))).thenReturn(target);
    PowerMockito.mockStatic(ClientBuilder.class);  
    Mockito.when(ClientBuilder.newClient()).thenReturn(client);
    
    instance = new ExternalServices();
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of getResults method, of class ExternalServices.
   */
  @Test
  public void testGetResults() {
    System.out.println("getResults");   
    String result = instance.getResults(servicePath);
    assertEquals(jsonString, result); 
     
    verify(client, times(1)).target(any(String.class));
    verify(target, times(1)).request(any(String.class));
    verify(builder, times(1)).get();
    verify(response, times(1)).readEntity(String.class);
  } 
}
