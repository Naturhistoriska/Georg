package se.nrm.georg.service.model;

/**
 *
 * @author idali
 */
public class CSVBean {
  
  private String id;
  private String sourceLocality;
  private String suggestedLocality;
  private String latLngDMS;
  private int uncertainty;
  
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
  
  public CSVBean(String id, String sourceLocality, String suggestedLocality, String latLngDMS) {
    this.id = id;
    this.sourceLocality = sourceLocality;
    this.suggestedLocality = suggestedLocality;
    this.latLngDMS = latLngDMS; 
  }
  
  public CSVBean(String id, String sourceLocality, String suggestedLocality, 
          String latLngDMS, int uncertainty) {
    this.id = id;
    this.sourceLocality = sourceLocality;
    this.suggestedLocality = suggestedLocality;
    this.latLngDMS = latLngDMS;
    this.uncertainty = uncertainty;
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

  public void setLatLngDMS(String latLngDMS) {
    this.latLngDMS = latLngDMS;
  }

  public int getUncertainty() {
    return uncertainty;
  }

  public void setUncertainty(int uncertainty) {
    this.uncertainty = uncertainty;
  } 
}
