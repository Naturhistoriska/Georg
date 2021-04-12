package se.nrm.georg.service.logic.json;
 
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author idali
 */
@Slf4j
public class JsonParser {
  
  private final String emptySpace = " ";
 
  public JsonParser() { 
  }
  
  public JSONObject convertStringToJson(String jsonString) throws JSONException { 
    log.info("convertStringToJson");
    return new JSONObject(jsonString);
  }
  
  public JSONArray getJsonArray(JSONObject json, String key) {
    return json.getJSONArray(key);
  }
  
  public JSONObject getJsonObject(JSONObject json, String key) {
    return json.getJSONObject(key);
  }
  
  public String getString(JSONObject json, String key) {
    return json.getString(key);
  }
  
  public JSONObject buildJsonObject(String key, String value) {
    JSONObject jsonObject = new JSONObject(); 
    jsonObject.put(key, value);  
    return jsonObject;
  }
   
  /**
   * 
   * @param json
   * @param key
   * @param lng
   * @param lat
   * @return JSONObject
   */
  public JSONObject buildJsonObject(JSONObject json, String key, double lng, double lat) {
    JSONArray array = new JSONArray();
    array.put(0, lng);
    array.put(1, lat);
    json.put(key, array);
    return json;
  }
  
  /**
   * 
   * @param json
   * @param key
   * @param lng
   * @param lat
   * @return JSONObject
   */
  public JSONObject buildJsonObject(JSONObject json, String key, String lng, String lat) {
    JSONArray array = new JSONArray();
    array.put(0, lng);
    array.put(1, lat);
    json.put(key, array);
    return json;
  }
  
  public JSONObject buildJsonObject(JSONObject json, String key, String value) { 
    json.put(key, value);  
    return json;
  }
  
  public JSONObject buildJsonObject(JSONArray jsonArray, String key) { 
    JSONObject jsonObject = new JSONObject();
    jsonObject.put(key, jsonArray.join(emptySpace)); 
    return jsonObject;
  } 
  
  public JSONObject buildJsonObject(String key, JSONObject value) {
    JSONObject json = new JSONObject();
    json.put(key, value);
    return json;
  }
}
