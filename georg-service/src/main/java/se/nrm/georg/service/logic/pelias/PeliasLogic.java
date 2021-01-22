package se.nrm.georg.service.logic.pelias;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import se.nrm.georg.service.logic.coordinates.CoordinatesHelper;
import se.nrm.georg.service.logic.csv.CSVParser;
import se.nrm.georg.service.logic.json.JsonParser;
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
  private final String noSearchResult = "No result";
  
  public String processBatch(Map<String, String> map, String peliasPath, CSVParser csv) {
    List<CSVBean> beans = new ArrayList();
 
    peliasUrl = Util.getInstance().buildBatchUrl(peliasPath);
    map.entrySet().stream()
            .forEach(m -> {
              String id = m.getKey();
              String name = m.getValue();
              String result = search(name);
              CSVBean bean;
              JSONObject json = parser.convertStringToJson(result);
              JSONArray jsonArray = parser.getJsonArray(json, "features");
              if (jsonArray.length() > 0) {
                JSONObject featureJson = jsonArray.getJSONObject(0);
                String label = featureJson.getJSONObject("properties").getString("label");
                JSONObject geometryJson = featureJson.getJSONObject("geometry");
                JSONArray coordinates = parser.getJsonArray(geometryJson, "coordinates"); 
                double doubleLng = coordinates.getBigDecimal(0).setScale(6, RoundingMode.HALF_UP).doubleValue();
                double doubleLat = coordinates.getBigDecimal(1).setScale(6, RoundingMode.HALF_UP).doubleValue();    
                bean = new CSVBean(id, name, label, 
                        CoordinatesHelper.getInstance().buildDMS(doubleLat, doubleLng)); 
              } else {
                bean = new CSVBean(id, name, noSearchResult); 
              }
              beans.add(bean);
    }); 
    return csv.createCSVFile(beans); 
  }

  public String search(String address) {
    return service.getResults(peliasUrl.concat(address));
  }
}
