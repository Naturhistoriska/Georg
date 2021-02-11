package se.nrm.georg.service.logic.coordinates;

import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import org.locationtech.proj4j.ProjCoordinate;

/**
 *
 * @author idali
 */
public class CoordinatesTransformationTest {
  
  private final double lat = 59.33;
  private final double lng = 15.33;
  
  private CoordinatesTransformation instance;
  
  public CoordinatesTransformationTest() {
  }
   
  @Before
  public void setUp() {
    instance = new CoordinatesTransformation();
  }
  
  @After
  public void tearDown() {
    instance = null;
  }

  /**
   * Test of transformRT90 method, of class CoordinatesTransformation.
   */
  @Test
  public void testTransformRT90() {
    System.out.println("transformRT90");
  
    double expResultx = 1472952.091363791;
    double expResulty = 6578896.503822367;
    
    ProjCoordinate result = instance.transformRT90(lat, lng); 
    assertEquals(expResultx, result.x, result.x);
    assertEquals(expResulty, result.y, result.y);
  }

  /**
   * Test of transformSWEREF99 method, of class CoordinatesTransformation.
   */
  @Test
  public void testTransformSWEREF99() {
    System.out.println("transformSWEREF99");
     
    instance = new CoordinatesTransformation(); 
    
    double expResultx = 518777.48393226886;
    double expResulty = 6576845.157366491;
    ProjCoordinate result = instance.transformSWEREF99(lat, lng);
 
    assertEquals(expResultx, result.x, result.x); 
    assertEquals(expResulty, result.y, result.y); 
  }
  
}
