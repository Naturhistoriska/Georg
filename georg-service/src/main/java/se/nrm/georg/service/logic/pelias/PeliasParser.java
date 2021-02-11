package se.nrm.georg.service.logic.pelias;

import java.text.DecimalFormat;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.proj4j.ProjCoordinate;
import se.nrm.georg.service.logic.coordinates.CoordinatesHelper;
import se.nrm.georg.service.logic.coordinates.CoordinatesTransformation;
import se.nrm.georg.service.logic.json.JsonParser;

/**
 *
 * @author idali
 */
@Slf4j
public class PeliasParser {
  
  private final String features = "features"; 
  private final String bbox = "bbox"; 
  private final String geometry = "geometry"; 
  private final String properties = "properties";
  
  private final String addendum = "addendum";
  private final String georg = "georg";
  
  private final String labelKey = "label";
  private final String uncertaintyInMeters = "coordinateUncertaintyInMeters";
  
  private final String coordinatesKey = "coordinates"; 
  
  private final String nameKey = "name";
  private final String idKey = "id";
  private final String gidKey = "gid";
  private final String typeKey = "type";
  private final String dinPlats = "Din plats";
  private final String newMarker = "newMarker";
  private final String feature = "Feature";
  private final String point = "Point";
  private final String countryKey = "country";
  private final String regionKey = "region";
  private final String countyKey = "county";
  
  private final String ddKey = "dd";
  private final String dmsKey = "dms";
  private final String ddmKey = "ddm";
  private final String rt90Key = "rt90";
  private final String sweref99Key = "sweref99";
  private final DecimalFormat df = new DecimalFormat("###.000000");
  
  @Inject
  private JsonParser parser;
  @Inject
  private CoordinatesTransformation transformation;
    
  public PeliasParser() {
    
  }
  
  public PeliasParser(JsonParser parser, CoordinatesTransformation transformation) {
    this.parser = parser;
    this.transformation = transformation;
  }
  
  /**
   * 
   * @param jsonString - pelias search result
   * @return JSONObject - pelias search result in json
   */
  public JSONObject stringToJson(String jsonString) {
    log.info("stringToJson");
    return parser.convertStringToJson(jsonString);
  }
  
  /**
   * 
   * @param json 
   * @return - JSONArray
   */
  public JSONArray getFeatures(JSONObject json) { 
    return parser.getJsonArray(json, features);
  }
  
  public JSONObject getFirstFeature(JSONObject json) { 
    JSONArray featuresJson = getFeatures(json);
    return featuresJson.isEmpty() ? null : featuresJson.getJSONObject(0);
  }
  
  /**
   * 
   * @param json - feature json
   * @return - geometry json
   */
  public JSONObject getGeometry(JSONObject json) { 
    return parser.getJsonObject(json, geometry);
  }
  
  /**
   * 
   * @param json - feature JSON
   * @return JSONObject - properties JSON
   */
  public JSONObject getProperties(JSONObject json) { 
    return parser.getJsonObject(json, properties);
  }
  
  public String getLabel(JSONObject json) { 
    return json.getString(labelKey);
  }
  
  public JSONArray getBBox(JSONObject json) { 
    return json.has(bbox) ? json.getJSONArray(bbox) : null;
  }
 
  
  /**
   * 
   * @param json - properties
   * @return JSONObject
   */
  public JSONObject getAddendum(JSONObject json) {  
    return json != null && json.has(addendum) ? json.getJSONObject(addendum) : null;
  }
   
  /**
   * 
   * @param json - properties
   * @return JSONObject 
   */
  public JSONObject getGeorg(JSONObject json) {  
    JSONObject auddendumJson = getAddendum(json);    
    return auddendumJson != null && auddendumJson.has(georg) ? auddendumJson.getJSONObject(georg) : null;
  }
  
  /**
   * 
   * @param json - georg json
   * @return boolean
   */
  public boolean hasUncertainty(JSONObject json) {  
    return json != null && json.has(uncertaintyInMeters) && !json.isNull(uncertaintyInMeters);
  }
  
  /**
   * 
   * @param json - properties
   * @return int
   */
  public Integer getUncertaintyInMeters(JSONObject json) { 
    JSONObject georgJson = getGeorg(json); 
    return hasUncertainty(georgJson) ? georgJson.getInt(uncertaintyInMeters) : null;  
  } 
  
  /**
   * 
   * @param json - feature json
   * @return JSONArray - coordinates json array
   */
  public JSONArray getCoordinates(JSONObject json) {
    return getGeometry(json).getJSONArray(coordinatesKey); 
  }
  
  public void addCoordinatesJson(JSONObject propertyJson, double lat, double lng) {
    propertyJson.put(coordinatesKey,
              buildCoordinatesTransformationJson(lat, lng));
  }
  
  public void addCoordinatesToGeometry(JSONObject geometry, double lat, double lng) {
    parser.buildJsonObject(geometry, coordinatesKey, lng, lat);
  }
   
  
  private JSONObject buildCoordinatesTransformationJson(double lat, double lng) {
    JSONObject coordinatesJson = new JSONObject();
    
    parser.buildJsonObject(coordinatesJson, ddKey, df.format(lat), df.format(lng));
    parser.buildJsonObject(coordinatesJson, dmsKey,
            CoordinatesHelper.getInstance().buildDMS(lat, true),
            CoordinatesHelper.getInstance().buildDMS(lng, false));
    parser.buildJsonObject(coordinatesJson, ddmKey,
            CoordinatesHelper.getInstance().buildDDM(lat, true),
            CoordinatesHelper.getInstance().buildDDM(lng, false));

    ProjCoordinate rt90 = transformation.transformRT90(lat, lng);
    ProjCoordinate sweref99 = transformation.transformSWEREF99(lat, lng);
    parser.buildJsonObject(coordinatesJson, rt90Key,
            String.valueOf(Math.round(rt90.y)),
            String.valueOf(Math.round(rt90.x)));
    parser.buildJsonObject(coordinatesJson, sweref99Key,
            String.valueOf(Math.round(sweref99.y)),
            String.valueOf(Math.round(sweref99.x)));

    return coordinatesJson;
  }
  
  public JSONObject addDinPlats(JSONObject json, double lat, double lng ) { 
    log.info("addDinPlats");
    JSONObject dinPlatsJson = new JSONObject();
    JSONObject geometryJson = new JSONObject();
    parser.buildJsonObject(geometryJson, coordinatesKey, lng, lat);
    geometryJson.put(typeKey, point);
    JSONObject propertiesJson = new JSONObject();
    propertiesJson.put(idKey, newMarker);
    propertiesJson.put(gidKey, newMarker);
    propertiesJson.put(nameKey, dinPlats);
    propertiesJson.put(coordinatesKey, buildCoordinatesTransformationJson(lat, lng));
    if (json != null) {   
      if (json.has(countryKey)) {
        propertiesJson.put(countryKey, json.getString(countryKey));
      }
      if (json.has(regionKey)) {
        propertiesJson.put(regionKey, json.getString(regionKey));
      }
      if (json.has(countyKey)) {
        propertiesJson.put(countyKey, json.getString(countyKey));
      }
    } 
    dinPlatsJson.put(geometry, geometryJson);
    dinPlatsJson.put(typeKey, feature);
    dinPlatsJson.put(properties, propertiesJson); 
    return dinPlatsJson;
  } 
}
