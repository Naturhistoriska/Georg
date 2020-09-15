package se.nrm.georg.service.logic;
 
//import com.peertopark.java.geocalc.DMSCoordinate;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject; 
import lombok.extern.slf4j.Slf4j;  
//import org.apache.commons.lang3.StringUtils;
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
  private final String comma = ",";
//  private final String degreeSign = "°";
//  private final String minuteSign = "'";
  
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
      
    return coordinates.addCoordinatesTransformation(service.getResults(peliasUrl)); 
  }
   
  public String runAutocompleteSearch(String text, String sources, String layers,
          String countryCode, int size) {
    log.info("runAutocompleteSearch");
    String searchUrl = Util.getInstance()
            .bunildAutoCompleteSearchPath(peliasPath, text, sources, layers, countryCode, size);   
    return coordinates.addCoordinatesTransformation(service.getResults(searchUrl)); 
  } 
  
  public String coordinatesSearch(String coordinatesString)  throws NumberFormatException {
    System.out.println("coordinatesSearch..." + coordinatesString);
//    double lat = 0;
//    double lng = 0;
    
//    coordinatesString = coordinatesString.replace(comma, emptySpace);

//    if(CoordinatesHelper.getInstance().isDD(coordinatesString)) {
//      String[] coors = coordinatesString.split(emptySpace);
//      lat = Double.valueOf(coors[0]); 
//      lng = coors.length == 2 ? Double.valueOf(coors[1]) : Double.valueOf(coors[2]);
//    } else {
//      String strLat = CoordinatesHelper.getInstance().getLatString(coordinatesString.toLowerCase());
//      String strLng = CoordinatesHelper.getInstance().getlngString(coordinatesString.toLowerCase());
//       
//      if (CoordinatesHelper.getInstance().isDMS(coordinatesString)) {
//        lat = convertDMSToDD(strLat, coordinatesString.toLowerCase().contains("s"));
//        lng = convertDMSToDD(strLng, coordinatesString.toLowerCase().contains("w"));
//      } else if (CoordinatesHelper.getInstance().isDDM(coordinatesString)) { 
//        lat = convertDDMToDD(strLat, coordinatesString.toLowerCase().contains("s"));
//        lng = convertDDMToDD(strLng, coordinatesString.toLowerCase().contains("w"));  
//      } 
//    } 
//    String peliasUrl = Util.getInstance().buildReverseGeoCodePath(peliasPath, lat, lng); 
//    String peliasUrl = "https://georg-test.nrm.se/api/reverse?lat=" + lat + "&lng="+ lng;
//    return service.getResults(peliasUrl);  
    Double[] latLng = CoordinatesHelper.getInstance()
            .getLatAndLng(coordinatesString.replace(comma, emptySpace));   
    String peliasUrl = Util.getInstance().buildReverseGeoCodePath(peliasPath, latLng[0], latLng[1]);  
 
//    String peliasUrl = "https://georg-test.nrm.se/api/reverse?lat=" + latLng[0] + "&lng="+ latLng[1];
  



    return coordinates.addCoordinatesTransformation(service.getResults(peliasUrl), latLng[0], latLng[1], false);
//    return reverseSearch(latLng[0], latLng[1]);
  } 
  
  public String reverseSearch(double lat, double lng) {
    log.info("reverseSearch " );
    String peliasUrl = Util.getInstance().buildReverseGeoCodePath(peliasPath, lat, lng); 
    
    return coordinates.addCoordinatesTransformation(service.getResults(peliasUrl), lat, lng, true); 
  }
}
