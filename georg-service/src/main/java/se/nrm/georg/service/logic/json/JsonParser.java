package se.nrm.georg.service.logic.json;
 
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author idali
 */
@Slf4j
public class JsonParser {
 
  public JsonParser() { 
  }
  
  public JSONObject convertStringToJson(String jsonString) {
    return new JSONObject(jsonString);
  }
  
  public JSONArray getJsonArray(JSONObject json, String key) {
    return json.getJSONArray(key);
  }
  
  public JSONObject getJsonObject(JSONObject json, String key) {
    return json.getJSONObject(key);
  }
  
  public JSONObject buildJsonObject(String key, String value) {
    JSONObject jsonObject = new JSONObject(); 
    jsonObject.put(key, value);  
    return jsonObject;
  }
  
  public JSONObject buildJsonObject(JSONObject json, String key, double lat, double lng) {
    JSONArray array = new JSONArray();
    array.put(0, lat);
    array.put(1, lng);
    json.put(key, array);
    return json;
  }
  
  public JSONObject buildJsonObject(JSONObject json, String key, String lat, String lng) {
    JSONArray array = new JSONArray();
    array.put(0, lat);
    array.put(1, lng);
    json.put(key, array);
    return json;
  }
  
  public JSONObject buildJsonObject(JSONObject json, String key, String value) { 
    json.put(key, value);  
    return json;
  }
  
  public JSONObject buildJsonObject(JSONArray jsonArray, String key) { 
    JSONObject jsonObject = new JSONObject();
    jsonObject.put(key, jsonArray.join(" ")); 
    return jsonObject;
  } 
  
  public JSONObject buildJsonObject(String key, JSONObject value) {
    JSONObject json = new JSONObject();
    json.put(key, value);
    return json;
  }
}
