package se.nrm.georg.service.logic.exceptions;

import javax.inject.Inject; 
import se.nrm.georg.service.logic.json.JsonParser;

/**
 *
 * @author idali
 */
public class ErrorMessageBuilder {
  
  @Inject 
  private JsonParser parser;
   
  private final String errorKey = "error";
  private final String msgKey = "msgKey";
  private final String invalidCoordinates = "Invalid coordinates";
  private final String invalidCsvFile = "Invalid CSV file"; 
  private final String peliasNotAvailable = "Pelias is not available";
  private final String missingSearchText = "Search text is missing";
  
  public ErrorMessageBuilder() {
    
  }
  
  public ErrorMessageBuilder(JsonParser parser) {
    this.parser = parser;
  }
   
  public String buildInvalidCoordinatesMessage() {  
    return parser.buildJsonObject(errorKey, 
            parser.buildJsonObject(msgKey, invalidCoordinates)).toString(); 
  }
  
  public String buildInvalidCSVFileMessage() {
    return parser.buildJsonObject(errorKey, 
            parser.buildJsonObject(msgKey, invalidCsvFile)).toString();  
  }
  
  public String buildPeliasNotAvailableMessage() {
    return parser.buildJsonObject(errorKey, 
            parser.buildJsonObject(msgKey, peliasNotAvailable)).toString();  
  }
  
  public String buildSearchTextMissingMessage() {
    return parser.buildJsonObject(errorKey, 
            parser.buildJsonObject(msgKey, missingSearchText)).toString();  
  }
  
}
