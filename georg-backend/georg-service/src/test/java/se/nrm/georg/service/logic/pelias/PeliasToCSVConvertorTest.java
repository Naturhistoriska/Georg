package se.nrm.georg.service.logic.pelias;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import org.mockito.runners.MockitoJUnitRunner;
import se.nrm.georg.service.model.CSVBean;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class PeliasToCSVConvertorTest {

  private PeliasToCSVConvertor instance;
  
  private String jsonString;
  private JSONObject json;
  private JSONObject feature;
  private JSONObject properties;
  private JSONArray coordinates;
  private String label;

  @Mock
  private PeliasParser parser;
  @Mock
  private UncertaintyParser uncertainty;

  public PeliasToCSVConvertorTest() {
  }

  @Before
  public void setUp() { 
    jsonString ="{\n"
            + "	\"features\": [{\n"
            + "			\"bbox\": [\n"
            + "				17.922861,\n"
            + "				59.333256,\n"
            + "				17.924899,\n"
            + "				59.335798\n"
            + "			],\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.9167,\n"
            + "					59.35\n"
            + "				]\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"label\": \"Bromma, Stockholm, Sweden\",\n"
            + "				\"addendum\": {\n"
            + "					\"georg\": {\n"
            + "						\"coordinateUncertaintyInMeters\": 2000\n"
            + "					}\n"
            + "				}\n"
            + "			}\n"
            + "		},\n"  
            + "		{\n"
            + "			\"geometry\": {\n"
            + "				\"coordinates\": [\n"
            + "					17.912401,\n"
            + "					59.347716\n"
            + "				]\n"
            + "			},\n"
            + "			\"properties\": {\n"
            + "				\"label\": \"Norra Ängby, Bromma, Stockholm [RT90:6582668/1619879], Stockholm, Sweden\"\n"
            + "			}\n"
            + "		}\n"
            + "	],\n"
            + "	\"type\": \"FeatureCollection\"\n"
            + "}";
    json = new JSONObject(jsonString);
    feature = json.getJSONArray("features").getJSONObject(0);
    properties = feature.getJSONObject("properties");
    coordinates = feature.getJSONObject("geometry").getJSONArray("coordinates");
    label = properties.getString("label");
    
    when(parser.stringToJson(jsonString)).thenReturn(json); 
    when(parser.getFirstFeature(json)).thenReturn(feature); 
    
    instance = new PeliasToCSVConvertor(parser, uncertainty);
  }
  
  @After
  public void tearDown() {
    instance = null;
  }
  
  
  @Test(expected = NullPointerException.class)
  public void testDefaultConstructor() {
    instance = new PeliasToCSVConvertor();
    assertNotNull(instance);
    instance.createBean(jsonString, "1233", "Sollentuna");
  }

  @Test
  public void testConstructorWithParams() {
    assertNotNull(instance);
    when(parser.getFirstFeature(json)).thenReturn(null);  
    CSVBean result = instance.createBean(jsonString, "1233", "Sollentuna");
    assertNotNull(result);
  }

  /**
   * Test of createBean method, of class PeliasToCSVConvertor.
   */
  @Test
  public void testCreateBean() {
    System.out.println("createBean"); 
    
    String id = "125772423";
    String locality = "Sollentuna, Sweden";   
    when(parser.getProperties(feature)).thenReturn(properties);
    when(parser.getLabel(properties)).thenReturn(label);
    when(parser.getCoordinates(feature)).thenReturn(coordinates);  
    when(uncertainty.getUncertainty(feature, properties, parser)).thenReturn(2000); 
    
    CSVBean result = instance.createBean(jsonString, id, locality);
    
    assertEquals(id, result.getId()); 
    assertEquals(locality, result.getSourceLocality()); 
    assertEquals(label, result.getSuggestedLocality()); 
    assertEquals(2000, result.getUncertainty()); 
    
    verify(parser, times(1)).stringToJson(jsonString);
    verify(parser, times(1)).getFirstFeature(any(JSONObject.class));
    verify(parser, times(1)).getProperties(feature);
    verify(parser, times(1)).getLabel(properties);
    verify(parser, times(1)).getSource(properties);
    verify(parser, times(1)).getLayer(properties);
    verify(parser, times(1)).getCoordinates(feature);
    verify(uncertainty, times(1)).getUncertainty(feature, properties, parser);
  }
  
   /**
   * Test of createBean method, of class PeliasToCSVConvertor.
   */
  @Test
  public void testCreateBeanWithNoSearchResult() {
    System.out.println("createBean"); 
    
    String id = "125772423";
    String locality = "Sollentuna, Sweden";  
     
    when(parser.getFirstFeature(json)).thenReturn(null);  
    CSVBean result = instance.createBean(jsonString, id, locality);
    
    assertEquals(id, result.getId()); 
    assertEquals(locality, result.getSourceLocality()); 
    assertEquals("No result", result.getSuggestedLocality());  
    assertEquals(0, result.getUncertainty());
    
    verify(parser, times(1)).stringToJson(jsonString);
    verify(parser, times(1)).getFirstFeature(any(JSONObject.class));
    verify(parser, never()).getProperties(feature);
    verify(parser, never()).getLabel(properties);
    verify(parser, never()).getCoordinates(feature);
    verify(uncertainty, never()).getUncertainty(feature, properties, parser);
  }
  
}
