package se.nrm.georg.service.util;

/**
 *
 * @author idali
 */
public class Util {
   
  private final String searchGeoCode = "search?text=";
  private final String reverseGeoCode = "reverse?";
  private final String pointLat = "point.lat=";
  private final String pointLon = "point.lon=";
  private final String and = "&";
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
  
  public String buildGeoCodePath(String peliasPath, String address) { 
    sb = new StringBuilder();
    sb.append(peliasPath);
    sb.append(searchGeoCode);
    sb.append(address); 
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
