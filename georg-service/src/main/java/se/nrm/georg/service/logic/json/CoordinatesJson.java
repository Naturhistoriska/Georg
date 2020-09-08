package se.nrm.georg.service.logic.json;
 
import java.math.BigDecimal; 
import java.math.RoundingMode;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.proj4j.ProjCoordinate;
import se.nrm.georg.service.logic.coordinates.CoordinatesBuilder;
import se.nrm.georg.service.logic.coordinates.CoordinatesHelper;

/**
 *
 * @author idali
 */
@Slf4j
public class CoordinatesJson {
  
  @Inject
  private JsonParser parser;
  @Inject
  private CoordinatesBuilder builder;
  
  private final String featuresKey = "features";
  private final String geometryKey = "geometry";
  private final String coordinatesKey = "coordinates";
  private final String propertiesKey = "properties";
  
  private final String ddKey = "dd";
  private final String dmsKey = "dms";
  private final String ddmKey = "ddm";
  private final String rt90Key = "rt90";
  private final String sweref99Key = "sweref99";
   
  public CoordinatesJson() {
    
  }
 
  public CoordinatesJson(JsonParser parser, CoordinatesBuilder builder) {
    this.parser = parser;
    this.builder = builder;
  }
  
  public String addCoordinatesTransformation(String jsonString) {
    log.info("addCoordinatesTransformation");
 
    JSONObject jsonObject = parser.convertStringToJson(jsonString);
    JSONArray array = parser.getJsonArray(jsonObject, featuresKey);

    array.iterator().forEachRemaining(element -> {
      JSONObject jsonObj = (JSONObject) element;

      JSONObject geometry = parser.getJsonObject(jsonObj, geometryKey);
      JSONArray coordinates = parser.getJsonArray(geometry, coordinatesKey);
      BigDecimal lng = coordinates.getBigDecimal(0).setScale(6, RoundingMode.HALF_UP);
      BigDecimal lat = coordinates.getBigDecimal(1).setScale(6, RoundingMode.HALF_UP); 
       
      JSONObject coordinatesJson = new JSONObject(); 
      parser.buildJsonObject(coordinatesJson, ddKey, lat.doubleValue(), lng.doubleValue());
      parser.buildJsonObject(coordinatesJson, dmsKey, 
              CoordinatesHelper.getInstance().buildDMS(lat.doubleValue(), true), 
              CoordinatesHelper.getInstance().buildDMS(lng.doubleValue(), false));
      parser.buildJsonObject(coordinatesJson, ddmKey, 
              CoordinatesHelper.getInstance().buildDDM(lat.doubleValue(), true), 
              CoordinatesHelper.getInstance().buildDDM(lng.doubleValue(), false));
      
      ProjCoordinate rt90 = builder.buildRT90(lat, lng);
      ProjCoordinate sweref99 = builder.buildSWEREF99(lat, lng);
      parser.buildJsonObject(coordinatesJson, rt90Key, 
              String.valueOf(Math.round(rt90.y)), 
              String.valueOf(Math.round(rt90.x)));
      parser.buildJsonObject(coordinatesJson, sweref99Key, 
              String.valueOf(Math.round(sweref99.y)), 
              String.valueOf(Math.round(sweref99.x)));

//      parser.buildJsonObject(coordinatesJson, ddKey, builder.buildDD(lat, lng));
//      parser.buildJsonObject(coordinatesJson, dmsKey, builder.buildDMS(lat, lng));
//      parser.buildJsonObject(coordinatesJson, ddmKey, builder.buildDDM(lat, lng));
//      parser.buildJsonObject(coordinatesJson, rt90Key, builder.buildRT90(lat, lng));
      
 
      JSONObject property = jsonObj.getJSONObject(propertiesKey);
      property.put(coordinatesKey, coordinatesJson);
    });
    return jsonObject.toString();
  } 
}
