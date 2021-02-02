package se.nrm.georg.service.logic.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;   
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.json.JSONArray;
import org.json.JSONObject;
import se.nrm.georg.service.logic.csv.util.CSVHeader;
import se.nrm.georg.service.model.CSVBean;

/**
 *
 * @author idali
 */
@Slf4j
public class FileHandler {
  
   private CSVFormat csvFileFormat; 
//  private final char tabSepartor = '\t';
  private final String tempFileName = "georgTempFile";
  private final String csvSuffix = ".csv";
  private final String jsonSuffix = ".json";
  private final String json = "json";
  
  public FileHandler() {

  }

  public List<CSVRecord> readCsv(InputStream in) {
//csvFileFormat = CSVFormat.DEFAULT.withDelimiter(tabSepartor)
    csvFileFormat = CSVFormat.DEFAULT
            .withFirstRecordAsHeader()
            .withQuoteMode(QuoteMode.ALL);
    Reader reader;
    try {
      reader = new InputStreamReader(in);
      return csvFileFormat.parse(reader).getRecords();
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
    return null;
  }

  public String createFile(List<CSVBean> beans, String fileType) {

    FileWriter out = null;
    File tempFile = null;
    try {
      tempFile = File.createTempFile(tempFileName, getFileSuffix(fileType));
      out = new FileWriter(tempFile); 
      if(fileType != null && fileType.equals(json)) {
        createJsonFile(beans, out);
      } else {
        createCSVFile(beans, out);
      }
      return tempFile.toString();
    } catch (IOException ex) {
      log.error(ex.getMessage());
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        if (tempFile != null) {
          tempFile.deleteOnExit();
        }
      } catch (IOException ex) {
        log.error(ex.getMessage());
      }
    }
    return null;
  }

  private String getFileSuffix(String fileType) {
    return fileType != null && fileType.equals(json) ? jsonSuffix : csvSuffix;
  }

  public void createJsonFile(List<CSVBean> beans, FileWriter out) {
     try {
       JSONArray jsonArray = new JSONArray();
       beans.stream()
               .map(bean -> new JSONObject(bean))
               .forEach(jsonObj -> {
                 jsonArray.put(jsonObj);
               });
       out.write(jsonArray.toString());
       out.flush();
     } catch (IOException ex) {
       log.error(ex.getMessage());
     } 
  }

  public void createCSVFile(List<CSVBean> beans, FileWriter out) {
    try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
            .withHeader(CSVHeader.class))) {
      beans.stream()
              .forEach(bean -> {
                printRow(bean, printer);
              });
    } catch (IOException ex) { 
      log.error(ex.getMessage());
     } 
  } 
 

//    FileWriter out = null;
//    File tempFile = null;
//    try {   
//      tempFile = File.createTempFile(tempFileName, csv); 
//      out = new FileWriter(tempFile); 
//      try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
//              .withHeader(CSVHeader.class))) {
//        beans.stream()
//                .forEach(bean -> {
//                  printRow(bean, printer);
//                });
//      }
//      return tempFile.toString();
//    } catch (IOException ex) {
//      log.error(ex.getMessage());
//    } finally {
//      try {
//        if (out != null) {
//          out.close();
//        }
//        if(tempFile != null) {
//          tempFile.deleteOnExit();
//        }
//      } catch (IOException ex) {
//        log.error(ex.getMessage());
//      }
//    }
//    return null;
//  }
  
  private void printRow(CSVBean bean, CSVPrinter printer) {
    try {
      printer.printRecord(bean.getId(), bean.getSourceLocality(),
              bean.getSuggestedLocality(), bean.getLatLngDMS(), bean.getUncertainty());
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
  }
  
}
