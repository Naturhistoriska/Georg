package se.nrm.georg.service.logic.pelias;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import se.nrm.georg.service.logic.coordinates.CoordinatesHelper;
import se.nrm.georg.service.model.CSVBean;
import se.nrm.georg.service.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class PeliasToCSVConvertor {

  private final String noSearchResult = "No result";

  @Inject
  private PeliasParser parser;
  @Inject
  private UncertaintyParser uncertainty;

  public PeliasToCSVConvertor() {

  }

  public PeliasToCSVConvertor(PeliasParser parser, UncertaintyParser uncertainty) {
    this.parser = parser;
    this.uncertainty = uncertainty;
  }

  /**
   *
   * @param jsonString
   * @param id
   * @param locality
   * 
   * @return CSVBean
   */
  public CSVBean createBean(String jsonString, String id, String locality) {
    log.info("createBean");

    JSONObject feature = parser.getFirstFeature(parser.stringToJson(jsonString));
    if (feature == null) {
      return new CSVBean(id, locality, noSearchResult);
    }

    JSONObject propertiesJson = parser.getProperties(feature);  
    int uncertaintyInMeter = uncertainty.getUncertainty(feature, propertiesJson, parser); 

    JSONArray coordinates = parser.getCoordinates(feature); 
    double doubleLat = Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(1));
    double doubleLng = Util.getInstance().convertBigDecimalToDouble(coordinates.getBigDecimal(0));
    
    return new CSVBean(id, locality, parser.getLabel(propertiesJson), 
            doubleLat, doubleLng, CoordinatesHelper.getInstance().buildDMS(doubleLat, doubleLng), 
            uncertaintyInMeter, parser.getSource(propertiesJson), 
            parser.getLayer(propertiesJson), parser.getCountry(propertiesJson));
  }
}
