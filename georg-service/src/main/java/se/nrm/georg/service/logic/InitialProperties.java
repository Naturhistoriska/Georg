package se.nrm.georg.service.logic;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

/**
 *
 * @author idali
 */
@ApplicationScoped
@Slf4j
public class InitialProperties implements Serializable {

  private final static String CONFIG_INITIALLISING_ERROR = "Property not initialized";
  
  private String peliasPath;
  
  public InitialProperties() {
  }
  
  @Inject
  public InitialProperties(@ConfigurationValue("swarm.pelias.path") String peliasPath) { 
    this.peliasPath = peliasPath; 
    log.info("test injection : {} ", peliasPath);
  }
  
  public String getPeliasPath() {
    if (peliasPath == null) {
      throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
    }
    return peliasPath;
  }  
}
