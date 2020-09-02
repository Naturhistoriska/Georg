package se.nrm.georg.service.logic.coordinates;

import java.math.BigDecimal;  
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
public class CoordinatesBuilder {

  private final String emptySpace = " "; 
  private final String comma = ", ";

    
  private final String wgs84 = "EPSG:4326";
  private final String rt90 = "EPSG:3021";
  private final String sweref99 = "EPSG:3006";
  
  private CRSFactory factory;
  private BasicCoordinateTransform tranform;
  private ProjCoordinate srcCoord;
  private ProjCoordinate dstCoord;
  
  private CoordinateReferenceSystem srcCrs;
  private CoordinateReferenceSystem dstCrs;
  
  public String buildRT90(BigDecimal lat, BigDecimal lng) { 
    transform(rt90, lat.doubleValue(), lng.doubleValue());
    return String.join(comma, String.valueOf(Math.round(dstCoord.y)), 
            String.valueOf(Math.round(dstCoord.x)));  
  }
  
  public String buildSWEREF99(BigDecimal lat, BigDecimal lng) { 
    transform(sweref99, lat.doubleValue(), lng.doubleValue());
    return String.join(comma, String.valueOf(Math.round(dstCoord.y)), 
            String.valueOf(Math.round(dstCoord.x)));  
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
    
  public String buildDD(BigDecimal lat, BigDecimal lng) {
    return String.join(emptySpace, String.valueOf(lat), String.valueOf(lng));
  }

  public String buildDMS(BigDecimal lat, BigDecimal lng) {

    String dmsLat = CoordinatesHelper.getInstance().buildDMS(lat.doubleValue(), true); 
    String dmsLng = CoordinatesHelper.getInstance().buildDMS(lng.doubleValue(), false);
    return String.join(emptySpace, dmsLat, dmsLng);
  }
  
  public String buildDDM(BigDecimal lat, BigDecimal lng) {
    String ddmLat = CoordinatesHelper.getInstance().buildDDM(lat.doubleValue(), true);
    String ddmLng = CoordinatesHelper.getInstance().buildDDM(lng.doubleValue(), false);
    return String.join(emptySpace, ddmLat, ddmLng);
  } 
}
