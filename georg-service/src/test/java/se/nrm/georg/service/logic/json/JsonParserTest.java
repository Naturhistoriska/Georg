package se.nrm.georg.service.logic.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author idali
 */
public class JsonParserTest {

  private JsonParser instance;
  private String jsonString;

  public JsonParserTest() {
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

    instance = new JsonParser();
  }

  @After
  public void tearDown() {
    instance = null;
  }

  /**
   * Test of convertStringToJson method, of class JsonParser.
   */
  @Test
  public void testConvertStringToJson() {
    System.out.println("convertStringToJson");

    instance = new JsonParser();
    JSONObject result = instance.convertStringToJson(jsonString);
    assertNotNull(result);
    assertEquals(result.getString("type"), "FeatureCollection");
  }

  /**
   * Test of getJsonArray method, of class JsonParser.
   */
  @Test
  public void testGetJsonArray() {
    System.out.println("getJsonArray");
    JSONObject json = instance.convertStringToJson(jsonString);
    instance = new JsonParser();
    JSONArray result = instance.getJsonArray(json, "features");
    assertNotNull(result);
    assertEquals(result.length(), 1);
  }

  /**
   * Test of getJsonObject method, of class JsonParser.
   */
  @Test
  public void testGetJsonObject() {
    System.out.println("getJsonObject");

    JSONObject json = instance.convertStringToJson(jsonString);
    String key = "geocoding";
    instance = new JsonParser();
    JSONObject result = instance.getJsonObject(json, key);
    assertNotNull(result);
    assertEquals(result.getString("attribution"), "http://api:4000/attribution");
  }

  /**
   * Test of buildJsonObject method, of class JsonParser.
   */
  @Test
  public void testBuildJsonObject_String_String() {
    System.out.println("buildJsonObject");
    String key = "item";
    String value = "item 1";
    instance = new JsonParser(); 
    JSONObject result = instance.buildJsonObject(key, value);
    assertNotNull(result);
    assertEquals(result.getString(key), value);
  }

  /**
   * Test of buildJsonObject method, of class JsonParser.
   */
  @Test
  public void testBuildJsonObject_4args_1() {
    System.out.println("buildJsonObject");
 
    String key = "dd";
    double lat = 59.33;
    double lng = 16.66;
    
    instance = new JsonParser();
    JSONObject json = instance.convertStringToJson(jsonString);
   
    JSONObject expResult = null;
    JSONObject result = instance.buildJsonObject(json, key, lat, lng);
    assertEquals(2, result.getJSONArray(key).length()); 
  }

  /**
   * Test of buildJsonObject method, of class JsonParser.
   */
  @Test
  public void testBuildJsonObject_4args_2() {
    System.out.println("buildJsonObject");
    
    JSONObject json = instance.convertStringToJson(jsonString);
    String key = "dms";
    String lat = "59";
    String lng = "15";
    instance = new JsonParser(); 
    JSONObject result = instance.buildJsonObject(json, key, lat, lng);
    assertEquals(2, result.getJSONArray(key).length()); 
  }

  /**
   * Test of buildJsonObject method, of class JsonParser.
   */
  @Test
  public void testBuildJsonObject_3args() {
    System.out.println("buildJsonObject");
    
    JSONObject json = instance.convertStringToJson(jsonString); 
    String key = "item";
    String value = "item 1";
    instance = new JsonParser(); 
    JSONObject result = instance.buildJsonObject(json, key, value);
    assertEquals(result.get(key), value); 
  }

  /**
   * Test of buildJsonObject method, of class JsonParser.
   */
  @Test
  public void testBuildJsonObject_JSONArray_String() {
    System.out.println("buildJsonObject");
    
    String lat = "59";
    String lng = "15";
    String key = "coordinates";
    JSONArray array = new JSONArray();
    array.put(0, lat);
    array.put(1, lng); 
    instance = new JsonParser(); 
    JSONObject result = instance.buildJsonObject(array, key);
    System.out.println("result..." + result.getString(key));
    assertEquals(result.getString(key), "\"59\" \"15\"");
  }

}
