package se.nrm.georg.service;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class CORSFilterTest {

  @Mock
  private ContainerRequestContext requestContext;
  @Mock
  private ContainerResponseContext responseContext;

  private MultivaluedMap map;

  private CORSFilter instance;

  public CORSFilterTest() {
  }

  @Before
  public void setUp() {
    map = new MultivaluedHashMap();
    when(responseContext.getHeaders()).thenReturn(map);
  }

  @After
  public void tearDown() {
    map = null;
    responseContext = null;
    instance = null;
  }

  /**
   * Test of filter method, of class CORSFilter.
   * @throws java.lang.Exception
   */
  @Test
  public void testFilter() throws Exception {
    System.out.println("filter");

    instance = new CORSFilter();
    instance.filter(requestContext, responseContext);
    assertNotNull(map);
    assertEquals(map.size(), 4);

  }

}
