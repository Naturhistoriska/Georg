package se.nrm.georg.service.logic;
 
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
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
public class GeoCodingTest {
  
  private GeoCoding instance;
  
  @Mock
  private InitialProperties mockProps; 
  @Mock
  private Client client;
  @Mock
  private WebTarget target;
  @Mock
  private Response response;
  @Mock
  private Builder builder;
   
  private String peliasPath; 
  private String jsonString;
  
  public GeoCodingTest() {
  }
   
  
  @Before
  public void setUp() {
    peliasPath = "http://api.nrm.se";
    when(mockProps.getPeliasPath()).thenReturn(peliasPath);
    
    jsonString = "\"data\": {\"test\"}"; 
      
    when(response.readEntity(String.class)).thenReturn(jsonString);
    
    when(target.request(MediaType.APPLICATION_JSON)).thenReturn(builder);
    when(builder.get()).thenReturn(response);
    when(client.target(any(String.class))).thenReturn(target);
    PowerMockito.mockStatic(ClientBuilder.class);  
    Mockito.when(ClientBuilder.newClient()).thenReturn(client);
    
    instance = new GeoCoding(mockProps);
  }
  
  @After
  public void tearDown() {
    instance = null;
  }
  
  @Test(expected = NullPointerException.class)
  public void testDefaultConstructor() {
    instance = new GeoCoding();
    assertNotNull(instance);
    instance.init(); 
  }

  /**
   * Test of init method, of class GeoCoding.
   */
  @Test
  public void testInit() {
    System.out.println("init"); 
    assertNotNull(instance);
    instance.init();  
  }

  /**
   * Test of getGeoCode method, of class GeoCoding.
   */
  @Test
  public void testGetGeoCode() {
    System.out.println("getGeoCode");

    String address = "stockholm"; 
    String source = "GBIF";
    String layer = "myLayer";
 
    instance.init();
    String result = instance.getGeoCode(address, source, layer); 
    assertEquals(jsonString, result); 
    
    verify(mockProps, times(1)).getPeliasPath();  
    verify(client, times(1)).target(any(String.class));
    verify(target, times(1)).request(any(String.class));
    verify(builder, times(1)).get();
    verify(response, times(1)).readEntity(String.class);
  }

  /**
   * Test of getReverseGeoCode method, of class GeoCoding.
   */
  @Test
  public void testGetReverseGeoCode() {
    System.out.println("getReverseGeoCode");
    double lat = 59.6;
    double lon = 18.7;  
    
    instance.init();
    String result = instance.getReverseGeoCode(lat, lon);
    assertEquals(jsonString, result); 
    
    verify(mockProps, times(1)).getPeliasPath();  
    verify(client, times(1)).target(any(String.class));
    verify(target, times(1)).request(any(String.class));
    verify(builder, times(1)).get();
    verify(response, times(1)).readEntity(String.class); 
  }
  
}
