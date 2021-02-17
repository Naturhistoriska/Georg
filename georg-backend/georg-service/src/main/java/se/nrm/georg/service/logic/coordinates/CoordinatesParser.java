package se.nrm.georg.service.logic.coordinates;
  
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;  
import se.nrm.georg.service.logic.pelias.PeliasParser;
import se.nrm.georg.service.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class CoordinatesParser {
  
  @Inject
  private PeliasParser parser;
  
  public CoordinatesParser() {
    
  }
  
  public CoordinatesParser(PeliasParser parser) {
    this.parser = parser;
  }
  
  public String addCoordinatesTransformation(String jsonString) {
    log.info("addCoordinatesTransformation");

    JSONObject json = parser.stringToJson(jsonString);
    JSONArray array = parser.getFeatures(json); 
    array.iterator().forEachRemaining(element -> {
      JSONObject jsonObj = (JSONObject) element;    
      JSONArray coordinates = parser.getCoordinates(jsonObj); 
       
      JSONObject property = parser.getProperties(jsonObj);
      parser.addCoordinatesJson(property, Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(1)), 
              Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(0)));  
    });
    return json.toString();
  }
  
  public String addCoordinatesTransformationNewPlace(String jsonString, Double lat, Double lng) { 
    JSONObject json = parser.stringToJson(jsonString);
    JSONArray array = parser.getFeatures(json); 
    JSONObject properties = array.isEmpty() ? null : parser.getProperties(array.getJSONObject(0)); 
    JSONObject dinPlatsJson = parser.addDinPlats(properties,
            Util.getInstance().roundDoubleToSix(lat),
            Util.getInstance().roundDoubleToSix(lng)); 
    array.put(dinPlatsJson);
    return json.toString();
  }

  public String addCoordinatesTransformation(String jsonString, Double lat, Double lng)  {
    JSONObject json = parser.stringToJson(jsonString);
    JSONArray array = parser.getFeatures(json); 
    
    JSONObject properties;
    if(array.isEmpty()) {
      properties = null;
    } else {
      array.iterator().forEachRemaining(element -> {
        JSONObject feature = (JSONObject) element;
        JSONObject geometryJson = parser.getGeometry(feature);
        JSONObject property = parser.getProperties(feature);
        JSONArray coordinates = parser.getCoordinates(feature);

        double realLng = Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(0));
        double realLat = Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(1)); 

        parser.addCoordinatesToGeometry(geometryJson, realLat, realLng);
        parser.addCoordinatesJson(property, realLat, realLng); 
      });
      properties = parser.getProperties(array.getJSONObject(0));
    }
    JSONObject dinPlatsJson = parser.addDinPlats(properties,
            Util.getInstance().roundDoubleToSix(lat),
            Util.getInstance().roundDoubleToSix(lng));
    array.put(dinPlatsJson);
    return json.toString();
  } 
}
