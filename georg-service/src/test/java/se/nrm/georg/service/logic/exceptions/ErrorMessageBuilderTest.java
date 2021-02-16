package se.nrm.georg.service.logic.exceptions;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import se.nrm.georg.service.logic.json.JsonParser;

/**
 *
 * @author idali
 */
@RunWith(MockitoJUnitRunner.class)
public class ErrorMessageBuilderTest {

  private ErrorMessageBuilder instance;

  @Mock
  private JsonParser parser;

  public ErrorMessageBuilderTest() {
  }

  @Before
  public void setUp() { 
    instance = new ErrorMessageBuilder(parser);
  }

  @After
  public void tearDown() {
    instance = null;
  }
   
  @Test(expected = NullPointerException.class)
  public void testDefaultConstructor() {
    instance = new ErrorMessageBuilder();
    assertNotNull(instance);
    instance.buildInvalidCSVFileMessage();
  }

  /**
   * Test of buildInvalidCoordinatesMessage method, of class
   * ErrorMessageBuilder.
   */
  @Test
  public void testBuildInvalidCoordinatesMessage() {
    System.out.println("buildInvalidCoordinatesMessage"); 
     
    String invalidCoordinates = "{\n"
            + "	\"msgKey\": \"Invalid coordinates\"\n"
            + "}";
    JSONObject invalidCoordinatesJson = new JSONObject(invalidCoordinates);

    String invalidCoordinatesError = "{\n"
            + "	\"error\": {\n"
            + "		\"msgKey\": \"Invalid coordinates\"\n"
            + "	}\n"
            + "}";
    JSONObject invalidCoordinatesErrorJson = new JSONObject(invalidCoordinatesError);

    when(parser.buildJsonObject(any(String.class), any(String.class))).thenReturn(invalidCoordinatesJson);
    when(parser.buildJsonObject(any(String.class), eq(invalidCoordinatesJson))).thenReturn(invalidCoordinatesErrorJson);
 
    String expResult = "{\"error\":{\"msgKey\":\"Invalid coordinates\"}}";
    String result = instance.buildInvalidCoordinatesMessage(); 
    assertEquals(expResult, result); 
    
    verify(parser, times(1)).buildJsonObject(any(String.class), any(String.class));
    verify(parser, times(1)).buildJsonObject(any(String.class), any(JSONObject.class));
  }

  /**
   * Test of buildInvalidCSVFileMessage method, of class ErrorMessageBuilder.
   */
  @Test
  public void testBuildInvalidCSVFileMessage() {
    System.out.println("buildInvalidCSVFileMessage");
     
    String invalidCsv = "{\n"
            + "	\"msgKey\": \"Invalid CSV file\"\n"
            + "}";
    JSONObject invalidCsvJson = new JSONObject(invalidCsv);

    String invalidCsvError = "{\n"
            + "	\"error\": {\n"
            + "		\"msgKey\": \"Invalid CSV file\"\n"
            + "	}\n"
            + "}";
    JSONObject invalidCsvErrorJson = new JSONObject(invalidCsvError);

    when(parser.buildJsonObject(any(String.class), any(String.class))).thenReturn(invalidCsvJson);
    when(parser.buildJsonObject(any(String.class), eq(invalidCsvJson))).thenReturn(invalidCsvErrorJson);
 
    String expResult = "{\"error\":{\"msgKey\":\"Invalid CSV file\"}}";
    String result = instance.buildInvalidCSVFileMessage(); 
    assertEquals(expResult, result); 
    
    verify(parser, times(1)).buildJsonObject(any(String.class), any(String.class));
    verify(parser, times(1)).buildJsonObject(any(String.class), any(JSONObject.class));
  }

}
