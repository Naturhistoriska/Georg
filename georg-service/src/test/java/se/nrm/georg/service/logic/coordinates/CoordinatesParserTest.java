package se.nrm.georg.service.logic.coordinates;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import se.nrm.georg.service.logic.pelias.PeliasParser;
import se.nrm.georg.service.util.Util;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class CoordinatesParserTest {

  @Mock
  private PeliasParser parser;

  private CoordinatesParser instance;
  private String jsonString;

  private JSONObject resultJson;

  public CoordinatesParserTest() {
  }

  @Before
  public void setUp() {
    jsonString = "{\n"
            + "	\"features\": [{\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.95093,\n"
            + "					59.42804\n"
            + "				],\n"
            + "				\"type\": \"Point\"\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"label\": \"Sollentuna, Sweden\",\n"
            + "				\"id\": \"1125772423\"\n"
            + "			}\n"
            + "		},\n"
            + "		{\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.94845,\n"
            + "					59.428389\n"
            + "				]\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"id\": \"node/35690049\",\n"
            + "				\"label\": \"Sollentuna, Stockholm, Sweden\"\n"
            + "			}\n"
            + "		}\n"
            + "	]\n"
            + "}";

    String finalResult = "{\n"
            + "	\"features\": [{\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.95093,\n"
            + "					59.42804\n"
            + "				],\n"
            + "				\"type\": \"Point\"\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"label\": \"Sollentuna, Sweden\",\n"
            + "				\"id\": \"1125772423\",\n"
            + "				\"coordinates\": {\n"
            + "					\"dd\": [\n"
            + "						\"59.428040\",\n"
            + "						\"17.950930\"\n"
            + "					],\n"
            + "					\"dms\": [\n"
            + "						\"59°25'40.9\\\" N\",\n"
            + "						\"17°57'03.3\\\" E\"\n"
            + "					],\n"
            + "					\"ddm\": [\n"
            + "						\"59°25.682' N\",\n"
            + "						\"17°57.056' E\"\n"
            + "					],\n"
            + "					\"rt90\": [\n"
            + "						\"6591683\",\n"
            + "						\"1621782\"\n"
            + "					],\n"
            + "					\"sweref99\": [\n"
            + "						\"6591429\",\n"
            + "						\"667393\"\n"
            + "					]\n"
            + "				}\n"
            + "			}\n"
            + "		},\n"
            + "		{\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.94845,\n"
            + "					59.428389\n"
            + "				]\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"id\": \"node/35690049\",\n"
            + "				\"label\": \"Sollentuna, Stockholm, Sweden\",\n"
            + "				\"coordinates\": {\n"
            + "					\"dd\": [\n"
            + "						\"59.428389\",\n"
            + "						\"17.948450\"\n"
            + "					],\n"
            + "					\"dms\": [\n"
            + "						\"59°25'42.2\\\" N\",\n"
            + "						\"17°56'54.4\\\" E\"\n"
            + "					],\n"
            + "					\"ddm\": [\n"
            + "						\"59°25.703' N\",\n"
            + "						\"17°56.907' E\"\n"
            + "					],\n"
            + "					\"rt90\": [\n"
            + "						\"6591717\",\n"
            + "						\"1621640\"\n"
            + "					],\n"
            + "					\"sweref99\": [\n"
            + "						\"6591461\",\n"
            + "						\"667251\"\n"
            + "					]\n"
            + "				}\n"
            + "			}\n"
            + "		}\n"
            + "	]\n"
            + "}";

    JSONObject json = new JSONObject(jsonString);
    JSONArray features = json.getJSONArray("features");
    JSONObject feature = features.getJSONObject(0);
    JSONObject propertiesJson = feature.getJSONObject("properties");
    JSONArray coordinates = feature.getJSONObject("geometry").getJSONArray("coordinates"); 
    
    when(parser.stringToJson(any(String.class))).thenReturn(json);
    when(parser.getFeatures(any(JSONObject.class))).thenReturn(features);
    when(parser.getCoordinates(any(JSONObject.class))).thenReturn(coordinates);
    when(parser.getProperties(any(JSONObject.class))).thenReturn(propertiesJson);
    
            
    doNothing().when(parser).addCoordinatesJson(propertiesJson,
            Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(1)),  
            Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(0)));
    instance = new CoordinatesParser(parser);
  }

  @After
  public void tearDown() {
    instance = null;
  }

  @Test(expected = NullPointerException.class)
  public void testDefaultConstructor() {
    System.out.println("testDefaultConstructor");
    instance = new CoordinatesParser();
    assertNotNull(instance);
    instance.addCoordinatesTransformation(jsonString);
  }

  @Test
  public void testConstructorWithParams() {
    System.out.println("testConstructorWithParams");
    assertNotNull(instance); 
    instance.addCoordinatesTransformation(jsonString);
    assertNotNull(instance);
  }

  /**
   * Test of addCoordinatesTransformation method, of class CoordinatesParser.
   */
  @Test
  public void testAddCoordinatesTransformation_String() {
    System.out.println("addCoordinatesTransformation");

    String expResult = "";
    String result = instance.addCoordinatesTransformation(jsonString);
    System.out.println("results ....: " + result);
//    assertEquals(expResult, re.sult);
  }

  /**
   * Test of addCoordinatesTransformationNewPlace method, of class
   * CoordinatesParser.
   */
//  @Test
  public void testAddCoordinatesTransformationNewPlace() {
    System.out.println("addCoordinatesTransformationNewPlace");
    String jsonString = "";
    Double lat = null;
    Double lng = null;
    CoordinatesParser instance = new CoordinatesParser();
    String expResult = "";
    String result = instance.addCoordinatesTransformationNewPlace(jsonString, lat, lng);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addCoordinatesTransformation method, of class CoordinatesParser.
   */
//  @Test
  public void testAddCoordinatesTransformation_3args() {
    System.out.println("addCoordinatesTransformation");
    String jsonString = "";
    Double lat = null;
    Double lng = null;
    CoordinatesParser instance = new CoordinatesParser();
    String expResult = "";
    String result = instance.addCoordinatesTransformation(jsonString, lat, lng);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
