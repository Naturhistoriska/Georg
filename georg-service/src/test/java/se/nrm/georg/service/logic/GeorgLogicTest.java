package se.nrm.georg.service.logic;

import org.junit.After; 
import org.junit.AfterClass;
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import se.nrm.georg.service.logic.json.CoordinatesJson;
import se.nrm.georg.service.logic.services.ExternalServices;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class) 
public class GeorgLogicTest {
  
  private GeorgLogic instance;
  private String resultString;
  private String countryCode; 
  
  @Mock
  private InitialProperties mockProps; 
  @Mock
  private ExternalServices service; 
  @Mock
  private CoordinatesJson coordinates; 
  
  private String peliasPath; 
  
  public GeorgLogicTest() {
  }
  
  @Before
  public void setUp() {
    peliasPath = "http://api.nrm.se";
    resultString = "test result";
    countryCode = "SWE";
    
    when(mockProps.getPeliasPath()).thenReturn(peliasPath);
    when(service.getResults(any(String.class))).thenReturn(resultString);
    when(coordinates.addCoordinatesTransformation(any(String.class))).thenReturn(resultString);
    instance = new GeorgLogic(mockProps, service, coordinates);
  }
  
  @After
  public void tearDown() {
    instance = null;
  }
  
  @Test(expected = NullPointerException.class)
  public void testDefaultConstructorWithException() {
    instance = new GeorgLogic();
    assertNotNull(instance);
    instance.init(); 
  }
  
  @Test
  public void testDefaultConstructor() { 
    assertNotNull(instance);
    instance.init(); 
    assertNotNull(instance);
  }

  /**
   * Test of init method, of class GeorgLogic.
   */
  @Test
  public void testInit() {
    System.out.println("init"); 
    instance.init(); 
    assertNotNull(instance);
    
    verify(mockProps, times(1)).getPeliasPath();  
  }

  /**
   * Test of searchAddress method, of class GeorgLogic.
   */
  @Test
  public void testSearchAddress() {
    System.out.println("searchAddress");
    
    String address = "stockholm"; 
    String source = "GBIF";
    String layer = "myLayer";
  
    int size = 0;
    
    String result = instance.searchAddress(address, source, layer, countryCode, size);
    assertEquals(resultString, result);  
     
    verify(service, times(1)).getResults(any(String.class)); 
  }

  /**
   * Test of reverseSearch method, of class GeorgLogic.
   */
  @Test
  public void testReverseSearch() {
    System.out.println("reverseSearch");
    double lat = 59.6;
    double lon = 18.7;  
     
    String result = instance.reverseSearch(lat, lon);
    assertEquals(resultString, result); 
     
    verify(service, times(1)).getResults(any(String.class)); 
  }

  /**
   * Test of runAutocompleteSearch method, of class GeorgLogic.
   */
  @Test
  public void testRunAutocompleteSearch() {
    System.out.println("runAutocompleteSearch");
    String text = "val";
    String sources = "gbif";
    String layers = "mylayer";
    int size = 25;  
    String result = instance.runAutocompleteSearch(text, sources, layers, countryCode, size);
    assertEquals(resultString, result); 
    
    verify(service, times(1)).getResults(any(String.class)); 
  }

  /**
   * Test of coordinatesSearch method, of class GeorgLogic.
   */
  @Test
  public void testCoordinatesSearchDDWithComma() {
    System.out.println("coordinatesSearch");
    String coordinatesString = "61.67051, 5.32276";  
    String result = instance.coordinatesSearch(coordinatesString);
    System.out.println("result.." + result);
    assertEquals(resultString, result); 
  }
  
  @Test
  public void testCoordinatesSearchDD() {
    System.out.println("coordinatesSearch");
    String coordinatesString = "61.67051 5.32276";  
    String result = instance.coordinatesSearch(coordinatesString);
    System.out.println("result.." + result);
    assertEquals(resultString, result); 
  }
  
  @Test
  public void testCoordinatesSearchDMS() {
    System.out.println("coordinatesSearch");
    String coordinatesString = "61°40'14\" N 5°19'22\" E";  
    String result = instance.coordinatesSearch(coordinatesString);
    System.out.println("result.." + result);
    assertEquals(resultString, result); 
  }
  
  @Test
  public void testCoordinatesSearchDMSWithComma() {
    System.out.println("coordinatesSearch");
    String coordinatesString = "61°40'14\" N, 5°19'22\" E";  
    String result = instance.coordinatesSearch(coordinatesString);
    System.out.println("result.." + result);
    assertEquals(resultString, result); 
  }
  
  @Test
  public void testCoordinatesSearchDDM() {
    System.out.println("coordinatesSearch");
    String coordinatesString = "61°40.230600' N 5°19.365600' E";  
    String result = instance.coordinatesSearch(coordinatesString);
    System.out.println("result.." + result);
    assertEquals(resultString, result); 
  }
  
  @Test
  public void testCoordinatesSearchDDMWithComma() {
    System.out.println("coordinatesSearch");
    String coordinatesString = "61°40.230600', N 5°19.365600' E";  
    String result = instance.coordinatesSearch(coordinatesString);
    System.out.println("result.." + result);
    assertEquals(resultString, result); 
  }
}
