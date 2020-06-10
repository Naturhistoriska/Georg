package se.nrm.georg.service.util;

/**
 *
 * @author idali
 */
public class Util {
   
  private final String searchGeoCode = "search?text=";
  private final String sourceQry = "&sources=";
  private final String layerQry = "&layers=";
  private final String sizeQry = "&size=";
  private final String reverseGeoCode = "reverse?";
  private final String pointLat = "point.lat=";
  private final String pointLon = "point.lon=";
  private final String and = "&";
  private final String autoComplete = "autocomplete?text=";
  private final String sizeKey = "size=";
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
  
  public String bunildAutoCompleteSearchPath(String peliasPath, String text, int size) {
    sb = new StringBuilder();
    sb.append(peliasPath);
    sb.append(autoComplete);
    sb.append(text);
    sb.append(sizeKey); 
    sb.append(size == 0 ? 10 : size);
    return sb.toString().trim();
  }
  
  public void buildGeoCodePath(String peliasPath, String address) { 
    sb = new StringBuilder();
    sb.append(peliasPath);
    sb.append(searchGeoCode);
    sb.append(address);  
  }
  
  public String buildGeoCodePath(String peliasPath, String address, String source, String layer, int size) {
    buildGeoCodePath(peliasPath, address); 
    if(source != null && source.trim().length() > 0) { 
      sb.append(sourceQry);
      sb.append(source);
    } 
    if(layer != null && layer.trim().length() > 0) {
      sb.append(layerQry);
      sb.append(layer);
    }
    if(size > 0) {
      sb.append(sizeQry);
      sb.append(size);
    }
    
    return sb.toString().trim();
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
}
