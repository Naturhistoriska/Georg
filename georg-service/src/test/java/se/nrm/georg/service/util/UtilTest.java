package se.nrm.georg.service.util;

import org.junit.After; 
import org.junit.AfterClass;
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author idali
 */
public class UtilTest {
  
  private Util instance;
  private String peliasPath;
  private String countryCode; 
    
  public UtilTest() {
  }
 
  @Before
  public void setUp() {
    peliasPath = "http://api.se/";
    instance = Util.getInstance();
    countryCode = "SWE";
  }
  
  @After
  public void tearDown() {
    instance = null;
  }

  /**
   * Test of getInstance method, of class Util.
   */
  @Test
  public void testGetInstance() {
    System.out.println("getInstance"); 
    Util result = Util.getInstance();
    assertNotNull(result); 
  }

  /**
   * Test of buildGeoCodePath method, of class Util.
   */
  @Test
  public void testBuildGeoCodePath() {
    System.out.println("buildGeoCodePath");
     
    String address = "stockholm";  
    String source = "mySource";
    String layer = "myLayer"; 
    String expResult = "http://api.se/search?text=stockholm*&boundary.country=SWE&sources=mySource&layers=myLayer&size=5";
    String result = instance.buildGeoCodePath(peliasPath, address, source, layer, countryCode, 5);  
     
    assertEquals(expResult, result); 
  }
  
 /**
   * Test of buildGeoCodePath method, of class Util.
   */
  @Test
  public void testBuildGeoCodePathWithAddressOnly() {
    System.out.println("buildGeoCodePath");
     
    String address = "stockholm";  
    String source = null;
    String layer = "";
    String expResult = "http://api.se/search?text=stockholm*&boundary.country=SWE";
    String result = instance.buildGeoCodePath(peliasPath, address, source, layer, countryCode, 0);  
     
    assertEquals(expResult, result); 
  }
  
  /**
   * Test of buildGeoCodePath method, of class Util.
   */
  @Test
  public void testBuildGeoCodePathWithAddressOnly2() {
    System.out.println("buildGeoCodePath");
     
    String address = "stockholm";  
    String source = "";
    String layer = null;
    String expResult = "http://api.se/search?text=stockholm*&boundary.country=SWE";
    String result = instance.buildGeoCodePath(peliasPath, address, source, layer, countryCode, 0);  
     
    assertEquals(expResult, result); 
  }

  @Test
  public void testBuildGeoCodePathWithAddressAndSource() {
    System.out.println("buildGeoCodePath");
     
    String address = "stockholm";  
    String source = "mySource";
    String layer = null;
    String expResult = "http://api.se/search?text=stockholm*&boundary.country=SWE&sources=mySource";
    String result = instance.buildGeoCodePath(peliasPath, address, source, layer, countryCode, 0);  
     
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testBuildGeoCodePathWithAddressAndSource2() {
    System.out.println("buildGeoCodePath");
     
    String address = "stockholm";   
    String source = "mySource";
    String layer = "";
    String expResult = "http://api.se/search?text=stockholm*&boundary.country=SWE&sources=mySource";
    String result = instance.buildGeoCodePath(peliasPath, address, source, layer, countryCode, 0);  
     
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testBuildGeoCodePathWithAddressAndLayer() {
    System.out.println("buildGeoCodePath");
     
    String address = "stockholm";  
    String source = null;
    String layer = "myLayer";
    String expResult = "http://api.se/search?text=stockholm*&boundary.country=SWE&layers=myLayer";
    String result = instance.buildGeoCodePath(peliasPath, address, source, layer, countryCode, 0);  
    System.out.println("result..." + result);
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testBuildGeoCodePathWithAddressAndLayer2() {
    System.out.println("buildGeoCodePath");
     
    String address = "stockholm";  
    String source = "";
    String layer = "myLayer";
    String expResult = "http://api.se/search?text=stockholm*&boundary.country=SWE&layers=myLayer";
    String result = instance.buildGeoCodePath(peliasPath, address, source, layer, countryCode, 0);  
     
    assertEquals(expResult, result); 
  }

  /**
   * Test of buildReverseGeoCodePath method, of class Util.
   */
  @Test
  public void testBuildReverseGeoCodePath() {
    System.out.println("buildReverseGeoCodePath");
 
    double lat = 59.3;
    double lon = 16.9; 
    String expResult = "http://api.se/reverse?point.lon=16.9&point.lat=59.3&boundary.circle.radius=1000&layers=coarse";
    String result = instance.buildReverseGeoCodePath(peliasPath, lat, lon); 
    assertEquals(expResult, result); 
  }

  /**
   * Test of bunildAutoCompleteSearchPath method, of class Util.
   */
  @Test
  public void testBunildAutoCompleteSearchPath() {
    System.out.println("bunildAutoCompleteSearchPath");
 
    String text = "bromma";
    String sources = "";
    String layers = "";  
    
    String expResult = "http://api.se/autocomplete?text=bromma&boundary.country=SWE";
    String result = instance.bunildAutoCompleteSearchPath(peliasPath, text, sources, layers, countryCode, 0); 
    assertEquals(expResult, result); 
  }
}
