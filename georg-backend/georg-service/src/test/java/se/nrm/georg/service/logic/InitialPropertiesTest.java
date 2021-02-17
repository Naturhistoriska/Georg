package se.nrm.georg.service.logic;

import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author idali
 */
public class InitialPropertiesTest {
  
  private InitialProperties instance;
  
  public InitialPropertiesTest() {
  }
   
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
    instance = null;
  }
  
  @Test(expected = RuntimeException.class)
  public void testDefaultConstructor() {
    instance = new InitialProperties();
    assertNotNull(instance);
    instance.getPeliasPath();
  }

  /**
   * Test of getPeliasPath method, of class InitialProperties.
   */
  @Test
  public void testGetPeliasPath() {
    System.out.println("getPeliasPath");  
    String peliasPath = "http://test_api.se/api";
    instance = new InitialProperties(peliasPath);
    String result = instance.getPeliasPath();
    assertEquals(peliasPath, result); 
  }
  
}
