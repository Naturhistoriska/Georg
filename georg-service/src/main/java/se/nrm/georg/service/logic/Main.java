/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.georg.service.logic;

//import org.locationtech.proj4j.BasicCoordinateTransform;
 
//import java.math.BigDecimal;
//import java.math.RoundingMode; 
//import java.util.regex.Pattern;
//import com.peertopark.java.geocalc.Coordinate;
//import com.peertopark.java.geocalc.DMSCoordinate;
//import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.DMSCoordinate;
import java.math.BigDecimal;
import org.locationtech.proj4j.BasicCoordinateTransform;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;

/**
 *
 * @author idali
 */
public class Main {
  
  private final static String degree = "°";
  private final static String minute = "'";
  
  private final static String secondSign = "\"";

  public static void main(String[] args) {

//    String value = "59°21'0\" N 17°56'0\" E";
//    String value1 = "59°21.049680' N 17°56.335620' E";

      String string = "21.049680, 56.335620";
      string = string.replace(",", "");
      System.out.println("what..." + string.split(" ").length);
//    
//    System.out.println("what.." + value.contains(secondSign));
//    System.out.println("what 1.." + value1.contains(secondSign));
//    
//   
//    System.out.println("start....");
//
//    int degrees = -38;
//    int minutes = -20;
//    int seconds = -40;
//    double min = ((minutes * 60) + seconds);
//    System.out.println("min..." + min);
////    
//    System.out.println("why..." + min / 3600);
//    double decimal = ((minutes * 60) + seconds) / 3600;
//    System.out.println("dec..." + decimal);
//    double answer = degrees + decimal; //  
////    
//    System.out.println("answer..." + answer); 
    
//    DMSCoordinate dmsCoord = new DMSCoordinate(-40, 45, 4);
//    double dd = dmsCoord.getDecimalDegrees();
//    System.out.println("dd..." + dd);
//
//    System.out.println("dd...." + dd);
//    DegreeCoordinate degree = new DegreeCoordinate(59.216700);
 
    
     
//    
//    double lat = 59.17;
//    double lng = 18.6011;
//    int d = (int)lat;  // Truncate the decimals
//    double t1 = (lat - d) * 60;
//    int m = (int)t1;
//    double s = (t1 - m) * 60;
//    
//    String strLat = d + degree + m + minute + s;
//    System.out.println("strLat ..." + strLat);
    
     
    
//
//        double input = -3.14159265359;
//        System.out.println("double : " + input);
//
//        BigDecimal bd = new BigDecimal(input).setScale(6, RoundingMode.HALF_UP);
//        double newInput = bd.doubleValue();
//
//        System.out.println("double : " + newInput);


    
    
    
     
    // EPSG:4121 EPSG:4326   lat 57.712951, lng 22.390137
    
    // WGS 84 - WGS84 - World Geodetic System 1984, used in GPS
    // EPSG:4326
    
    // RT90  -- 3021
    // SWEREF99 -- 3006    sweref99
    
    // https://epsg.io/
    // https://www.spatialreference.org/ref/epsg/3021/ 
    // https://www.lantmateriet.se/sv/Sjalvservice/koordinattransformation/
// 1985890, 811341

//59.350828 17.938927



// 1985890, 811341
 
// 3006 --- 667092 --- 6582806 

// 6583063, 1621377


// test: 17.938927, 59.350828
    
//    CRSFactory factory = new CRSFactory(); 
//    CoordinateReferenceSystem srcCrs = factory.createFromName("EPSG:4326");
//    CoordinateReferenceSystem dstCrs = factory.createFromName("EPSG:3006");
//    
//    BasicCoordinateTransform tranform = new BasicCoordinateTransform(srcCrs, dstCrs);
//    
//    ProjCoordinate srcCoord = new ProjCoordinate(17.938927, 59.350828);
// 
//    
//    ProjCoordinate dstCoord = new ProjCoordinate();
//    ProjCoordinate result = tranform.transform(srcCoord, dstCoord);
//    
//    System.out.println("dstCoord : " + dstCoord);
//     
//    System.out.println("dstCoord RT90 : " + Math.round(result.x)  + " --- " + Math.round(result.y));
    
    
//    dstCrs = factory.createFromName("EPSG:3006");
//    tranform = new BasicCoordinateTransform(srcCrs, dstCrs);
//    
//    tranform.transform(srcCoord, dstCoord);
//    
//    System.out.println("dstCoord : " + dstCoord);
//     
//    System.out.println("dstCoord SWEREF99 : " + Math.round(dstCoord.x)  + " --- " + Math.round(dstCoord.y));
    
  }
  
}

// 6583063, 1621377
