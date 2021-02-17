package se.nrm.georg.service.logic.coordinates;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;

/**
 *
 * @author idali
 */
@Slf4j
public class CoordinatesTransformation {
  
  private final String wgs84 = "EPSG:4326";
  private final String rt90 = "EPSG:3021";
  private final String sweref99 = "EPSG:3006";
  
  private CRSFactory factory;
  private BasicCoordinateTransform tranform;
  private ProjCoordinate srcCoord;
  private ProjCoordinate dstCoord;
  
  private CoordinateReferenceSystem srcCrs;
  private CoordinateReferenceSystem dstCrs;
  
  public ProjCoordinate transformRT90(double lat, double lng) { 
    return transform(rt90, lat, lng);  
  }
  
  public ProjCoordinate transformSWEREF99(double lat, double lng) {
    return transform(sweref99, lat, lng);
  }
 
  private ProjCoordinate transform(String to, double lat, double lng) {
    srcCoord = new ProjCoordinate(lng, lat); 
    dstCoord = new ProjCoordinate();
    factory = new CRSFactory(); 
    srcCrs = factory.createFromName(wgs84);
    dstCrs = factory.createFromName(to); 
    
    tranform = new BasicCoordinateTransform(srcCrs, dstCrs); 
    return tranform.transform(srcCoord, dstCoord);
  }
}
