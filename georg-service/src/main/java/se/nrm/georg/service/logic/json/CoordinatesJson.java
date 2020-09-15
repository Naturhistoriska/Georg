package se.nrm.georg.service.logic.json;
 
import java.math.BigDecimal; 
import java.math.RoundingMode; 
import java.text.DecimalFormat;
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
  private final String nameKey = "name"; 
  private final String idKey = "id";
//  private final String bboxKey = "bbox";
  private final String typeKey = "type";
  private final String dinPlatsKey = "dinPlats";
  private final String dinPlats = "Din plats";
  private final String newMarker = "newMarker";
  private final String feature = "Feature";
  private final String point = "Point";
  
  
  private final String ddKey = "dd";
  private final String dmsKey = "dms";
  private final String ddmKey = "ddm";
  private final String rt90Key = "rt90";
  private final String sweref99Key = "sweref99";
  private final DecimalFormat df = new DecimalFormat("###.000000");
    
  public CoordinatesJson() {
    
  }

  public CoordinatesJson(JsonParser parser, CoordinatesBuilder builder) {
    this.parser = parser;
    this.builder = builder;
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

    ProjCoordinate rt90 = builder.buildRT90(lat, lng);
    ProjCoordinate sweref99 = builder.buildSWEREF99(lat, lng);
    parser.buildJsonObject(coordinatesJson, rt90Key,
            String.valueOf(Math.round(rt90.y)),
            String.valueOf(Math.round(rt90.x)));
    parser.buildJsonObject(coordinatesJson, sweref99Key,
            String.valueOf(Math.round(sweref99.y)),
            String.valueOf(Math.round(sweref99.x)));

    return coordinatesJson;
  }

  public String addCoordinatesTransformation(String jsonString,
          Double lat, Double lng, boolean isNewMarker) {
    log.info("addCoordinatesTransformation");

    JSONObject jsonObject = parser.convertStringToJson(jsonString);
    JSONArray array = parser.getJsonArray(jsonObject, featuresKey);

    if (array.length() == 0) {
      JSONObject dinPlatsJson = addDinPlats(roundDoubleToSix(lat), roundDoubleToSix(lng));
      array.put(dinPlatsJson);
    } else {
      array.iterator().forEachRemaining(element -> {
        JSONObject jsonObj = (JSONObject) element;
        JSONObject geometryJson = jsonObj.getJSONObject(geometryKey);
        parser.buildJsonObject(geometryJson, coordinatesKey, lng, lat);
        JSONObject property = jsonObj.getJSONObject(propertiesKey);
        if (isNewMarker) {
          property.put(nameKey, dinPlats);
          property.put(idKey, newMarker);
        }
        property.put(coordinatesKey, buildCoordinatesTransformationJson(roundDoubleToSix(lat), roundDoubleToSix(lng)));
      });
      if (!isNewMarker) {
        JSONObject dinPlatsJson = addDinPlats(roundDoubleToSix(lat), roundDoubleToSix(lng));
        array.put(dinPlatsJson);
      }
    }

//     if (array.length() > 0) {
//       array.iterator().forEachRemaining(element -> {
//         JSONObject jsonObj = (JSONObject) element;
//         JSONObject geometryJson = jsonObj.getJSONObject(geometryKey);
//        parser.buildJsonObject(geometryJson, coordinatesKey, lng, lat); 
//        JSONObject property = jsonObj.getJSONObject(propertiesKey);
//        if (isNewMarker) {
//          property.put(nameKey, dinPlats);
//          property.put(idKey, newMarker);
//        }
//        property.put(coordinatesKey, buildCoordinatesTransformationJson(roundDoubleToSix(lat), roundDoubleToSix(lng)));
//      });
//    } else {
//      JSONObject dinPlatsJson = addDinPlats(roundDoubleToSix(lat), roundDoubleToSix(lng));
//      array.put(dinPlatsJson);
//    }
//
//    if (!isNewMarker && array.length() > 1) { 
//      JSONObject dinPlatsJson = addDinPlats(roundDoubleToSix(lat), roundDoubleToSix(lng));
//      array.put(parser.buildJsonObject(dinPlatsKey, dinPlatsJson));
//    }
    return jsonObject.toString();
  }

  private JSONObject addDinPlats(double lat, double lng) {
    log.info("addDinPlats : {}");
    JSONObject json = new JSONObject();
//     JSONArray bboxArray = parser.getJsonArray(jsonObject, "bbox");
    JSONObject geometryJson = new JSONObject();
    parser.buildJsonObject(geometryJson, coordinatesKey, lng, lat);
    geometryJson.put(typeKey, point);
    JSONObject propertiesJson = new JSONObject();
    propertiesJson.put(idKey, newMarker);
    propertiesJson.put(nameKey, dinPlats);
    propertiesJson.put(coordinatesKey, buildCoordinatesTransformationJson(lat, lng));

//    json.put(bboxKey, parser.getJsonArray(jsonObject, bboxKey));
    json.put(geometryKey, geometryJson);
    json.put(typeKey, feature);
    json.put(propertiesKey, propertiesJson);

    return json;
  }

  private double roundDoubleToSix(Double val) {
    return new BigDecimal(val.toString()).setScale(6, RoundingMode.HALF_UP).doubleValue();
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

//      JSONObject coordinatesJson = new JSONObject(); 
//      parser.buildJsonObject(coordinatesJson, ddKey, lat.doubleValue(), lng.doubleValue());
//      parser.buildJsonObject(coordinatesJson, dmsKey, 
//              CoordinatesHelper.getInstance().buildDMS(lat.doubleValue(), true), 
//              CoordinatesHelper.getInstance().buildDMS(lng.doubleValue(), false));
//      parser.buildJsonObject(coordinatesJson, ddmKey, 
//              CoordinatesHelper.getInstance().buildDDM(lat.doubleValue(), true), 
//              CoordinatesHelper.getInstance().buildDDM(lng.doubleValue(), false));
//      
//      ProjCoordinate rt90 = builder.buildRT90(lat, lng);
//      ProjCoordinate sweref99 = builder.buildSWEREF99(lat, lng);
//      parser.buildJsonObject(coordinatesJson, rt90Key, 
//              String.valueOf(Math.round(rt90.y)), 
//              String.valueOf(Math.round(rt90.x)));
//      parser.buildJsonObject(coordinatesJson, sweref99Key, 
//              String.valueOf(Math.round(sweref99.y)), 
//              String.valueOf(Math.round(sweref99.x)));  
      JSONObject property = jsonObj.getJSONObject(propertiesKey);
      property.put(coordinatesKey,
              buildCoordinatesTransformationJson(lat.doubleValue(), lng.doubleValue()));
    });
    return jsonObject.toString();
  }
}
