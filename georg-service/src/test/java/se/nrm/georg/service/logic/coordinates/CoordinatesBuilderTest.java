package se.nrm.georg.service.logic.coordinates;

import java.math.BigDecimal;
import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import org.locationtech.proj4j.ProjCoordinate;

/**
 *
 * @author idali
 */
public class CoordinatesBuilderTest {
  
  private CoordinatesBuilder instance;
  
  public CoordinatesBuilderTest() {
  }
 
  @Before
  public void setUp() {
    instance = new CoordinatesBuilder();
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of buildRT90 method, of class CoordinatesBuilder.
   */
  @Test
  public void testBuildRT90() {
    System.out.println("buildRT90");
    
//    BigDecimal lat = new BigDecimal(59.33);
//    BigDecimal lng = new BigDecimal(15.33);
    
    double lat = 59.33;
    double lng = 15.33;
    
    instance = new CoordinatesBuilder();
    double expResultx = 1472952.091363791;
    double expResulty = 6578896.503822367;
    ProjCoordinate result = instance.buildRT90(lat, lng);
    
    assertEquals(expResultx, result.x, result.x);
    assertEquals(expResulty, result.y, result.y);
  }
 
  /**
   * Test of buildSWEREF99 method, of class CoordinatesBuilder.
   */
  @Test
  public void testBuildSWEREF99() {
    System.out.println("buildSWEREF99");

//    BigDecimal lat = new BigDecimal(59.33);
//    BigDecimal lng = new BigDecimal(15.33);
    double lat = 59.33;
    double lng = 15.33;
    instance = new CoordinatesBuilder(); 
    
    double expResultx = 518777.48393226886;
    double expResulty = 6576845.157366491;
    ProjCoordinate result = instance.buildSWEREF99(lat, lng);
 
    assertEquals(expResultx, result.x, result.x); 
    assertEquals(expResulty, result.y, result.y); 
  }

  /**
   * Test of buildDD method, of class CoordinatesBuilder.
   */
  @Test
  public void testBuildDD() {
    System.out.println("buildDD");
    
    BigDecimal lat = new BigDecimal(59.5);
    BigDecimal lng = new BigDecimal(15.5);
    
    instance = new CoordinatesBuilder();
    String expResult = "59.5 15.5";
    String result = instance.buildDD(lat, lng); 
    assertEquals(expResult, result); 
  }
  
}
