package se.nrm.georg.service.logic.pelias;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.locationtech.proj4j.ProjCoordinate;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
  private JSONObject json;
  private JSONArray featuresArray;
  private JSONObject feature;
  private JSONObject feature2;
  private JSONObject feature3;
  private JSONObject feature4;
  private JSONObject geometry;
  private JSONObject properties;
  private JSONObject properties2;
  private JSONObject properties3;
  private JSONObject properties4;
  private JSONObject georg1;
  private JSONObject georg2;

  private final double rt90x = 1620115;
  private final double rt90y = 6582930;
  private final double sweRefx = 665833;
  private final double sweRefy = 6582658;

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
            + "			\"bbox\": [\n"
            + "				17.922861,\n"
            + "				59.333256,\n"
            + "				17.924899,\n"
            + "				59.335798\n"
            + "			],\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.9167,\n"
            + "					59.35\n"
            + "				]\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"label\": \"Bromma, Stockholm, Sweden\",\n"
            + "				\"addendum\": {\n"
            + "					\"georg\": {\n"
            + "						\"coordinateUncertaintyInMeters\": null\n"
            + "					}\n"
            + "				}\n"
            + "			}\n"
            + "		},\n"
            + "		{\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.90154,\n"
            + "					59.34439\n"
            + "				]\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"label\": \"Ängby, Bromma, Stockholm, Sweden\",\n"
            + "				\"addendum\": {\n"
            + "					\"georg\": {\n"
            + "						\"coordinateUncertaintyInMeters\": 2000\n"
            + "					}\n"
            + "				}\n"
            + "			}\n"
            + "		},\n"
            + "		{\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.88883,\n"
            + "					59.34495\n"
            + "				]\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"label\": \"Drachmannsgatan 2, Bromma, Stockholm, Sweden\",\n"
            + "				\"addendum\": {\n"
            + "					\"gbif\": {\n"
            + "						\"continent\": \"Europe\"\n"
            + "					}\n"
            + "				}\n"
            + "			}\n"
            + "		},\n"
            + "		{\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.912401,\n"
            + "					59.347716\n"
            + "				]\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"label\": \"Norra Ängby, Bromma, Stockholm [RT90:6582668/1619879], Stockholm, Sweden\"\n"
            + "			}\n"
            + "		}\n"
            + "	],\n"
            + "	\"type\": \"FeatureCollection\"\n"
            + "}";

    json = new JSONObject(jsonString);
    featuresArray = json.getJSONArray("features");
    feature = featuresArray.getJSONObject(0);
    feature2 = featuresArray.getJSONObject(1);
    feature3 = featuresArray.getJSONObject(2);
    feature4 = featuresArray.getJSONObject(3);
    geometry = feature.getJSONObject("geometry");
    properties = feature.getJSONObject("properties");
    properties2 = feature2.getJSONObject("properties");
    properties3 = feature3.getJSONObject("properties");
    properties4 = feature4.getJSONObject("properties");

    when(parser.convertStringToJson(any(String.class))).thenReturn(json);
    when(parser.getJsonArray(any(JSONObject.class), any(String.class))).thenReturn(featuresArray);

    ProjCoordinate mockRt90 = mock(ProjCoordinate.class);
    ProjCoordinate mockSweRef = mock(ProjCoordinate.class);
 
    doNothing().when(mockRt90).setValue(rt90x, rt90y);
    doNothing().when(mockSweRef).setValue(sweRefx, sweRefy);

    when(transformation.transformRT90(any(double.class), any(double.class))).thenReturn(mockRt90);
    when(transformation.transformSWEREF99(any(double.class), any(double.class))).thenReturn(mockSweRef);

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
    JSONObject result = instance.stringToJson(jsonString);
    assertNotNull(result);
  }

  /**
   * Test of stringToJson method, of class PeliasParser.
   */
  @Test
  public void testStringToJson() {
    System.out.println("stringToJson");
    JSONObject result = instance.stringToJson(jsonString);
    assertNotNull(result);
    assertEquals(result.getString("type"), "FeatureCollection");
    assertEquals(result.getJSONArray("features").length(), 4);
    verify(parser, times(1)).convertStringToJson(jsonString);
  }

  /**
   * Test of getFeatures method, of class PeliasParser.
   */
  @Test
  public void testGetFeatures() {
    System.out.println("getFeatures");
    JSONArray result = instance.getFeatures(json);
    assertNotNull(result);
    assertEquals(result.length(), 4);
    verify(parser, times(1)).getJsonArray(json, "features");
  }

  /**
   * Test of getFirstFeature method, of class PeliasParser.
   */
  @Test
  public void testGetFirstFeature() {
    System.out.println("getFirstFeature");

    JSONObject result = instance.getFirstFeature(json);
    assertNotNull(result);
    assertEquals(result.getJSONObject("properties").getString("label"), "Bromma, Stockholm, Sweden");
    verify(parser, times(1)).getJsonArray(json, "features");
  }

  @Test
  public void testGetFirstFeatureWithEmptyFeatures() {
    System.out.println("getFirstFeature");

    String jsonStr = "{\n"
            + "	\"features\": [],\n"
            + "	\"type\": \"FeatureCollection\"\n"
            + "}";
    JSONObject jsonObj = new JSONObject(jsonStr);
    JSONArray array = jsonObj.getJSONArray("features");
    when(parser.getJsonArray(any(JSONObject.class), any(String.class))).thenReturn(array);

    JSONObject result = instance.getFirstFeature(jsonObj);
    assertNull(result);
    verify(parser, times(1)).getJsonArray(jsonObj, "features");
  }

  /**
   * Test of getGeometry method, of class PeliasParser.
   */
  @Test
  public void testGetGeometry() {
    System.out.println("getGeometry");

    when(parser.getJsonObject(any(JSONObject.class), any(String.class))).thenReturn(geometry);
    JSONObject result = instance.getGeometry(feature);

    assertNotNull(result);
    assertEquals(result.getJSONArray("coordinates").length(), 2);
    verify(parser, times(1)).getJsonObject(feature, "geometry");
  }

  /**
   * Test of getProperties method, of class PeliasParser.
   */
  @Test
  public void testGetProperties() {
    System.out.println("getProperties");
    when(parser.getJsonObject(any(JSONObject.class), any(String.class))).thenReturn(properties);
    JSONObject result = instance.getProperties(feature);
    assertNotNull(result);
    assertEquals(result.getString("label"), "Bromma, Stockholm, Sweden");
  }

  /**
   * Test of getLabel method, of class PeliasParser.
   */
  @Test
  public void testGetLabel() {
    System.out.println("getLabel");
    String expResult = "Bromma, Stockholm, Sweden";
    String result = instance.getLabel(properties);
    assertEquals(expResult, result);
  }

  /**
   * Test of getBBox method, of class PeliasParser.
   */
  @Test
  public void testGetBBox() {
    System.out.println("getBBox");
    JSONArray result = instance.getBBox(feature);
    assertEquals(4, result.length());
  }

  @Test
  public void testGetBBoxNoData() {
    System.out.println("getBBox");
    JSONArray result = instance.getBBox(feature2);
    assertNull(result);
  }

  @Test
  public void testGetAddendum() {
    System.out.println("getAddendum");
    JSONObject result = instance.getAddendum(properties);
    assertNotNull(result);
  }

  /**
   * Test of getAddendum method, of class PeliasParser.
   */
  @Test
  public void testGetAddendumNoData() {
    System.out.println("getAddendum");
    JSONObject result = instance.getAddendum(properties4);
    assertNull(result);
  }

  /**
   * Test of getAddendum method, of class PeliasParser.
   */
  @Test
  public void testGetAddendumWithNull() {
    System.out.println("getAddendum");
    JSONObject result = instance.getAddendum(null);
    assertNull(result);
  }

  /**
   * Test of getGeorg method, of class PeliasParser.
   */
  @Test
  public void testGetGeorg() {
    System.out.println("getGeorg");
    JSONObject result = instance.getGeorg(properties);
    assertNotNull(result);
  }

  @Test
  public void testGetGeorgWithoutGeorg() {
    System.out.println("getGeorg");
    JSONObject result = instance.getGeorg(properties3);
    assertNull(result);
  }

  @Test
  public void testGetGeorgWithoutAddendum() {
    System.out.println("getGeorg");
    JSONObject result = instance.getGeorg(properties4);
    assertNull(result);
  }

  /**
   * Test of hasUncertainty method, of class PeliasParser.
   */
  @Test
  public void testHasUncertainty() {
    System.out.println("hasUncertainty");
    georg1 = properties2.getJSONObject("addendum").getJSONObject("georg");
    boolean result = instance.hasUncertainty(georg1);
    assertTrue(result);
  }

  @Test
  public void testHasUncertaintyFalse1() {
    System.out.println("hasUncertainty");
    georg2 = properties.getJSONObject("addendum").getJSONObject("georg");
    boolean result = instance.hasUncertainty(georg2);
    assertFalse(result);
  }

  @Test
  public void testHasUncertaintyFalse2() {
    System.out.println("hasUncertainty");
    boolean result = instance.hasUncertainty(null);
    assertFalse(result);
  }

  /**
   * Test of getUncertaintyInMeters method, of class PeliasParser.
   */
  @Test
  public void testGetUncertaintyInMeters() {
    System.out.println("getUncertaintyInMeters");

    int result = instance.getUncertaintyInMeters(properties2);
    assertEquals(2000, result);
  }

  /**
   * Test of getUncertaintyInMeters method, of class PeliasParser.
   */
  @Test
  public void testGetUncertaintyInMeters1() {
    System.out.println("getUncertaintyInMeters");

    Integer result = instance.getUncertaintyInMeters(properties);
    assertNull(result);
  }

  /**
   * Test of getUncertaintyInMeters method, of class PeliasParser.
   */
  @Test
  public void testGetUncertaintyInMeters2() {
    System.out.println("getUncertaintyInMeters");

    Integer result = instance.getUncertaintyInMeters(properties3);
    assertNull(result);
  }

  /**
   * Test of getUncertaintyInMeters method, of class PeliasParser.
   */
  @Test
  public void testGetUncertaintyInMeters3() {
    System.out.println("getUncertaintyInMeters");

    Integer result = instance.getUncertaintyInMeters(properties4);
    assertNull(result);
  }

  /**
   * Test of getCoordinates method, of class PeliasParser.
   */
  @Test
  public void testGetCoordinates() {
    System.out.println("getCoordinates");

    when(parser.getJsonObject(any(JSONObject.class), any(String.class))).thenReturn(geometry);
    JSONArray result = instance.getCoordinates(feature);
    assertEquals(2, result.length());
  }

  /**
   * Test of addCoordinatesJson method, of class PeliasParser.
   */
  @Test
  public void testAddCoordinatesJson() {
    System.out.println("addCoordinatesJson");
    double lat = 59.350000;
    double lng = 17.916700;

    instance.addCoordinatesJson(properties, lat, lng);
    verify(transformation, times(1)).transformRT90(lat, lng);
    verify(transformation, times(1)).transformSWEREF99(lat, lng);
  }

  /**
   * Test of addCoordinatesToGeometry method, of class PeliasParser.
   */
  @Test
  public void testAddCoordinatesToGeometry() {
    System.out.println("addCoordinatesToGeometry");
    double lat = 59.350000;
    double lng = 17.916700;

    String geoJsonString = "{\n"
            + "	\"geometry\": {\n"
            + "		\"coordinates\": [\n"
            + "			17.90154,\n"
            + "			59.34439\n"
            + "		]\n"
            + "	}\n"
            + "}";
    JSONObject geometryJson = new JSONObject(geoJsonString);
    when(parser.buildJsonObject(geometry, "coordinates", lng, lat)).thenReturn(geometryJson);
    instance.addCoordinatesToGeometry(geometry, lat, lng);
    verify(parser, times(1)).buildJsonObject(geometry, "coordinates", lng, lat);
    System.out.println("geometry..." + geometry);
  }

  /**
   * Test of addDinPlats method, of class PeliasParser.
   */
  @Test
  public void testAddDinPlats() {
    System.out.println("addDinPlats"); 
    
    double lat = 59.350000;
    double lng = 17.916700;  
    JSONObject result = instance.addDinPlats(properties, lat, lng); 
    assertNotNull(result);
    assertEquals(result.getJSONObject("properties").getString("gid"), "newMarker");
    assertEquals(result.getJSONObject("properties").getString("name"), "Din plats");
  }

}
