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
   
  public String buildInvalidCoordinatesMessage() {  
    return parser.buildJsonObject(errorKey, parser.buildJsonObject(msgKey, invalidCoordinates)).toString(); 
  }
  
}
