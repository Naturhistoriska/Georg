package se.nrm.georg.service.logic;
   
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;   
import java.util.List; 
import java.util.Map;  
import javax.inject.Inject; 
import lombok.extern.slf4j.Slf4j;   
import org.apache.commons.csv.CSVRecord;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import se.nrm.georg.service.logic.coordinates.CoordinatesHelper; 
import se.nrm.georg.service.logic.coordinates.CoordinatesParser;
import se.nrm.georg.service.logic.csv.CSVParser; 
import se.nrm.georg.service.logic.file.FileHandler; 
import se.nrm.georg.service.logic.pelias.GeoreferencingPelias;  
import se.nrm.georg.service.model.CSVBean; 

/**
 *
 * @author idali
 */
@Slf4j
public class GeorgLogic implements Serializable {
   
  @Inject 
  private FileHandler fileHandler; 
  @Inject
  private GeoreferencingPelias pelias;
  @Inject
  private CSVParser csv;
   
  @Inject
  private CoordinatesParser coordinates;
     
  private final String comma = ",";  
  private final String emptySpace = " ";   
   
  public GeorgLogic() {
    
  }
  
  public GeorgLogic(CoordinatesParser coordinates, CSVParser csv, 
          FileHandler fileHandler, GeoreferencingPelias pelias) { 
    this.coordinates = coordinates;
    this.csv = csv;
    this.fileHandler = fileHandler; 
    this.pelias = pelias;
  } 
 
  /**
   * 
   * @param uploadFile - uploaded csv file
   * @param type - return file type (csv or json)
   * 
   * @return String - temporary file path
   */
  public String processBatch(InputPart uploadFile, String type) {  
    List<CSVRecord> records;
    Map<String, String> map; 
    try { 
      records = fileHandler.readCsv(uploadFile.getBody(InputStream.class, null));    
      map = csv.convertCSVToMap(records);  
      if(!map.isEmpty()) { 
        List<CSVBean> beans = pelias.processBatch(map);  
        return fileHandler.createFile(beans, type); 
      }
    } catch (IOException ex) {
      log.error((ex.getMessage())); 
    } 
    return null;
  } 
    
  /**
   * 
   * @param address - Address or place for search
   * @param source - Data source (ex: gbif, wof, osm, etc....)
   * @param layer - Data layer (ex: nhrs-nrm, s-fbo, locality, etc...)
   * @param countryCode -County code (Default Sweden)
   * @param size - Number of results for return
   * @return String
   */
  public String searchAddress(String address, String source, String layer, 
          String countryCode, int size) {
    log.info("searchAddress"); 
    return coordinates.addCoordinatesTransformation(
            pelias.search(address, source, layer, countryCode, size, false));  
  }
   
  /**
   * 
   * @param text - text for search
   * @param sources - Data source (ex: gbif, wof, osm, etc....)
   * @param layers - Data layer (ex: nhrs-nrm, s-fbo, locality, etc...)
   * @param countryCode -County code (Default Sweden)
   * @param size - Number of results for return
   * @return String
   */
  public String runAutocompleteSearch(String text, String sources, String layers,
          String countryCode, int size) {
    log.info("runAutocompleteSearch");
    return coordinates.addCoordinatesTransformation(
            pelias.search(text, sources, layers, countryCode, size, true)); 
  } 
  
  /**
   * 
   * @param coordinatesString - Coordinates in one String for search. 
   * @return String
   */
  public String coordinatesSearch(String coordinatesString) {
    System.out.println("coordinatesSearch..." + coordinatesString);

    Double[] latLng = CoordinatesHelper.getInstance()
            .getLatAndLng(coordinatesString.replace(comma, emptySpace));   
      
    return coordinates.addCoordinatesTransformation(
            pelias.reverseSearch(latLng[0], latLng[1]), latLng[0], latLng[1]);  
  } 
  
  /**
   * 
   * @param lat - latitude
   * @param lng - longitude
   * @return String
   */
  public String reverseSearch(double lat, double lng) {
    log.info("reverseSearch " );  
    return coordinates.addCoordinatesTransformationNewPlace(pelias.reverseSearch(lat, lng), lat, lng); 
  }
}
