package se.nrm.georg.service.logic.json;

import java.math.RoundingMode;
import java.util.Optional; 
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author idali
 */
@Slf4j
public class UncertaintyParser {
   
  private final String addendum = "addendum";
  private final String bbox = "bbox"; 
  private final String georg = "georg";
  private final String uncertaintyInMeters = "coordinateUncertaintyInMeters";
  private final String properties = "properties";
  
  public UncertaintyParser() {
    
  }
  
  public int getUncertainty(JSONObject json) {  
    JSONObject propertiesJson = json.getJSONObject(properties);
    Integer uncertainty = getUncertaintyInMeters(propertiesJson);
    
    return Optional.ofNullable(uncertainty).orElse(0) != 0 ? uncertainty : calculateUncertainty(json);
  }
  
  private int calculateUncertainty(JSONObject json) {
    if(json.has(bbox)) {
      JSONArray bboxJson = json.getJSONArray(bbox);
      double west = bboxJson.getBigDecimal(0).setScale(6, RoundingMode.HALF_UP).doubleValue();
      double south = bboxJson.getBigDecimal(1).setScale(6, RoundingMode.HALF_UP).doubleValue();
      double east = bboxJson.getBigDecimal(2).setScale(6, RoundingMode.HALF_UP).doubleValue();
      double north = bboxJson.getBigDecimal(3).setScale(6, RoundingMode.HALF_UP).doubleValue();

      double latDeff = north - south / 2;
      double lngDeff = east - west / 2; 
      return (int)Math.round(Math.sqrt(Math.pow(lngDeff, 2) + Math.pow(latDeff, 2)));
    } 
    return 10000;
  }
  
  private JSONObject getAddendum(JSONObject json) {
    return json.has(addendum) ? json.getJSONObject(addendum) : null;
  }
  
  private JSONObject getGeorg(JSONObject json) {
    JSONObject addendumJson = getAddendum(json);
    return addendumJson == null ? null : addendumJson.has(georg) 
            ? addendumJson.getJSONObject(georg) : null; 
  }
  
  private Integer getUncertaintyInMeters(JSONObject json) {
    JSONObject georgJson = getGeorg(json); 
    if(georgJson != null && georgJson.has(uncertaintyInMeters)) {  
      return georgJson.isNull(uncertaintyInMeters) ? null : georgJson.getInt(uncertaintyInMeters);
    }
    return null; 
  } 
}
