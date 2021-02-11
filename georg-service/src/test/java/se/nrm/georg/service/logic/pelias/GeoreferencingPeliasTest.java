package se.nrm.georg.service.logic.pelias;

import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner; 
import se.nrm.georg.service.logic.InitialProperties;
import se.nrm.georg.service.logic.services.ExternalServices;
import se.nrm.georg.service.model.CSVBean;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class GeoreferencingPeliasTest {
  
  private GeoreferencingPelias instance;
  
  private final String peliasPath = "http://pelias.nrm.se";
  private String jsonString;
  
  @Mock
  private InitialProperties props;
  @Mock
  private PeliasToCSVConvertor convertor; 
  @Mock
  private ExternalServices service; 
  
  public GeoreferencingPeliasTest() {
    
  }
 
  @Before
  public void setUp() {
    jsonString = "{\n"
            + "	\"features\": [{\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.95093,\n"
            + "					59.42804\n"
            + "				],\n"
            + "				\"type\": \"Point\"\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"label\": \"Sollentuna, Sweden\",\n"
            + "				\"id\": \"1125772423\"\n"
            + "			}\n"
            + "		},\n"
            + "		{\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.94845,\n"
            + "					59.428389\n"
            + "				]\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"id\": \"node/35690049\",\n"
            + "				\"label\": \"Sollentuna, Stockholm, Sweden\"\n"
            + "			}\n"
            + "		}\n"
            + "	]\n"
            + "}";
    
    when(props.getPeliasPath()).thenReturn(peliasPath);
    when(service.search(any(String.class))).thenReturn(jsonString); 
    instance = new GeoreferencingPelias(props, convertor, service);
    instance.init();
  }
  
  @After
  public void tearDown() {
    instance = null;
  }

  @Test(expected = NullPointerException.class)
  public void testDefaultConstructor() {
    instance = new GeoreferencingPelias();
    assertNotNull(instance);
    instance.init();
  }

  @Test
  public void testConstructorWithParams() {
    assertNotNull(instance); 
    assertEquals(instance.getPeliasPath(), peliasPath);
  }

  /**
   * Test of init method, of class GeoreferencingPelias.
   */
  @Test
  public void testInit() {
    System.out.println("init");  
    String peliasUrl = instance.getPeliasPath();
    assertNotNull(peliasUrl);
    assertEquals(peliasUrl, peliasPath);
    
    verify(props, times(1)).getPeliasPath();
  }

  /**
   * Test of search method, of class GeoreferencingPelias.
   */
  @Test
  public void testAutoCompleteSearch() {
    System.out.println("search");
    String text = "search text";
    String sources = "swe-virtual-herbarium";
    String layers = "socken";
    String countryCode = "swe";
    int size = 0;
    boolean isAutocompleteSearch = true; 
     
    String result = instance.search(text, sources, layers, countryCode, size, isAutocompleteSearch); 
    assertEquals(jsonString, result); 
    
    verify(service, times(1)).search(any(String.class));
  }
  
  /**
   * Test of search method, of class GeoreferencingPelias.
   */
  @Test
  public void testSearch() {
    System.out.println("search");
    String text = "search text";
    String sources = "swe-virtual-herbarium";
    String layers = "socken";
    String countryCode = "swe";
    int size = 10;
    boolean isAutocompleteSearch = false; 
     
    String result = instance.search(text, sources, layers, countryCode, size, isAutocompleteSearch); 
    assertEquals(jsonString, result); 
    
    verify(service, times(1)).search(any(String.class));
  }

  /**
   * Test of reverseSearch method, of class GeoreferencingPelias.
   */
  @Test
  public void testReverseSearch() {
    System.out.println("reverseSearch");
    double lat = 59.334390; 
    double lng = 17.923360; 
     
    String result = instance.reverseSearch(lat, lng);
    assertEquals(jsonString, result); 
    
    verify(service, times(1)).search(any(String.class));
  }

  /**
   * Test of processBatch method, of class GeoreferencingPelias.
   */
  @Test
  public void testProcessBatch() {
    System.out.println("processBatch");
     
    Map<String, String> map = new HashMap();
    map.put("id1", "Stockholm");
    map.put("id2", "Sollentuna");
    map.put("id3", "Tyreso");
    map.put("id4", "Uppsala");
    map.put("id5", "Linköping"); 
    
    CSVBean bean = new CSVBean("id1", "Stockholm");
    when(convertor.createBean(any(String.class), any(String.class), any(String.class))).thenReturn(bean);
     
    List<CSVBean> result = instance.processBatch(map);
    assertEquals(5, result.size());
    
    verify(service, times(5)).search(any(String.class));
    verify(convertor, times(5)).createBean(any(String.class), any(String.class), any(String.class)); 
  } 
}
