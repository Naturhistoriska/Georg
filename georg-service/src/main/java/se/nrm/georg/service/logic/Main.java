/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.georg.service.logic;

//import org.locationtech.proj4j.BasicCoordinateTransform;
//import org.locationtech.proj4j.CRSFactory;
//import org.locationtech.proj4j.CoordinateReferenceSystem;
//import org.locationtech.proj4j.ProjCoordinate;

/**
 *
 * @author idali
 */
public class Main {
  
  public static void main(String[] args) {
    
    // EPSG:4121 EPSG:4326   lat 57.712951, lng 22.390137
    
    // WGS 84 - WGS84 - World Geodetic System 1984, used in GPS
    // EPSG:4326
    
    // RT90  -- 3021
    // SWEREF99 -- 3006
    
    // https://epsg.io/
    // https://www.spatialreference.org/ref/epsg/3021/ 
    // https://www.lantmateriet.se/sv/Sjalvservice/koordinattransformation/




    
//    CRSFactory factory = new CRSFactory(); 
//    CoordinateReferenceSystem srcCrs = factory.createFromName("EPSG:4326");
//    CoordinateReferenceSystem dstCrs = factory.createFromName("EPSG:3021");
//    
//    BasicCoordinateTransform tranform = new BasicCoordinateTransform(srcCrs, dstCrs);
//    
//    ProjCoordinate srcCoord = new ProjCoordinate(22.390137, 57.712951);
// 
//    
//    ProjCoordinate dstCoord = new ProjCoordinate();
//    tranform.transform(srcCoord, dstCoord);
//    
//    System.out.println("dstCoord : " + dstCoord);
//     
//    System.out.println("dstCoord : " + dstCoord.x + " --- " + dstCoord.y);
  }
  
}
