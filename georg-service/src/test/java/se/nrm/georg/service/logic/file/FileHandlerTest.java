package se.nrm.georg.service.logic.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.junit.After; 
import org.junit.Before; 
import org.junit.Test;
import static org.junit.Assert.*;
import se.nrm.georg.service.model.CSVBean;

/**
 *
 * @author idali
 */
public class FileHandlerTest {
  
  private FileHandler instance;
  private List<CSVBean> beans;
  
  public FileHandlerTest() {
  }
 
  @Before
  public void setUp() {
    beans = new ArrayList();
    CSVBean bean1 = new CSVBean("A123", "Sollentuna");
    CSVBean bean2 = new CSVBean("A124", "Stockholm");
    CSVBean bean3 = new CSVBean("A125", "Tyreso");
    
    beans.add(bean1);
    beans.add(bean2);
    beans.add(bean3);
    
    instance = new FileHandler();
  }
  
  @After
  public void tearDown() {
    instance = null;
  }

  /**
   * Test of readCsv method, of class FileHandler.
   * @throws java.io.FileNotFoundException
   */
  @Test
  public void testReadCsv() throws FileNotFoundException {
    System.out.println("readCsv");
    
    String fileType = "csv"; 
     
    String filePath = instance.createFile(beans, fileType); 
    InputStream input = new FileInputStream(filePath);  
    List<CSVRecord> result = instance.readCsv(input);
    assertEquals(result.size(), 3); 
  }

  /**
   * Test of createFile method, of class FileHandler.
   * @throws java.io.IOException
   */
  @Test
  public void testCreateFileCSV() throws IOException {
    System.out.println("createFile"); 
    String fileType = "csv"; 
     
    String result = instance.createFile(beans, fileType); 
    Path path = Paths.get(result); 
    assertTrue(Files.exists(path)); 
    assertEquals(StringUtils.substringAfterLast(result, "."), fileType); 
    Files.deleteIfExists(path);
    assertFalse(Files.exists(path)); 
  }
  
  
  /**
   * Test of createFile method, of class FileHandler.
   * @throws java.io.IOException
   */
  @Test
  public void testCreateFileJSON() throws IOException {
    System.out.println("createFile"); 
    String fileType = "json"; 
     
    String result = instance.createFile(beans, fileType); 
    Path path = Paths.get(result); 
    assertTrue(Files.exists(path)); 
    assertEquals(StringUtils.substringAfterLast(result, "."), fileType); 
    
    Files.deleteIfExists(path);
    assertFalse(Files.exists(path)); 
  }
 
  
//  /**
//   * Test of createJsonFile method, of class FileHandler.
//   */
////  @Test
//  public void testCreateJsonFile() {
//    System.out.println("createJsonFile");
//    List<CSVBean> beans = null;
//    FileWriter out = null;
//    FileHandler instance = new FileHandler();
//    instance.createJsonFile(beans, out);
//    // TODO review the generated test code and remove the default call to fail.
//    fail("The test case is a prototype.");
//  }
//
//  /**
//   * Test of createCSVFile method, of class FileHandler.
//   */
////  @Test
//  public void testCreateJSONFile() throws IOException {
//    System.out.println("createCSVFile");
//
//    File tempFile = File.createTempFile("test", "json");
//    FileWriter out = new FileWriter(tempFile);  
//    instance.createCSVFile(beans, out); 
//  }
//  
}
