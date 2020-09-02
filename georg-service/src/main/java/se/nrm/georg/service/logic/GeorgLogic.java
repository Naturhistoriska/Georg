package se.nrm.georg.service.logic;
 
import com.peertopark.java.geocalc.DMSCoordinate;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject; 
import lombok.extern.slf4j.Slf4j;  
import org.apache.commons.lang3.StringUtils;
import se.nrm.georg.service.logic.coordinates.CoordinatesHelper;
import se.nrm.georg.service.logic.json.CoordinatesJson;
import se.nrm.georg.service.logic.services.ExternalServices;
import se.nrm.georg.service.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class GeorgLogic implements Serializable {
  
  @Inject  
  private InitialProperties props;  
  @Inject
  private ExternalServices service; 
  @Inject
  private CoordinatesJson coordinates; 
  
  private String peliasPath;  
  private final String secondSign = "\"";
  private final String degreeSign = "°";
  private final String minuteSign = "'";
  
  private final String emptySpace = " "; 
   
  public GeorgLogic() {
    
  }
  
  public GeorgLogic(InitialProperties props, ExternalServices service, CoordinatesJson coordinates) {
    this.props = props; 
    this.service = service;
    this.coordinates = coordinates;
  }
  
  @PostConstruct 
  public void init() {
    peliasPath = props.getPeliasPath();    
  }
  
  public String searchAddress(String address, String source, String layer, 
          String countryCode, int size) {
    log.info("searchAddress");
    
    String peliasUrl = Util.getInstance()
            .buildGeoCodePath(peliasPath, address, source, layer, countryCode, size);  
     
    String results = service.getResults(peliasUrl); 
    return coordinates.addCoordinatesTransformation(results); 
  }
  
  public String reverseSearch(double lat, double lon) {
    log.info("reverseSearch");
    String peliasUrl = Util.getInstance().buildReverseGeoCodePath(peliasPath, lat, lon);
    
    return service.getResults(peliasUrl);  
  }
    
  public String runAutocompleteSearch(String text, String sources, String layers,
          String countryCode, int size) {
    log.info("runAutocompleteSearch");
    String searchUrl = Util.getInstance()
            .bunildAutoCompleteSearchPath(peliasPath, text, sources, layers, countryCode, size);  
    String results = service.getResults(searchUrl); 
    return coordinates.addCoordinatesTransformation(results); 
  } 
  
  public String coordinatesSearch(String coordinates) {
    double lat = 0;
    double lng = 0;
    
    if(CoordinatesHelper.getInstance().isDD(coordinates)) {
      String[] coors = coordinates.split(emptySpace);
      lat = Double.valueOf(coors[0]);
      lng = Double.valueOf(coors[1]);
    } else {
      String strLat = CoordinatesHelper.getInstance().getLatString(coordinates.toLowerCase());
      String strLng = CoordinatesHelper.getInstance().getlngString(coordinates.toLowerCase());
       
      if (CoordinatesHelper.getInstance().isDMS(coordinates)) {
        lat = convertDMSToDD(strLat);
        lng = convertDMSToDD(strLng);
      } else if (CoordinatesHelper.getInstance().isDDM(coordinates)) {
        lat = convertDDMToDD(strLat);
        lng = convertDDMToDD(strLng); 
      } 
    } 
    
    String peliasUrl = Util.getInstance().buildReverseGeoCodePath(peliasPath, lat, lng); 

//    String peliasUrl = "https://georg-test.nrm.se/api/reverse?lat=" + lat + "&lng="+ lng;
    return service.getResults(peliasUrl);  
  }
  
  private double convertDDMToDD(String value) {
    int degrees = CoordinatesHelper.getInstance().getDegrees(value);
    double minutes = getMinutesInDouble(value); 
    return degrees + minutes / 60;
  }
  
  private double convertDMSToDD(String value) {
    int degrees = CoordinatesHelper.getInstance().getDegrees(value);
    int minutes = CoordinatesHelper.getInstance().getMinutes(value);
    int seconds = CoordinatesHelper.getInstance().getSeconds(value);
       
//    double decimal = ((minutes * 60) + seconds);
//    return degrees + decimal / 3600;  

    DMSCoordinate dmsCoord = new DMSCoordinate(degrees, minutes, seconds);
    return dmsCoord.getDecimalDegrees();
  }
   
  private double getMinutesInDouble(String value) {
    return Double.valueOf(StringUtils.substringBetween(value, degreeSign, minuteSign).trim());
  } 
}
