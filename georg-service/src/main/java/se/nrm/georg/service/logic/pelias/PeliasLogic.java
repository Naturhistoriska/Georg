package se.nrm.georg.service.logic.pelias;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import se.nrm.georg.service.logic.coordinates.CoordinatesHelper; 
import se.nrm.georg.service.logic.json.JsonParser;
import se.nrm.georg.service.logic.json.UncertaintyParser;
import se.nrm.georg.service.logic.services.ExternalServices;
import se.nrm.georg.service.model.CSVBean;
import se.nrm.georg.service.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class PeliasLogic {

  private String peliasUrl;
  @Inject
  private JsonParser parser;  
  @Inject
  private ExternalServices service; 
  @Inject
  private UncertaintyParser uncertainty;
  
  private final String noSearchResult = "No result";
  private final String comma = ", ";
  private final String semicolon = "; ";
  private final String space = " ";
  private final String latLngWGS = "lat, lng WGS84 DD; ";
  private final String kmUncertainty = " km uncertainty";
  
  public List<CSVBean> processBatch(Map<String, String> map, String peliasPath) {
    List<CSVBean> beans = new ArrayList();
 
 
    peliasUrl = Util.getInstance().buildBatchUrl(peliasPath);
    map.entrySet().stream()
            .forEach(m -> {
              String id = m.getKey();
              String name = m.getValue();
              String result = search(name);
//              CSVBean bean;
                JSONObject json = parser.convertStringToJson(result);
//              JSONArray jsonArray = parser.getJsonArray(json, "features");
//              if (jsonArray.length() > 0) {
//                JSONObject featureJson = jsonArray.getJSONObject(0);
//                JSONObject propertiesJson = featureJson.getJSONObject("properties");
//                String label = propertiesJson.getString("label");
//                JSONObject geometryJson = featureJson.getJSONObject("geometry");
//                JSONArray coordinates = parser.getJsonArray(geometryJson, "coordinates"); 
//                double doubleLng = coordinates.getBigDecimal(0).setScale(6, RoundingMode.HALF_UP).doubleValue();
//                double doubleLat = coordinates.getBigDecimal(1).setScale(6, RoundingMode.HALF_UP).doubleValue();     
//                bean = new CSVBean(id, name, label, doubleLat, doubleLng, 
//                        uncertainty.getUncertainty(featureJson)); 
//                
//              } else {
//                bean = new CSVBean(id, name, noSearchResult); 
//              }

              beans.add(createBean(parser.getJsonArray(json, "features"), m));  
    }); 
    return beans; 
  }

  public String search(String address) {
    return service.getResults(peliasUrl.concat(address));
  }

  private CSVBean createBean(JSONArray jsonArray, Entry<String, String> e) {
    if (jsonArray.isEmpty()) {
      return new CSVBean(e.getKey(), e.getValue(), noSearchResult);
    }
    
    JSONObject featureJson = jsonArray.getJSONObject(0);
    JSONObject propertiesJson = featureJson.getJSONObject("properties");
    String label = propertiesJson.getString("label");
    JSONObject geometryJson = featureJson.getJSONObject("geometry");
    int uncertaintyInMeter = uncertainty.getUncertainty(featureJson);
    
    JSONArray coordinates = parser.getJsonArray(geometryJson, "coordinates"); 
    double doubleLat = Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(1));
    double doubleLng = Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(0));
    return new CSVBean(e.getKey(), e.getValue(), label, doubleLat, doubleLng, 
            CoordinatesHelper.getInstance().buildDMS(doubleLat, doubleLng), uncertaintyInMeter); 
  }
  
//  private String createResultText(String suggestedLocality, double lat, double lng, int uncertaintyInMeter) {
//    StringBuilder sb = new StringBuilder();
//    sb.append(suggestedLocality);
//    sb.append(semicolon);
//    sb.append(lat);
//    sb.append(comma);
//    sb.append(lng);
//    sb.append(space);
//    sb.append(latLngWGS);
//    sb.append(uncertaintyInMeter);
//    sb.append(kmUncertainty);
//    return sb.toString();
//  }
}
