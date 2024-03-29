package se.nrm.georg.service.model;
   
/**
 *
 * @author idali
 */
public class CSVBean {
  
  private String id;
  private String sourceLocality;
  private String suggestedLocality;
  private double lat;
  private double lng;
  private String latLngDMS;
  private int uncertainty; 
  private String dataSources;
  private String layers;
  private String country;
          
  public CSVBean() { 
  }
  
  public CSVBean(String id, String sourceLocality) {
    this.id = id;
    this.sourceLocality = sourceLocality;
  }
  
  public CSVBean(String id, String sourceLocality, String suggestedLocality) {
    this.id = id;
    this.sourceLocality = sourceLocality;
    this.suggestedLocality = suggestedLocality; 
  }
 
  
  public CSVBean(String id, String sourceLocality, String suggestedLocality, 
          double lat, double lng, String latLngDMS, int uncertainty, 
          String dataSources, String layers, String country) {
    this.id = id;
    this.sourceLocality = sourceLocality;
    this.suggestedLocality = suggestedLocality;
    this.lat = lat;
    this.lng = lng;
    this.latLngDMS = latLngDMS;
    this.uncertainty = uncertainty; 
    this.dataSources = dataSources;
    this.layers = layers;
    this.country = country;
  }
 
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSourceLocality() {
    return sourceLocality;
  }

  public void setSourceLocality(String sourceLocality) {
    this.sourceLocality = sourceLocality;
  }

  public String getSuggestedLocality() {
    return suggestedLocality;
  }

  public void setSuggestedLocality(String suggestedLocality) {
    this.suggestedLocality = suggestedLocality;
  }

  public String getLatLngDMS() {  
    return latLngDMS;
  }

  public int getUncertainty() {
    return uncertainty;
  }

  public void setUncertainty(int uncertainty) {
    this.uncertainty = uncertainty;
  } 

  public double getLat() {
    return lat;
  } 
  
  public double getLng() {
    return lng;
  } 

  public String getDataSources() {
    return dataSources;
  }

  public String getLayers() {
    return layers;
  } 

  public String getCountry() {
    return country;
  } 
}
