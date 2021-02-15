package se.nrm.georg.service.logic.csv;
 
import java.util.HashMap;
import java.util.List; 
import java.util.Map; 
import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j; 
import org.apache.commons.csv.CSVRecord; 
import se.nrm.georg.service.logic.csv.util.CSVHeader;
/**
 *
 * @author idali
 */
@Slf4j
public class CSVParser {

  public CSVParser() {
    
  }
  
  /**
   * 
   * @param records
   * @return Map<String, String>
   */
  public Map<String, String> convertCSVToMap(List<CSVRecord> records) {
    
    Predicate<Map<String, String>> predicate =  
            m -> m.containsKey(CSVHeader.Id.name()) && m.containsKey(CSVHeader.SourceLocality.name());
    
    Map<String, String> map = new HashMap();
    records.stream()
            .map(r -> r.toMap())
            .filter(predicate) 
            .forEach(m -> {
              map.put(m.get(CSVHeader.Id.name()), m.get(CSVHeader.SourceLocality.name()));
            });
    return map;
  }
}
