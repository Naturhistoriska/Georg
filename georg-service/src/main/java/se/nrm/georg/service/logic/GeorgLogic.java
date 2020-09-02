package se.nrm.georg.service.logic;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject; 
import lombok.extern.slf4j.Slf4j;  
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
  
  private String peliasPath;  
   
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
  } 
}
