package se.nrm.georg.service.util;
 
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.extern.slf4j.Slf4j;


/**
 *
 * @author idali
 */
@Slf4j
public class Util {
   
  private final String search = "search?";
  private final String autoComplete = "autocomplete?"; 
  private final String textQuery = "text="; 
  private final String sourceQry = "&sources=";
  private final String layerQry = "&layers="; 
  private final String sizeQry = "&size=";
  private final String sizeOne = "size=1";
  private final String reverseGeoCode = "reverse?";
  private final String pointLat = "point.lat=";
  private final String pointLon = "point.lon=";
  private final String and = "&"; 
  private final String emptyString = "";
  private final String wildCard = "*";
  private final String countryQuery = "&boundary.country="; 
//  private final String boundaryCircle = "&boundary.circle.radius=1000"; 
  
  private static Util instance = null;
  
  public static Util getInstance() {
    synchronized (Util.class) {
      if (instance == null) {
        instance = new Util();
      }
    }
    return instance;
  }
   
  public String bunildAutoCompleteSearchPath(String peliasPath, String text,
          String sources, String layers, String countryCode, int size) {
    
    return buildSearchUrl(peliasPath, autoComplete, text, sources, layers, countryCode, size);  
  } 
 
  public String buildAddressSearchPath(String peliasPath, String address, 
          String sources, String layers, String countryCode, int size) {
    return buildSearchUrl(peliasPath, search, address, sources, layers, countryCode, size);   
  }
  
  public String buildReverseGeoCodePath(String peliasPath, double lat, double lon) { 
    StringBuilder sb = new StringBuilder();
    sb.append(peliasPath);
    sb.append(reverseGeoCode);
    sb.append(pointLon);
    sb.append(lon);
    sb.append(and);
    sb.append(pointLat);
    sb.append(lat);
//    sb.append(coarseLayer);
//    sb.append(boundaryCircle);
//    sb.append(coarseLayer);
    return sb.toString().trim();
  }

  public String buildBatchUrl(String peliasPath) {
    StringBuilder sb = new StringBuilder();
    sb.append(peliasPath);
    sb.append(search);
    sb.append(sizeOne); 
    sb.append(and);
    sb.append(textQuery);
    return sb.toString();
  }

  private String buildSearchUrl(String peliasPath, String searchType, String text, 
          String sources, String layers, String countryCode, int size) {
    log.info("searchType : {}", searchType); 
    
    StringBuilder sb = new StringBuilder();
    sb.append(peliasPath);
    sb.append(searchType); 
    sb.append(textQuery);
    sb.append(text);  
    sb.append(searchType.equals(search) ? wildCard : emptyString);
    if(countryCode != null && countryCode.trim().length() > 0) {
      sb.append(countryQuery);
      sb.append(countryCode);
    }
 
    if(sources != null && sources.trim().length() > 0) { 
      sb.append(sourceQry);
      sb.append(sources);
    } 
    if(layers != null && layers.trim().length() > 0) {
      sb.append(layerQry);
      sb.append(layers);
    }
    if(size > 0) {
      sb.append(sizeQry);
      sb.append(size);
    }
    return sb.toString().trim();
  } 
   
  public double convertBigDecimalToDouble(BigDecimal value) {
    return value.setScale(6, RoundingMode.HALF_UP).doubleValue();
  }
  
  public double roundDoubleToSix(Double value) {
    return convertBigDecimalToDouble(new BigDecimal(value.toString())); 
  }
}
