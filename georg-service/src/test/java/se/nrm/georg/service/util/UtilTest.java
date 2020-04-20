package se.nrm.georg.service.util;

import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author idali
 */
public class UtilTest {
  
  private Util instance;
  private String peliasPath;
  
  public UtilTest() {
  }
   
  @Before
  public void setUp() {
    peliasPath = "http://api.se";
    instance = Util.getInstance();
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
    String expResult = "http://api.sesearch?text=stockholm";
    String result = instance.buildGeoCodePath(peliasPath, address);  
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
    String expResult = "http://api.sereverse?point.lon=16.9&point.lat=59.3";
    String result = instance.buildReverseGeoCodePath(peliasPath, lat, lon); 
    assertEquals(expResult, result); 
  }
  
}
