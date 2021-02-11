package se.nrm.georg.service.logic.coordinates;

import org.junit.After;  
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*; 

/**
 *
 * @author idali
 */
public class CoordinatesHelperTest {
  
  private CoordinatesHelper instance;
  private final double lat = 57.3602;
  private final double lng = 15.3602;
  private final String dms = "57°21'52\" N 15°24'13\" E";
  private final String dd = "57.3602 15.3602";
  private final String ddm = "57°21.867300' N 15°24.217800' E";
  
  private final String dmsSouthEast = "57°21'52\" S 15°24'13\" E";
  private final String dmsSouthWest = "57°21'52\" S 15°24'13\" W"; 
  private final String dmsNorthWest = "57°21'52\" N 15°24'13\" W";
  
  public CoordinatesHelperTest() {
  }
  
  @Before
  public void setUp() {
    instance = CoordinatesHelper.getInstance();
  }
  
  @After
  public void tearDown() {
    instance = null;
  }

  /**
   * Test of getInstance method, of class CoordinatesHelper.
   */
  @Test
  public void testGetInstance() {
    System.out.println("getInstance");  
    assertNotNull(instance); 
  }
 
  /**
   * Test of buildDMS method, of class CoordinatesHelper.
   */
  @Test
  public void testBuildDMS_double_double() {
    System.out.println("buildDMS");
   
    String expResult = "57°21'36.7\" N 15°21'36.7\" E";
    String result = instance.buildDMS(lat, lng);
    assertEquals(expResult, result); 
  }
    
  /**
   * Test of buildDMS method, of class CoordinatesHelper.
   */
  @Test
  public void testBuildLatDMS() {
    System.out.println("buildDMS"); 
    String expResult = "57°21'36.7\" N";
    String result = instance.buildDMS(lat, true); 
    assertEquals(expResult, result); 
  }

  @Test
  public void testBuildLngDMS() {
    System.out.println("buildDMS"); 
    String expResult = "15°21'36.7\" E";
    String result = instance.buildDMS(lng, false); 
    assertEquals(expResult, result); 
  }
  
  /**
   * Test of buildDDM method, of class CoordinatesHelper.
   */
  @Test
  public void testBuildLatDDM() {
    System.out.println("buildDDM"); 
    String expResult = "57°21.612' N";
    String result = instance.buildDDM(lat, true); 
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testBuildLngDDM() {
    System.out.println("buildDDM"); 
    String expResult = "15°21.612' E";
    String result = instance.buildDDM(lng, false); 
    assertEquals(expResult, result); 
  }

  /**
   * Test of isDD method, of class CoordinatesHelper.
   */
  @Test
  public void testIsDDTrue() {
    System.out.println("isDD"); 
    boolean result = instance.isDD(dd);
    assertTrue(result); 
  }

  @Test
  public void testIsDDFalse() {
    System.out.println("isDD"); 
    boolean result = instance.isDD(dms);
    assertFalse(result); 
  }
  
  
  /**
   * Test of isDMS method, of class CoordinatesHelper.
   */
  @Test
  public void testIsDMSTrue() {
    System.out.println("isDMS");  
    boolean result = instance.isDMS(dms);
    assertTrue(result); 
  }
  
  @Test
  public void testIsDMSFalse() {
    System.out.println("isDMS");  
    boolean result = instance.isDMS(dd);
    assertFalse(result); 
  }

  /**
   * Test of isDDM method, of class CoordinatesHelper.
   */
  @Test
  public void testIsDDMTrue() {
    System.out.println("isDDM");   
    boolean result = instance.isDDM(ddm);
    assertTrue(result); 
  }
  
  @Test
  public void testIsDDMFalse() {
    System.out.println("isDDM");   
    boolean result = instance.isDDM(dd);
    assertFalse(result); 
  }

  /**
   * Test of getLatString method, of class CoordinatesHelper.
   */
  @Test
  public void testGetLatStringNorth() {
    System.out.println("getLatString"); 
    String expResult = "57°21'52\"";
    String result = instance.getLatString(dms.toLowerCase());
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testGetLatStringSourch() {
    System.out.println("getLatString"); 
    String expResult = "57°21'52\"";
    String result = instance.getLatString(dmsSouthEast.toLowerCase());
    assertEquals(expResult, result); 
  }


  /**
   * Test of getlngString method, of class CoordinatesHelper.
   */
  @Test
  public void testGetlngStringNorthEast() {
    System.out.println("getlngString"); 
    String expResult = "15°24'13\"";
    String result = instance.getlngString(dms.toLowerCase());
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testGetlngStringNorthWest() {
    System.out.println("getlngString"); 
    String expResult = "15°24'13\"";
    String result = instance.getlngString(dmsNorthWest.toLowerCase());
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testGetlngStringSouthWest() {
    System.out.println("getlngString"); 
    String expResult = "15°24'13\"";
    String result = instance.getlngString(dmsSouthWest.toLowerCase());
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testGetlngStringNSouthEast() {
    System.out.println("getlngString"); 
    String expResult = "15°24'13\"";
    String result = instance.getlngString(dmsSouthEast.toLowerCase());
    assertEquals(expResult, result); 
  }

  /**
   * Test of getLatAndLng method, of class CoordinatesHelper.
   */
  @Test
  public void testGetLatAndLngDD() {
    System.out.println("getLatAndLng");  
    Double[] result = instance.getLatAndLng(dd);
    
    assertEquals(lat, result[0], lat); 
    assertEquals(lng, result[1], lng); 
  }
  
  
  @Test
  public void testGetLatAndLngDMS() {
    System.out.println("getLatAndLng");  
    Double[] result = instance.getLatAndLng(dms.toLowerCase());
    System.out.println("result..." + result[0] +"..."  + result[1]);
    assertEquals(lat, result[0], lat); 
    assertEquals(lng, result[1], lng); 
  }
  
  @Test
  public void testGetLatAndLngDMSNorthWest() {
    System.out.println("getLatAndLng");  
    Double[] result = instance.getLatAndLng(dmsNorthWest.toLowerCase()); 
    double expLng = lng * (-1);
    assertEquals(lat, result[0], lat); 
    assertEquals(expLng, result[1], lng); 
  }
  
  @Test
  public void testGetLatAndLngDMSSouthWest() {
    System.out.println("getLatAndLng");  
    Double[] result = instance.getLatAndLng(dmsSouthWest.toLowerCase()); 
    double expLng = lng * (-1);
    double expLat = lat * (-1);
    assertEquals(expLat, result[0], lat); 
    assertEquals(expLng, result[1], lng); 
  }
  
  @Test
  public void testGetLatAndLngDMSSouthEast() {
    System.out.println("getLatAndLng");  
    Double[] result = instance.getLatAndLng(dmsSouthEast.toLowerCase()); 
 
    double expLat = lat * (-1);
    assertEquals(expLat, result[0], lat); 
    assertEquals(lng, result[1], lng); 
  } 
}
