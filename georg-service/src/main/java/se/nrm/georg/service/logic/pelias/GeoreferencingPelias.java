package se.nrm.georg.service.logic.pelias;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;  
import se.nrm.georg.service.logic.InitialProperties;   
import se.nrm.georg.service.logic.services.ExternalServices;
import se.nrm.georg.service.model.CSVBean;
import se.nrm.georg.service.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class GeoreferencingPelias {

  private String peliasPath;
  private String peliasUrl; 
//  private String localTestUrl = "https://georg-test.nrm.se/api/";
   
  @Inject
  private InitialProperties props;
  @Inject
  private PeliasToCSVConvertor convertor; 
  @Inject
  private ExternalServices service; 

  public GeoreferencingPelias() {

  }
  
  public GeoreferencingPelias(InitialProperties props, PeliasToCSVConvertor convertor,
                  ExternalServices service) {
    this.props = props;
    this.convertor = convertor;
    this.service = service;
  }

  @PostConstruct
  public void init() {
    peliasPath = props.getPeliasPath(); 
  }
  
  public String getPeliasPath() {
    return peliasPath;
  }
   
  public String search(String text, String sources, String layers, 
          String countryCode, int size, boolean isAutocompleteSearch) {
    peliasUrl = isAutocompleteSearch ? 
            Util.getInstance()
                    .bunildAutoCompleteSearchPath(peliasPath, text, sources, layers, countryCode, size) 
            : Util.getInstance()
                    .buildAddressSearchPath(peliasPath, text, sources, layers, countryCode, size);    
    
    // For local test
//    if(isAutocompleteSearch && isLocal()) {
//      peliasUrl = "https://georg-test.nrm.se/api/search?text=" + text;
//    }
    return service.search(peliasUrl);
  }
  
  public String reverseSearch(double lat, double lng) {
    peliasUrl = Util.getInstance().buildReverseGeoCodePath(peliasPath, lat, lng); 
    // For local test
//    if(isLocal()) {
//      peliasUrl = "https://georg-test.nrm.se/api/reverse?lat=" + lat + "&lng=" + lng;
//    }
    return service.search(peliasUrl);
  }
  
//  private boolean isLocal() {
//    return peliasPath.equals(localTestUrl);
//  }
  
  public List<CSVBean> processBatch(Map<String, String> map) { 
    List<CSVBean> beans = new ArrayList(); 
 
    peliasUrl = Util.getInstance().buildBatchUrl(peliasPath);
    map.entrySet().stream()  
            .forEach(m -> { 
              String name = m.getValue(); 
              beans.add(convertor.createBean(service.search(peliasUrl.concat(name)), m.getKey(), name));  
    }); 
    return beans; 
  } 
} 