package se.nrm.georg.service.logic.pelias;
 
import java.util.Optional; 
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import se.nrm.georg.service.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class UncertaintyParser {
    
  
  public UncertaintyParser() {
    
  }
  
/**
 * 
 * @param fearureJson
 * @param propertiesJson 
 * @param parser
 * @return int
 */
  public int getUncertainty(JSONObject fearureJson, 
          JSONObject propertiesJson, PeliasParser parser) {  
    
    Integer uncertainty = parser.getUncertaintyInMeters(propertiesJson);
    return Optional.ofNullable(uncertainty).orElse(0) != 0 
            ? uncertainty : calculateUncertainty(fearureJson, parser);
  }
  
  /**
   * 
   * @param json - feature
   * @param parser
   * @return int
   */
  private int calculateUncertainty(JSONObject json, PeliasParser parser) {  
    JSONArray bboxJson = parser.getBBox(json); 
    return bboxJson != null ? calculateUncertaintyFromBBox(bboxJson) : 10000;
  }
  
/**
 * 
 * @param bboxJson 
 * @return int
 */
  private int calculateUncertaintyFromBBox(JSONArray bboxJson) { 
    
    double west = Util.getInstance().convertBigDecimalToDouble(bboxJson.getBigDecimal(0));
    double south = Util.getInstance().convertBigDecimalToDouble(bboxJson.getBigDecimal(1));
    double east = Util.getInstance().convertBigDecimalToDouble(bboxJson.getBigDecimal(2));
    double north = Util.getInstance().convertBigDecimalToDouble(bboxJson.getBigDecimal(3)); 
    
    double latDeff = north - south / 2;
    double lngDeff = east - west / 2;
    return (int) Math.round(Math.sqrt(Math.pow(lngDeff, 2) + Math.pow(latDeff, 2)));
  }
}
