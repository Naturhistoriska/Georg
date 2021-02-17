package se.nrm.georg.service.logic.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;   
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
 
import static org.mockito.Mockito.when; 
import static org.powermock.api.mockito.PowerMockito.mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author idali
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CSVRecord.class}) 
public class CSVParserTest {
  
  private CSVParser instance;
  
  public CSVParserTest() {
  }
 
  
  @Before
  public void setUp() {
    instance = new CSVParser();
  }
  
  @After
  public void tearDown() {
    instance = null;
  }

  /**
   * Test of convertCSVToMap method, of class CSVParser.
   */
  @Test
  public void testConvertCSVToMap() {
    System.out.println("convertCSVToMap");  
    
    List<CSVRecord> list = new ArrayList();
    
    Map<String, String> map1 = new HashMap();
    map1.put("Id", "a1");
    map1.put("SourceLocality", "Sollentuna");
    
    Map<String, String> map2 = new HashMap();
    map2.put("Id", "a2");
    map2.put("SourceLocality", "Stockholm");
    
    Map<String, String> map3 = new HashMap();
    map3.put("Id", "a3");
    map3.put("SourceLocality", "Tyreso");
    
    Map<String, String> map4 = new HashMap();
    map4.put("Ids", "a4");
    map4.put("Locality", "Bromma");
    
    Map<String, String> map5 = new HashMap();
    map5.put("Id", "a5");
    map5.put("Locality", "Linkoping");
    
    Map<String, String> map6 = new HashMap();
    map6.put("Ids", "a6");
    map6.put("SourceLocality", "Uppsala");
     
    CSVRecord mockRecord1 = mock(CSVRecord.class);
    when(mockRecord1.toMap()).thenReturn(map1);
    
    CSVRecord mockRecord2 = mock(CSVRecord.class);
    when(mockRecord2.toMap()).thenReturn(map2); 
    CSVRecord mockRecord3 = mock(CSVRecord.class);
    when(mockRecord3.toMap()).thenReturn(map3); 
    CSVRecord mockRecord4 = mock(CSVRecord.class);
    when(mockRecord4.toMap()).thenReturn(map4); 
    CSVRecord mockRecord5 = mock(CSVRecord.class);
    when(mockRecord4.toMap()).thenReturn(map5); 
    CSVRecord mockRecord6 = mock(CSVRecord.class);
    when(mockRecord4.toMap()).thenReturn(map6);

    list.add(mockRecord1);
    list.add(mockRecord2);
    list.add(mockRecord3);
    list.add(mockRecord4);
    list.add(mockRecord5);
    list.add(mockRecord6);
     
    Map<String, String> result = instance.convertCSVToMap(list);
    assertEquals(3, result.size()); 
    assertTrue(result.containsKey("a1"));
    assertTrue(result.containsKey("a2"));
    assertTrue(result.containsKey("a3"));
    assertFalse(result.containsKey("a4"));
    assertFalse(result.containsKey("a5"));
    assertFalse(result.containsKey("a6"));
    
    assertTrue(result.containsValue("Sollentuna"));
    assertTrue(result.containsValue("Tyreso"));
    assertTrue(result.containsValue("Stockholm"));
    assertFalse(result.containsValue("Bromma"));
    assertFalse(result.containsValue("Linkoping"));
    assertFalse(result.containsValue("Uppsala"));
    
    verify(mockRecord1, times(1)).toMap();
    verify(mockRecord2, times(1)).toMap();
    verify(mockRecord3, times(1)).toMap();
    verify(mockRecord4, times(1)).toMap();
    verify(mockRecord5, times(1)).toMap();
    verify(mockRecord6, times(1)).toMap();
  }
  
}
