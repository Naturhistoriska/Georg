package se.nrm.georg.service.logic.coordinates;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.StringJoiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author idali
 */
@Slf4j
public class CoordinatesHelper {
    
  private final String emptyString = ""; 
  
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

  private static CoordinatesHelper instance = null;

  public static CoordinatesHelper getInstance() {
    synchronized (CoordinatesHelper.class) {
      if (instance == null) {
        instance = new CoordinatesHelper();
      }
    }
    return instance;
  }
  
  public String buildDMS(double value, boolean isLat) { 
    
    double absValue = Math.abs(value);
    int degree = getDegree(absValue);
    double dbMinutes = getMinutes(absValue, degree);
    int minutes = (int) dbMinutes; 
    double dbSecond = getSeconds(dbMinutes); 
    int second = (int) Math.round(dbSecond);  
    
    return buildCoordinatesString(String.valueOf(degree), 
            String.valueOf(minutes), String.valueOf(second), 
            getDirection(value, isLat)); 
  }
  
  public String buildDDM(double value, boolean isLat) {

    double absValue = Math.abs(value);
    int degree =  getDegree(absValue);
    double minutes = getMinutes(absValue, degree);
    BigDecimal bd = new BigDecimal(minutes).setScale(6, RoundingMode.HALF_UP);
    
    return buildCoordinatesString(String.valueOf(degree), 
            String.valueOf(bd), null, getDirection(value, isLat)); 
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
  
  
  public int getDegrees(String value) {
    log.info("getDegrees : {}", value);
    return Integer.parseInt(StringUtils.substringBefore(value, degreeSign).trim()) ;
  }
     
  public int getMinutes(String value) {
    return Integer.parseInt(StringUtils.substringBetween(value, degreeSign, minuteSign).trim());
  }
    
  public int getSeconds(String value) {
    return Integer.parseInt(StringUtils.substringBetween(value, minuteSign, secondSign).trim());
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
 
  private int getDegree(double value) {
    return (int) value;
  }
   
  private double getMinutes(double value, int degree) {
    return (value - degree) * 60;
  }
 
  private double getSeconds(double dbMinutes) {  
    return (dbMinutes - (int)dbMinutes) * 60; 
  } 
 
  private String getDirection(double value, boolean isLat) {
    return value < 0 ? isLat ? south : west : isLat ? north : east;
  }

}
