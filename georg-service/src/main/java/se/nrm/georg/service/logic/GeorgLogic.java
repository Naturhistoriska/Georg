package se.nrm.georg.service.logic;
   
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;  
import java.util.HashMap;
import java.util.List; 
import java.util.Map; 
import javax.annotation.PostConstruct;
import javax.inject.Inject; 
import lombok.extern.slf4j.Slf4j;   
import org.apache.commons.csv.CSVRecord;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import se.nrm.georg.service.logic.coordinates.CoordinatesHelper; 
import se.nrm.georg.service.logic.csv.util.CSVHeader;
import se.nrm.georg.service.logic.file.FileHandler;
import se.nrm.georg.service.logic.json.CoordinatesJson;
import se.nrm.georg.service.logic.pelias.PeliasLogic;
import se.nrm.georg.service.logic.services.ExternalServices; 
import se.nrm.georg.service.model.CSVBean;
import se.nrm.georg.service.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class GeorgLogic implements Serializable {
  
  @Inject  
  private InitialProperties props;  
//  @Inject
//  private CSVParser csv;
  
  @Inject 
  private FileHandler fileHandler;
  @Inject
  private PeliasLogic pelias;
  @Inject
  private ExternalServices service; 
  @Inject
  private CoordinatesJson coordinates;  
   
  private String peliasPath;   
  private final String comma = ",";  
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
 
  
  public String processBatch(InputPart uploadFile, String type) {  
    List<CSVRecord> records;
    Map<String, String> map = new HashMap(); 
    try {
      records = fileHandler.readCsv(uploadFile.getBody(InputStream.class, null));  
      records.stream()
              .map(r -> r.toMap())
              .filter(m -> m.containsKey(CSVHeader.Id.name()))
              .filter(m -> m.containsKey(CSVHeader.SourceLocality.name()))
              .forEach(m -> { 
                map.put(m.get(CSVHeader.Id.name()), m.get(CSVHeader.SourceLocality.name()));
              });  
      if(map.isEmpty()) {
        return null;
      }
      List<CSVBean> beans = pelias.processBatch(map, peliasPath); 
      return fileHandler.createFile(beans, type); 
    } catch (IOException ex) {
      log.error((ex.getMessage()));
    } 
    return null;
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

    Double[] latLng = CoordinatesHelper.getInstance()
            .getLatAndLng(coordinatesString.replace(comma, emptySpace));   
    String peliasUrl = Util.getInstance().buildReverseGeoCodePath(peliasPath, latLng[0], latLng[1]);  
 
//    String peliasUrl = "https://georg-test.nrm.se/api/reverse?lat=" + latLng[0] + "&lng="+ latLng[1]; // for local test

    return coordinates.addCoordinatesTransformation(service.getResults(peliasUrl), latLng[0], latLng[1], false); 
  } 
  
  public String reverseSearch(double lat, double lng) {
    log.info("reverseSearch " );
    String peliasUrl = Util.getInstance().buildReverseGeoCodePath(peliasPath, lat, lng); 
    
    return coordinates.addCoordinatesTransformation(service.getResults(peliasUrl), lat, lng, true); 
  }
}
