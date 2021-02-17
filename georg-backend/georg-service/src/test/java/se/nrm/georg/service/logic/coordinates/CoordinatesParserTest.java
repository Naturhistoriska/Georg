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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
  private JSONObject json;
  private JSONArray features;
  private JSONObject feature;
  private JSONObject propertiesJson;
  private JSONObject geometryJson;
  private JSONArray coordinates;
  private double lat;
  private double lng;

  private JSONObject dinPlatsJson;
  private String finalResult;

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
    String dinPlatesString = "{\n"
            + "	\"geometry\": {\n"
            + "		\"coordinates\": [\n"
            + "			17.95093,\n"
            + "			59.42804\n"
            + "		],\n"
            + "		\"type\": \"Point\"\n"
            + "	},\n"
            + "	\"type\": \"Feature\",\n"
            + "	\"properties\": {\n"
            + "		\"country\": \"Sweden\",\n"
            + "		\"gid\": \"newMarker\",\n"
            + "		\"name\": \"Din plats\",\n"
            + "		\"coordinates\": {\n"
            + "			\"dd\": [\n"
            + "				\"59.428389\",\n"
            + "				\"17.948450\"\n"
            + "			],\n"
            + "			\"dms\": [\n"
            + "				\"59°25'42.2\\\" N\",\n"
            + "				\"17°56'54.4\\\" E\"\n"
            + "			],	\n"
            + "			\"ddm\": [\n"
            + "				\"59°25.703' N\",\n"
            + "				\"17°56.907' E\"\n"
            + "			],\n"
            + "			\"rt90\": [\n"
            + "				\"6591717\",\n"
            + "				\"1621640\"\n"
            + "			],\n"
            + "			\"sweref99\": [\n"
            + "				\"6591461\",\n"
            + "				\"667251\"\n"
            + "			]\n"
            + "		},\n"
            + "		\"county\": \"Sollentuna\",\n"
            + "		\"id\": \"newMarker\",\n"
            + "		\"region\": \"Stockholm\"\n"
            + "	}\n"
            + "}";

    finalResult = "{\n"
            + "	\"features\": [{\n"
            + "		\"geometry\": {\n"
            + "			\"coordinates\": [17.95093, 59.42804],\n"
            + "			\"type\": \"Point\"\n"
            + "		},\n"
            + "		\"properties\": {\n"
            + "			\"label\": \"Sollentuna, Sweden\",\n"
            + "			\"id\": \"1125772423\"\n"
            + "		}\n"
            + "	}, {\n"
            + "		\"geometry\": {\n"
            + "			\"coordinates\": [17.94845, 59.428389]\n"
            + "		},\n"
            + "		\"properties\": {\n"
            + "			\"id\": \"node/35690049\",\n"
            + "			\"label\": \"Sollentuna, Stockholm, Sweden\"\n"
            + "		}\n"
            + "	}, {\n"
            + "		\"geometry\": {\n"
            + "			\"coordinates\": [17.95093, 59.42804],\n"
            + "			\"type\": \"Point\"\n"
            + "		},\n"
            + "		\"type\": \"Feature\",\n"
            + "		\"properties\": {\n"
            + "			\"country\": \"Sweden\",\n"
            + "			\"gid\": \"newMarker\",\n"
            + "			\"name\": \"Din plats\",\n"
            + "			\"coordinates\": {\n"
            + "				\"dd\": [\"59.428389\", \"17.948450\"],\n"
            + "				\"dms\": [\"59°25'42.2\\\" N\", \"17°56'54.4\\\" E\"],\n"
            + "				\"ddm\": [\"59°25.703' N\", \"17°56.907' E\"],\n"
            + "				\"rt90\": [\"6591717\", \"1621640\"],\n"
            + "				\"sweref99\": [\"6591461\", \"667251\"]\n"
            + "			},\n"
            + "			\"county\": \"Sollentuna\",\n"
            + "			\"id\": \"newMarker\",\n"
            + "			\"region\": \"Stockholm\"\n"
            + "		}\n"
            + "	}]\n"
            + "}";
    json = new JSONObject(jsonString);
    features = json.getJSONArray("features");
    feature = features.getJSONObject(0);
    propertiesJson = feature.getJSONObject("properties");
    geometryJson = feature.getJSONObject("geometry");
    coordinates = feature.getJSONObject("geometry").getJSONArray("coordinates");
    lat = Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(1));
    lng = Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(0));

    dinPlatsJson = new JSONObject(dinPlatesString);

    when(parser.stringToJson(jsonString)).thenReturn(json);
    when(parser.getFeatures(json)).thenReturn(features);
    when(parser.getCoordinates(any(JSONObject.class))).thenReturn(coordinates);
    when(parser.getProperties(any(JSONObject.class))).thenReturn(propertiesJson);
    when(parser.getGeometry(any(JSONObject.class))).thenReturn(geometryJson);

    when(parser.addDinPlats(propertiesJson, lat, lng)).thenReturn(dinPlatsJson);

    doNothing().when(parser).addCoordinatesJson(propertiesJson, lat, lng);
    doNothing().when(parser).addCoordinatesToGeometry(geometryJson, lat, lng);
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

    instance.addCoordinatesTransformation(jsonString);
    verify(parser, times(1)).stringToJson(jsonString);
    verify(parser, times(1)).getFeatures(json);
    verify(parser, times(2)).getCoordinates(any(JSONObject.class));
    verify(parser, times(2)).addCoordinatesJson(propertiesJson, lat, lng);
  }

  /**
   * Test of addCoordinatesTransformationNewPlace method, of class
   * CoordinatesParser.
   */
  @Test
  public void testAddCoordinatesTransformationNewPlace() {
    System.out.println("addCoordinatesTransformationNewPlace");

    String result = instance.addCoordinatesTransformationNewPlace(jsonString, lat, lng);
    JSONObject resultJson = new JSONObject(result);
    JSONArray resultFeatures = resultJson.getJSONArray("features");
    JSONObject resultDinPlats = resultFeatures.getJSONObject(2);

    assertEquals(3, resultFeatures.length());
    assertEquals(resultDinPlats.getJSONObject("properties").getString("gid"), "newMarker");
    assertEquals(resultDinPlats.getJSONObject("properties").getString("name"), "Din plats");
    verify(parser, times(1)).stringToJson(jsonString);
    verify(parser, times(1)).getFeatures(json);
    verify(parser, times(1)).addDinPlats(propertiesJson, lat, lng);
  }

  /**
   * Test of addCoordinatesTransformationNewPlace method, of class
   * CoordinatesParser.
   */
  @Test
  public void testAddCoordinatesTransformationNewPlaceWithoutResult() {
    System.out.println("addCoordinatesTransformationNewPlace");

    String jsonStr = "{\n"
            + "	\"features\": [],\n"
            + "	\"type\": \"FeatureCollection\"\n"
            + "}";
    JSONObject jsonObj = new JSONObject(jsonStr);
    JSONArray featuresArray = jsonObj.getJSONArray("features");

    when(parser.stringToJson(any(String.class))).thenReturn(jsonObj);
    when(parser.getFeatures(any(JSONObject.class))).thenReturn(featuresArray);
    when(parser.addDinPlats(null, lat, lng)).thenReturn(dinPlatsJson);

    String result = instance.addCoordinatesTransformationNewPlace(jsonStr, lat, lng);
    JSONObject resultJson = new JSONObject(result);

    JSONArray resultFeatures = resultJson.getJSONArray("features");
    JSONObject resultDinPlats = resultFeatures.getJSONObject(0);
    assertEquals(1, resultFeatures.length());
    assertEquals(resultDinPlats.getJSONObject("properties").getString("gid"), "newMarker");
    assertEquals(resultDinPlats.getJSONObject("properties").getString("name"), "Din plats");
    verify(parser, times(1)).stringToJson(jsonStr);
    verify(parser, times(1)).getFeatures(jsonObj);
    verify(parser, times(1)).addDinPlats(null, lat, lng);
  }

  /**
   * Test of addCoordinatesTransformation method, of class CoordinatesParser.
   */
  @Test
  public void testAddCoordinatesTransformation_3args() {
    System.out.println("addCoordinatesTransformation");

    String result = instance.addCoordinatesTransformation(jsonString, lat, lng); 

    JSONObject resultJson = new JSONObject(result);
    JSONArray resultFeatures = resultJson.getJSONArray("features");
    JSONObject resultDinPlats = resultFeatures.getJSONObject(2);

    assertEquals(3, resultFeatures.length());
    assertEquals(resultDinPlats.getJSONObject("properties").getString("gid"), "newMarker");
    assertEquals(resultDinPlats.getJSONObject("properties").getString("name"), "Din plats");
    verify(parser, times(1)).stringToJson(jsonString);
    verify(parser, times(1)).getFeatures(json);
    verify(parser, times(2)).getProperties(feature);
    verify(parser, times(1)).addDinPlats(propertiesJson, lat, lng);
    verify(parser, times(2)).addCoordinatesJson(propertiesJson, lat, lng);
    verify(parser, times(2)).addCoordinatesToGeometry(geometryJson, lat, lng);
    verify(parser, times(1)).addDinPlats(propertiesJson, lat, lng);
  }
  
  /**
   * Test of addCoordinatesTransformation method, of class CoordinatesParser.
   */
  @Test
  public void testAddCoordinatesTransformationNoResult() {
    System.out.println("addCoordinatesTransformation");
    
    
    String jsonStr = "{\n"
            + "	\"features\": [],\n"
            + "	\"type\": \"FeatureCollection\"\n"
            + "}";
    JSONObject jsonObj = new JSONObject(jsonStr);
    JSONArray featuresArray = jsonObj.getJSONArray("features");
    
    when(parser.stringToJson(any(String.class))).thenReturn(jsonObj);
    when(parser.getFeatures(any(JSONObject.class))).thenReturn(featuresArray);
    when(parser.addDinPlats(null, lat, lng)).thenReturn(dinPlatsJson);

    String result = instance.addCoordinatesTransformation(jsonStr, lat, lng); 

    JSONObject resultJson = new JSONObject(result);
    JSONArray resultFeatures = resultJson.getJSONArray("features");
    JSONObject resultDinPlats = resultFeatures.getJSONObject(0);

    assertEquals(1, resultFeatures.length());
    assertEquals(resultDinPlats.getJSONObject("properties").getString("gid"), "newMarker");
    assertEquals(resultDinPlats.getJSONObject("properties").getString("name"), "Din plats");
    verify(parser, times(1)).stringToJson(jsonStr);
    verify(parser, times(1)).getFeatures(jsonObj);
    verify(parser, times(1)).addDinPlats(null, lat, lng);
    verify(parser, never()).addCoordinatesJson(propertiesJson, lat, lng);
    verify(parser, never()).addCoordinatesToGeometry(geometryJson, lat, lng); 
  }

}
