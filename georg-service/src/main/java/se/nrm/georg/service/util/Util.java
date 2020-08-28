package se.nrm.georg.service.util;
 

/**
 *
 * @author idali
 */
public class Util {
   
  private final String search = "search?";
  private final String autoComplete = "autocomplete?"; 
  private final String textQuery = "text="; 
  private final String sourceQry = "&sources=";
  private final String layerQry = "&layers=";
  private final String sizeQry = "&size=";
  private final String reverseGeoCode = "reverse?";
  private final String pointLat = "point.lat=";
  private final String pointLon = "point.lon=";
  private final String and = "&"; 
  private final String emptyString = "";
  private final String wildCard = "*";
  private final String countryQuery = "&boundary.country=";
  private StringBuilder sb; 
  
  private static Util instance = null;
  
  public static Util getInstance() {
    synchronized (Util.class) {
      if (instance == null) {
        instance = new Util();
      }
    }
    return instance;
  }
  
//  public String buildGbifDatasetUrl(String gbifUrl, String datasetKey, String occurranceId) {
//    sb = new StringBuilder();
//    sb.append(gbifUrl);
//    sb.append("search?datesetKey=");
//    sb.append(datasetKey);
//    sb.append("&occurrenceID=");
//    sb.append(occurranceId);
//    return sb.toString().trim();
//  }
  
  public String bunildAutoCompleteSearchPath(String peliasPath, String text,
          String sources, String layers, String countryCode, int size) {
    
    return buildSearchUrl(peliasPath, autoComplete, text, sources, layers, countryCode, size);  
  } 
 
  public String buildGeoCodePath(String peliasPath, String address, 
          String sources, String layers, String countryCode, int size) {
    return buildSearchUrl(peliasPath, search, address, sources, layers, countryCode, size);   
  }
  
  public String buildReverseGeoCodePath(String peliasPath, double lat, double lon) { 
    sb = new StringBuilder();
    sb.append(peliasPath);
    sb.append(reverseGeoCode);
    sb.append(pointLon);
    sb.append(lon);
    sb.append(and);
    sb.append(pointLat);
    sb.append(lat);
    return sb.toString().trim();
  } 
  
  private String buildSearchUrl(String peliasPath, String searchType, String text, 
          String sources, String layers, String countryCode, int size) {
    sb = new StringBuilder();
    sb.append(peliasPath);
    sb.append(searchType); 
    sb.append(textQuery);
    sb.append(text);  
    sb.append(searchType.equals(search) ? wildCard : emptyString);
    sb.append(countryQuery);
    sb.append(countryCode);
    
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
}
