package se.nrm.georg.service.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import org.apache.commons.csv.CSVRecord;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import se.nrm.georg.service.logic.coordinates.CoordinatesParser;
import se.nrm.georg.service.logic.csv.CSVParser;
import se.nrm.georg.service.logic.file.FileHandler;
import se.nrm.georg.service.logic.pelias.GeoreferencingPelias;
import se.nrm.georg.service.model.CSVBean;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class GeorgLogicTest {

  private GeorgLogic instance;
  private final String resultString = "test result 1";
  private final String countryCode = "SWE";
  private final String searchText = "Sollentuna";
  private Map<String, String> map;
  private List<CSVBean> beans;
  
  private final String typeJson = "json"; 
  private final String typeCsv = "csv"; 
  private final String tempFilePath = "/usr/temp/file.json";
   

  @Mock
  private FileHandler mockFile;
  @Mock
  private GeoreferencingPelias mockPelias;
  @Mock
  private CoordinatesParser mockCoordinates;
  @Mock
  private CSVParser mockCsv;
  @Mock
  private InputPart token;
  @Mock
  private List<CSVRecord> mockList;

  public GeorgLogicTest() {
  }

  @Before
  public void setUp() throws IOException {
 
    map = new HashMap();
    map.put("id1", "Stockholm");
    map.put("id2", "Sollentuna");
    map.put("id3", "Tyreso");
    map.put("id4", "Uppsala");
    map.put("id5", "Linköping");

    beans = new ArrayList();
    beans.add(new CSVBean("id1", "Stockholm"));
    beans.add(new CSVBean("id2", "Sollentuna"));
    beans.add(new CSVBean("id3", "Tyreso"));
    beans.add(new CSVBean("id4", "Uppsala"));
    beans.add(new CSVBean("id5", "Linköping"));
    
    
    InputStream in = mock(InputStream.class); 
    token = mock(InputPart.class);
    when(token.getBody(InputStream.class, null)).thenReturn(in);
 
    when(mockList.size()).thenReturn(5);
    when(mockFile.readCsv(any(InputStream.class))).thenReturn(mockList);
    when(mockFile.createFile(beans, typeJson)).thenReturn(tempFilePath);
    when(mockFile.createFile(beans, typeCsv)).thenReturn(tempFilePath);
    when(mockCsv.convertCSVToMap(mockList)).thenReturn(map);
    when(mockPelias.processBatch(map)).thenReturn(beans);

    when(mockPelias.search(any(String.class), any(String.class), any(String.class), any(String.class), any(Integer.class), any(Boolean.class))).thenReturn(resultString);
    when(mockPelias.reverseSearch(any(Double.class), any(Double.class))).thenReturn(resultString);

    when(mockCoordinates.addCoordinatesTransformation(any(String.class))).thenReturn(resultString);
    when(mockCoordinates.addCoordinatesTransformation(any(String.class), any(Double.class), any(Double.class))).thenReturn(resultString);
    when(mockCoordinates.addCoordinatesTransformation(any(String.class), any(Double.class), any(Double.class))).thenReturn(resultString);
    when(mockCoordinates.addCoordinatesTransformationNewPlace(any(String.class), any(Double.class), any(Double.class))).thenReturn(resultString);

    instance = new GeorgLogic(mockCoordinates, mockCsv, mockFile, mockPelias);
  }

  @After
  public void tearDown() {
    instance = null;
    map = null;
  }

  @Test(expected = NullPointerException.class)
  public void testDefaultConstructor() {
    instance = new GeorgLogic();
    assertNotNull(instance);
    instance.searchAddress(searchText, null, null, countryCode, 10);
  }

  @Test
  public void testConstructorWithParams() {
    assertNotNull(instance);
    instance.searchAddress(searchText, null, null, countryCode, 10);
    assertNotNull(instance);
  }

  /**
   * Test of processBatch method, of class GeorgLogic.
   *
   * @throws java.io.IOException
   */
  @Test
  public void testProcessBatchWithJsonOut() throws IOException {
    System.out.println("testProcessBatchWithJsonOut");
 
    String result = instance.processBatch(token, typeJson);
    assertEquals(tempFilePath, result);

    verify(mockFile, times(1)).readCsv(any(InputStream.class));
    verify(mockFile, times(1)).createFile(beans, typeJson);

    verify(mockCsv, times(1)).convertCSVToMap(mockList);
    verify(mockPelias, times(1)).processBatch(map);
  }

  /**
   * Test of processBatch method, of class GeorgLogic.
   *
   * @throws java.io.IOException
   */
  @Test
  public void testProcessBatchWithCSVOut() throws IOException {
    System.out.println("testProcessBatchWithCSVOut");
   
    String result = instance.processBatch(token, typeCsv);
    assertEquals(tempFilePath, result); 
    verify(mockFile, times(1)).readCsv(any(InputStream.class));
    verify(mockFile, times(1)).createFile(beans, typeCsv);

    verify(mockCsv, times(1)).convertCSVToMap(mockList);
    verify(mockPelias, times(1)).processBatch(map);
  }

  /**
   * Test of processBatch method, of class GeorgLogic.
   *
   * @throws java.io.IOException
   */
  @Test
  public void testProcessBatchWithInvalidCSV() throws IOException {
    System.out.println("testProcessBatchWithInvalidCSV");

    map = new HashMap();
    when(mockCsv.convertCSVToMap(mockList)).thenReturn(map);
    String result = instance.processBatch(token, typeCsv);
    assertNull(result);

    verify(mockFile, times(1)).readCsv(any(InputStream.class));
    verify(mockFile, never()).createFile(beans, typeCsv);

    verify(mockCsv, times(1)).convertCSVToMap(mockList);
    verify(mockPelias, never()).processBatch(map);
  }

  /**
   * Test of processBatch method, of class GeorgLogic.
   *
   * @throws java.io.IOException
   */
  @Test
  public void testProcessBatchThrowException() throws IOException {
    System.out.println("testProcessBatchThrowException");
 
    when(token.getBody(InputStream.class, null)).thenThrow(IOException.class); 
    String result = instance.processBatch(token, typeCsv);
    assertNull(result);

    verify(mockFile, never()).readCsv(any(InputStream.class));
    verify(mockFile, never()).createFile(beans, typeCsv); 
    verify(mockCsv, never()).convertCSVToMap(mockList);
    verify(mockPelias, never()).processBatch(map);
  }

  /**
   * Test of searchAddress method, of class GeorgLogic.
   */
  @Test
  public void testSearchAddress() {
    System.out.println("searchAddress");

    String address = "stockholm";
    String source = "GBIF";
    String layer = "myLayer";

    int size = 0;

    String result = instance.searchAddress(address, source, layer, countryCode, size);
    assertEquals(resultString, result);

    verify(mockPelias, times(1)).search(address, source, layer, countryCode, size, false);
    verify(mockCoordinates, times(1)).addCoordinatesTransformation(any(String.class));
  }

  /**
   * Test of searchAddress method, of class GeorgLogic.
   */
  @Test
  public void testRunAutocompleteSearch() {
    System.out.println("searchAddress");

    String address = "stockholm";
    String source = "GBIF";
    String layer = "myLayer";
    int size = 0;

    String result = instance.runAutocompleteSearch(address, source, layer, countryCode, size);
    assertEquals(resultString, result);

    verify(mockPelias, times(1)).search(address, source, layer, countryCode, size, true);
    verify(mockCoordinates, times(1)).addCoordinatesTransformation(any(String.class));
  }

  /**
   * Test of reverseSearch method, of class GeorgLogic.
   */
  @Test
  public void testReverseSearch() {
    System.out.println("reverseSearch");
    double lat = 59.6;
    double lon = 18.7;
    String result = instance.reverseSearch(lat, lon);
    assertEquals(resultString, result);

    verify(mockPelias, times(1)).reverseSearch(lat, lon);
    verify(mockCoordinates, times(1)).addCoordinatesTransformationNewPlace(any(String.class), any(Double.class), any(Double.class));
  }

  /**
   * Test of coordinatesSearch method, of class GeorgLogic.
   */
  @Test
  public void testCoordinatesSearchDDWithComma() {
    System.out.println("testCoordinatesSearchDDWithComma");
    String coordinatesString = "61.67051, 5.32276";

    String result = instance.coordinatesSearch(coordinatesString);
    assertEquals(resultString, result);

    verify(mockPelias, times(1)).reverseSearch(61.67051, 5.32276);
    verify(mockCoordinates, times(1)).addCoordinatesTransformation(any(String.class), any(Double.class), any(Double.class));
  }

  @Test
  public void testCoordinatesSearchDDWithSpace() {
    System.out.println("testCoordinatesSearchDDWithSpace");

    String coordinatesString = "61.67051 5.32276";
    String result = instance.coordinatesSearch(coordinatesString);
    assertEquals(resultString, result);

    verify(mockPelias, times(1)).reverseSearch(61.67051, 5.32276);
    verify(mockCoordinates, times(1)).addCoordinatesTransformation(any(String.class), any(Double.class), any(Double.class));
  }

  @Test
  public void testCoordinatesSearchDMS() {
    System.out.println("testCoordinatesSearchDMS");

    String coordinatesString = "61°40'14\" N 5°19'22\" E";
    String result = instance.coordinatesSearch(coordinatesString);
    System.out.println("result.." + result);
    assertEquals(resultString, result);

    verify(mockPelias, times(1)).reverseSearch(any(Double.class), any(Double.class));
    verify(mockCoordinates, times(1)).addCoordinatesTransformation(any(String.class), any(Double.class), any(Double.class));
  }

  @Test
  public void testCoordinatesSearchDMSWithComma() {
    System.out.println("testCoordinatesSearchDMSWithComma");

    String coordinatesString = "61°40'14\" N, 5°19'22\" E";
    String result = instance.coordinatesSearch(coordinatesString);
    System.out.println("result.." + result);
    assertEquals(resultString, result);

    verify(mockPelias, times(1)).reverseSearch(any(Double.class), any(Double.class));
    verify(mockCoordinates, times(1)).addCoordinatesTransformation(any(String.class), any(Double.class), any(Double.class));
  }

  @Test
  public void testCoordinatesSearchDDM() {
    System.out.println("testCoordinatesSearchDDM");

    String coordinatesString = "61°40.230600' N 5°19.365600' E";
    String result = instance.coordinatesSearch(coordinatesString);
    assertEquals(resultString, result);

    verify(mockPelias, times(1)).reverseSearch(any(Double.class), any(Double.class));
    verify(mockCoordinates, times(1)).addCoordinatesTransformation(any(String.class), any(Double.class), any(Double.class));
  }

  @Test
  public void testCoordinatesSearchDDMWithComma() {
    System.out.println("testCoordinatesSearchDDMWithComma");

    String coordinatesString = "61°40.230600', N 5°19.365600' E";
    String result = instance.coordinatesSearch(coordinatesString);
    assertEquals(resultString, result);

    verify(mockPelias, times(1)).reverseSearch(any(Double.class), any(Double.class));
    verify(mockCoordinates, times(1)).addCoordinatesTransformation(any(String.class), any(Double.class), any(Double.class));
  }
}
