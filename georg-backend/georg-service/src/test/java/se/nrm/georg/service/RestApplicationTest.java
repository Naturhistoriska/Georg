package se.nrm.georg.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author idali
 */
public class RestApplicationTest {
  
  RestApplication instance;
  
  public RestApplicationTest() {
  }
  
  
  @Before
  public void setUp() {
    instance = new RestApplication();
  }
  
  @After
  public void tearDown() {
    instance = null;
  }

  @Test
  public void testSomeMethod() { 
    assertNotNull(instance);
  }
  
}
