package se.nrm.georg.service.logic.pelias;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class UncertaintyParserTest {

  private UncertaintyParser instance;

  @Mock
  private PeliasParser parser;

  public UncertaintyParserTest() {
  }

  @Before
  public void setUp() {
    instance = new UncertaintyParser();
  }

  @After
  public void tearDown() {
    instance = null;
  }

  /**
   * Test of getUncertainty method, of class UncertaintyParser.
   */
  @Test
  public void testGetUncertainty() {
    System.out.println("getUncertainty");
    JSONObject fearureJson = new JSONObject();
    JSONObject propertiesJson = new JSONObject();
    when(parser.getUncertaintyInMeters(any(JSONObject.class))).thenReturn(2000);

    int expResult = 2000;
    int result = instance.getUncertainty(fearureJson, propertiesJson, parser);
    assertEquals(expResult, result);
    verify(parser, times(1)).getUncertaintyInMeters(any(JSONObject.class));
    verify(parser, never()).getBBox(any(JSONObject.class));
  }

  /**
   * Test of getUncertainty method, of class UncertaintyParser.
   */
  @Test
  public void testGetUncertaintyWithBBox() {
    System.out.println("getUncertainty");
    JSONObject fearureJson = new JSONObject();
    JSONObject propertiesJson = new JSONObject();

    String bbox = "[\n"
            + "	16.188956,\n"
            + "	58.901872,\n"
            + "	16.302235,\n"
            + "	58.947086\n"
            + "]";
    JSONArray bboxArray = new JSONArray(bbox);
    when(parser.getUncertaintyInMeters(any(JSONObject.class))).thenReturn(null);
    when(parser.getBBox(any(JSONObject.class))).thenReturn(bboxArray);
 
    int result = instance.getUncertainty(fearureJson, propertiesJson, parser); 
    assertEquals(31, result);
    verify(parser, times(1)).getUncertaintyInMeters(any(JSONObject.class));
    verify(parser, times(1)).getBBox(any(JSONObject.class));
  }
  
  @Test
  public void testGetUncertaintyWithNull() {
    System.out.println("getUncertainty");
    JSONObject fearureJson = new JSONObject();
    JSONObject propertiesJson = new JSONObject();
 
    when(parser.getUncertaintyInMeters(any(JSONObject.class))).thenReturn(null);
    when(parser.getBBox(any(JSONObject.class))).thenReturn(null);
 
    int result = instance.getUncertainty(fearureJson, propertiesJson, parser); 
    assertEquals(10000, result);
    verify(parser, times(1)).getUncertaintyInMeters(any(JSONObject.class));
    verify(parser, times(1)).getBBox(any(JSONObject.class));
  }

}
