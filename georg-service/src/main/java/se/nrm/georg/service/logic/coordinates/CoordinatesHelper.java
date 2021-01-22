package se.nrm.georg.service.logic.coordinates;

import com.peertopark.java.geocalc.DMSCoordinate; 
import java.text.DecimalFormat;
import java.util.StringJoiner;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author idali
 */
@Slf4j
public class CoordinatesHelper {
    
  private final String emptyString = ""; 
  private final String emptySpace = " ";  
  
  private final String degreeSign = "°";
  private final String minuteSign = "'";
  private final String secondSign = "\"";

  private final String south = " S";
  private final String north = " N";
  private final String west = " W";
  private final String east = " E";
  
  private final String n = "n";
  private final String s = "s";
  private final String e = "e";
  private final String w = "w";
  
  private final DecimalFormat df = new DecimalFormat("00.0");
  private final DecimalFormat df1 = new DecimalFormat("###.000"); 

  private static CoordinatesHelper instance = null;

  public static CoordinatesHelper getInstance() {
    synchronized (CoordinatesHelper.class) {
      if (instance == null) {
        instance = new CoordinatesHelper();
      }
    }
    return instance;
  }
  
  public String buildDMS(double lat, double lng) {  
    StringBuilder sb = new StringBuilder();
    sb.append(buildDMS(lat, true));
    sb.append(" ");
    sb.append(buildDMS(lng, false)); 
    return sb.toString(); 
  }
  
  public String buildDMS(double value, boolean isLat) { 
    
    double absValue = Math.abs(value);
    int degree = getDegree(absValue);
    double dbMinutes = getMinutes(absValue, degree);
    int minutes = (int) dbMinutes; 
    double dbSecond = getSeconds(dbMinutes); 
//    int second = (int) Math.round(dbSecond);  
    
    return buildCoordinatesString(String.valueOf(degree), 
            String.valueOf(minutes), df.format(dbSecond), 
            getDirection(value, isLat)); 
  }
  
  public String buildDDM(double value, boolean isLat) {

    double absValue = Math.abs(value);
    int degree =  getDegree(absValue);
    double minutes = getMinutes(absValue, degree);
//    BigDecimal bd = new BigDecimal(minutes).setScale(6, RoundingMode.HALF_UP);
    
    return buildCoordinatesString(String.valueOf(degree), 
            df1.format(minutes), null, getDirection(value, isLat)); 
  } 
  
  public boolean isDD(String coordinates) {
    return !coordinates.contains(degreeSign);
  }
   
  public boolean isDMS(String coordinates) {
    return coordinates.contains(secondSign); 
  }
   
  public boolean isDDM(String coordinatees) {
    return coordinatees.contains(minuteSign) && !isDMS(coordinatees);
  }
    
  public String getLatString(String coordinates) {
    return coordinates.trim().contains(n) ? 
            StringUtils.substringBefore(coordinates, n).trim() : 
            StringUtils.substringBefore(coordinates, s).trim();
  }
  
  public String getlngString(String coordinates) {
    log.info("coordinates : {}", coordinates);
    if(coordinates.contains(e) && coordinates.contains(n)) {
      return StringUtils.substringBetween(coordinates, n, e).trim();
    }
    if(coordinates.contains(e) && coordinates.contains(s)) {
      return StringUtils.substringBetween(coordinates, s, e).trim();
    }
    if(coordinates.contains(w) && coordinates.contains(n)) {
      return StringUtils.substringBetween(coordinates, n, w).trim();
    }
    if(coordinates.contains(w) && coordinates.contains(s)) {
      return StringUtils.substringBetween(coordinates, s, w).trim();
    } 
    return null;
  }
  
  public Double[] getLatAndLng(String coordinates) { 
    Double lat = 0.0;
    Double lng = 0.0;
    if(CoordinatesHelper.getInstance().isDD(coordinates)) {
      String[] coors = coordinates.split(emptySpace);
      lat = Double.valueOf(coors[0]); 
      lng = coors.length == 2 ? Double.valueOf(coors[1]) : Double.valueOf(coors[2]);
    } else {
      String strLat = getLatString(coordinates.toLowerCase());
      String strLng = getlngString(coordinates.toLowerCase()); 
      if (isDMS(coordinates)) {
        lat = convertDMSToDD(strLat, coordinates.toLowerCase().contains(s));
        lng = convertDMSToDD(strLng, coordinates.toLowerCase().contains(w));
      } else if (CoordinatesHelper.getInstance().isDDM(coordinates)) { 
        lat = convertDDMToDD(strLat, coordinates.toLowerCase().contains(s));
        lng = convertDDMToDD(strLng, coordinates.toLowerCase().contains(w));  
      } 
    }  
    return Stream.of(lat, lng).toArray(Double[]::new);   
  }
 
  private String buildCoordinatesString(String degree, String minutes, String seconds, String direction) {
    StringJoiner joiner = new StringJoiner(emptyString);
    joiner.add(degree);
    joiner.add(degreeSign);
    joiner.add(minutes);
    joiner.add(minuteSign);
    if(seconds != null) {
      joiner.add(seconds);  
      joiner.add(secondSign);
    } 
    joiner.add(direction); 
    return joiner.toString();  
  }
   
  private int getDegrees(String value) {
    log.info("getDegrees : {}", value);
    
    String strDegree = StringUtils.substringBefore(value, degreeSign);
    if(strDegree != null) {
      return Integer.parseInt(strDegree.trim());
    }
    return 0;
  }
 
  private int getDegree(double value) {
    return (int) value;
  }
   
  private int getMinutes(String value) {
    String strMinutes = StringUtils.substringBetween(value, degreeSign, minuteSign);
    if(strMinutes != null) {
      return Integer.parseInt(strMinutes.trim());
    }
    return 0;
  }
   
  private double getMinutes(double value, int degree) {
    return (value - degree) * 60;
  }
   
  private double getMinutesInDouble(String value) throws NumberFormatException  { 
    return Double.valueOf(StringUtils.substringBetween(value, degreeSign, minuteSign).trim()); 
  } 
  
  private double getSeconds(String value) {
    String strSecond =  StringUtils.substringBetween(value, minuteSign, secondSign);
    if(strSecond != null) {
      return Double.parseDouble(strSecond.trim());
    }
    return 0.0;
  } 
 
  private double getSeconds(double dbMinutes) {  
    return (dbMinutes - (int)dbMinutes) * 60; 
  } 
 
  private String getDirection(double value, boolean isLat) {
    return value < 0 ? isLat ? south : west : isLat ? north : east;
  }
  
  
  private double convertDDMToDD(String value, boolean isSouthOrWest) throws NumberFormatException {
    int degrees = getDegrees(value); 
    double minutes = getMinutesInDouble(value); 
    double dd = degrees + minutes / 60;
    return isSouthOrWest ? (-1) * dd : dd;
  }

  private double convertDMSToDD(String value, boolean isSouthOrWest) {
    int degrees = getDegrees(value);
    int minutes = getMinutes(value);
    double seconds = getSeconds(value);
       
//    double decimal = ((minutes * 60) + seconds);
//    return degrees + decimal / 3600;  
    if(isSouthOrWest) {
      degrees = degrees * (-1);
    }

    DMSCoordinate dmsCoord = new DMSCoordinate(degrees, minutes, seconds);
    return dmsCoord.getDecimalDegrees();
  }


}
