package se.nrm.georg.service.logic;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Slf4j
public class GeorgLogic implements Serializable {
  
  @Inject  
  private InitialProperties props; 
  @Inject
  private AutocompleteSearch autocompleteSearch;
  private String peliasPath; 
   
  public GeorgLogic() {
    
  }
  
  @PostConstruct 
  public void init() {
    peliasPath = props.getPeliasPath();   
  }
  
  public String runAutocompleteSearch(String text, int size) {
    return autocompleteSearch.search(text, size, peliasPath);
  }
}
