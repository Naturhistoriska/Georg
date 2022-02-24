package se.nrm.georg.service.logic.coordinates.validation;
  

/**
 *
 * @author idali
 */
public class CoordinatesValidation {

  /**
   * The minimum allowed latitude
   */
  public final float MIN_LATITUDE = Float.valueOf("-90.0000");

  /**
   * The maximum allowed latitude
   */
  public final float MAX_LATITUDE = Float.valueOf("90.0000");

  /**
   * The minimum allowed longitude
   */
  public final float MIN_LONGITUDE = Float.valueOf("-180.0000");

  /**
   * The maximum allowed longitude
   */
  public final float MAX_LONGITUDE = Float.valueOf("180.0000");
  
  private static CoordinatesValidation instance = null;
  
  public static CoordinatesValidation getInstance() {
    synchronized (CoordinatesValidation.class) {
      if (instance == null) {
        instance = new CoordinatesValidation();
      }
    }
    return instance;
  }

  /**
   * A method to validate a latitude value
   *
   * @param latitude the latitude to check is valid
   *
   * @return true if, and only if, the latitude is within the MIN and MAX latitude
   */
  public boolean isValidLatitude(double latitude) {
    return latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE;
  }

  
  /**
   * A method to validate a longitude value
   *
   * @param longitude the longitude to check is valid
   *
   * @return          true if, and only if, the longitude is between the MIN and MAX longitude
   */
  public boolean isValidLongitude(double longitude) {
    return longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE;
  }
  
  public boolean ivValidCoordinates(double lat, double lng) {
    return isValidLatitude(lat) && isValidLongitude(lng);
  }
}
