package se.nrm.georg.service.logic.coordinates.validation;
 
import org.junit.After;  
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*; 

/**
 *
 * @author idali
 */
public class CoordinatesValidationTest {
  
  private CoordinatesValidation instance;
  
  public CoordinatesValidationTest() {
  }
 
  @Before
  public void setUp() {
    instance = CoordinatesValidation.getInstance();
  }
  
  @After
  public void tearDown() {
    instance = null;
  }

  /**
   * Test of getInstance method, of class CoordinationValidation.
   */
  @Test
  public void testGetInstance() {
    System.out.println("getInstance"); 
    CoordinatesValidation result = CoordinatesValidation.getInstance();
    assertNotNull(result); 
  }

  /**
   * Test of isValidLatitude method, of class CoordinationValidation.
   */
  @Test
  public void testIsValidLatitude() {
    System.out.println("isValidLatitude");
    double latitude = 25.6767; 
    boolean expResult = true;
    boolean result = instance.isValidLatitude(latitude);
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testIsValidLatitudeMinus90() {
    System.out.println("testIsValidLatitudeMinus90");
    double latitude = -90.0000; 
    boolean expResult = true;
    boolean result = instance.isValidLatitude(latitude);
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testIsValidLatitude90() {
    System.out.println("testIsValidLatitude90");
    double latitude = 90.0000; 
    boolean expResult = true;
    boolean result = instance.isValidLatitude(latitude);
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testIsValidLatitudeMinus90Error() {
    System.out.println("testIsValidLatitudeMinus90Error");
    double latitude = -90.89000; 
    boolean expResult = false;
    boolean result = instance.isValidLatitude(latitude);
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testIsValidLatitude90Error() {
    System.out.println("testIsValidLatitude90Error");
    double latitude = 90.98000; 
    boolean expResult = false;
    boolean result = instance.isValidLatitude(latitude);
    assertEquals(expResult, result); 
  }

  /**
   * Test of isValidLongitude method, of class CoordinationValidation.
   */
  @Test
  public void testIsValidLongitude() {
    System.out.println("isValidLongitude");
    double longitude = 16.89;
    boolean expResult = true;
    boolean result = instance.isValidLongitude(longitude);
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testIsValidLongitudeMinus180() {
    System.out.println("isValidLongitude");
    double longitude = -180.00;
    boolean expResult = true;
    boolean result = instance.isValidLongitude(longitude);
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testIsValidLongitude180() {
    System.out.println("isValidLongitude");
    double longitude = 180.00;
    boolean expResult = true;
    boolean result = instance.isValidLongitude(longitude);
    assertEquals(expResult, result); 
  }
  
   @Test
  public void testIsValidLongitudeMinus180Error() {
    System.out.println("testIsValidLongitudeMinus180Error");
    double longitude = -180.90;
    boolean expResult = false;
    boolean result = instance.isValidLongitude(longitude);
    assertEquals(expResult, result); 
  }
  
  @Test
  public void testIsValidLongitude180Error() {
    System.out.println("testIsValidLongitude180Error");
    double longitude = 180.89;
    boolean expResult = false;
    boolean result = instance.isValidLongitude(longitude);
    assertEquals(expResult, result); 
  }
}
