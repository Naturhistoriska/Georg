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
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import se.nrm.georg.service.logic.coordinates.CoordinatesTransformation;
import se.nrm.georg.service.logic.json.JsonParser;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class PeliasParserTest {
  
  private PeliasParser instance;
  
  private String jsonString;
  
  @Mock
  private JsonParser parser;
  @Mock
  private CoordinatesTransformation transformation;
  
  
  
  public PeliasParserTest() {
  }
 
  @Before
  public void setUp() {
    jsonString = "{\n"
            + "	\"features\": [{\n"
            + "		\"geometry\": {\n"
            + "			\"coordinates\": [\n"
            + "				16.2,\n"
            + "				62.8\n"
            + "			],\n"
            + "			\"type\": \"Point\"\n"
            + "		},\n"
            + "		\"type\": \"Feature\",\n"
            + "		\"properties\": {\n"
            + "			\"country\": \"Sweden\",\n"
            + "			\"gid\": \"whosonfirst:locality:1209608545\",\n"
            + "			\"locality_gid\": \"whosonfirst:locality:1209608545\"\n"
            + "		}\n"
            + "	}],\n"
            + "	\"geocoding\": {\n"
            + "		\"attribution\": \"http://api:4000/attribution\",\n"
            + "		\"version\": \"0.2\",\n"
            + "		\"timestamp\": 1599559812369\n"
            + "	},\n"
            + "	\"type\": \"FeatureCollection\"\n"
            + "}";
    
    JSONObject json = new JSONObject(jsonString);
    when(parser.convertStringToJson(any(String.class))).thenReturn(json);
    instance = new PeliasParser(parser, transformation);
  }
  
  @After
  public void tearDown() {
    instance = null;
  }
   
  @Test(expected = NullPointerException.class)
  public void testDefaultConstructor() {
    instance = new PeliasParser();
    assertNotNull(instance);
    instance.stringToJson(jsonString);
  }

  @Test
  public void testConstructorWithParams() {
    assertNotNull(instance);
    JSONObject json = instance.stringToJson(jsonString);
    assertNotNull(json);
  }
 

  /**
   * Test of stringToJson method, of class PeliasParser.
   */
//  @Test
  public void testStringToJson() {
    System.out.println("stringToJson");
    String jsonString = "";
    PeliasParser instance = new PeliasParser();
    JSONObject expResult = null;
    JSONObject result = instance.stringToJson(jsonString);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getFeatures method, of class PeliasParser.
   */
//  @Test
  public void testGetFeatures() {
    System.out.println("getFeatures");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    JSONArray expResult = null;
    JSONArray result = instance.getFeatures(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getFirstFeature method, of class PeliasParser.
   */
//  @Test
  public void testGetFirstFeature() {
    System.out.println("getFirstFeature");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    JSONObject expResult = null;
    JSONObject result = instance.getFirstFeature(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getGeometry method, of class PeliasParser.
   */
//  @Test
  public void testGetGeometry() {
    System.out.println("getGeometry");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    JSONObject expResult = null;
    JSONObject result = instance.getGeometry(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getProperties method, of class PeliasParser.
   */
//  @Test
  public void testGetProperties() {
    System.out.println("getProperties");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    JSONObject expResult = null;
    JSONObject result = instance.getProperties(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getLabel method, of class PeliasParser.
   */
//  @Test
  public void testGetLabel() {
    System.out.println("getLabel");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    String expResult = "";
    String result = instance.getLabel(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
 

  /**
   * Test of getBBox method, of class PeliasParser.
   */
//  @Test
  public void testGetBBox() {
    System.out.println("getBBox");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    JSONArray expResult = null;
    JSONArray result = instance.getBBox(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
 

  /**
   * Test of getAddendum method, of class PeliasParser.
   */
//  @Test
  public void testGetAddendum() {
    System.out.println("getAddendum");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    JSONObject expResult = null;
    JSONObject result = instance.getAddendum(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
 
  /**
   * Test of getGeorg method, of class PeliasParser.
   */
//  @Test
  public void testGetGeorg() {
    System.out.println("getGeorg");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    JSONObject expResult = null;
    JSONObject result = instance.getGeorg(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of hasUncertainty method, of class PeliasParser.
   */
//  @Test
  public void testHasUncertainty() {
    System.out.println("hasUncertainty");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    boolean expResult = false;
    boolean result = instance.hasUncertainty(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getUncertaintyInMeters method, of class PeliasParser.
   */
//  @Test
  public void testGetUncertaintyInMeters() {
    System.out.println("getUncertaintyInMeters");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    int expResult = 0;
    int result = instance.getUncertaintyInMeters(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getCoordinates method, of class PeliasParser.
   */
//  @Test
  public void testGetCoordinates() {
    System.out.println("getCoordinates");
    JSONObject json = null;
    PeliasParser instance = new PeliasParser();
    JSONArray expResult = null;
    JSONArray result = instance.getCoordinates(json);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addCoordinatesJson method, of class PeliasParser.
   */
//  @Test
  public void testAddCoordinatesJson() {
    System.out.println("addCoordinatesJson");
    JSONObject propertyJson = null;
    double lat = 0.0;
    double lng = 0.0;
    PeliasParser instance = new PeliasParser();
    instance.addCoordinatesJson(propertyJson, lat, lng);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addCoordinatesToGeometry method, of class PeliasParser.
   */
//  @Test
  public void testAddCoordinatesToGeometry() {
    System.out.println("addCoordinatesToGeometry");
    JSONObject geometry = null;
    double lat = 0.0;
    double lng = 0.0;
    PeliasParser instance = new PeliasParser();
    instance.addCoordinatesToGeometry(geometry, lat, lng);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addDinPlats method, of class PeliasParser.
   */
//  @Test
  public void testAddDinPlats() {
    System.out.println("addDinPlats");
    JSONObject json = null;
    double lat = 0.0;
    double lng = 0.0;
    PeliasParser instance = new PeliasParser();
    JSONObject expResult = null;
    JSONObject result = instance.addDinPlats(json, lat, lng);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
