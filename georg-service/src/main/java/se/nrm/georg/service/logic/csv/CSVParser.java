package se.nrm.georg.service.logic.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List; 

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import se.nrm.georg.service.logic.csv.util.CSVHeader;
import se.nrm.georg.service.model.CSVBean;

/**
 *
 * @author idali
 */
@Slf4j
public class CSVParser {

  private CSVFormat csvFileFormat; 
//  private final char tabSepartor = '\t';
  private final String tempFileName = "georgTempFile";
  private final String suffix = "csv";

  public CSVParser() {
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

  public String createCSVFile(List<CSVBean> beans) {

    FileWriter out = null;
    File tempFile = null;
    try {   
      tempFile = File.createTempFile(tempFileName, suffix); 
      out = new FileWriter(tempFile); 
      try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
              .withHeader(CSVHeader.class))) {
        beans.stream()
                .forEach(bean -> {
                  printRow(bean, printer);
                });
      }
      return tempFile.toString();
    } catch (IOException ex) {
      log.error(ex.getMessage());
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        if(tempFile != null) {
          tempFile.deleteOnExit();
        }
      } catch (IOException ex) {
        log.error(ex.getMessage());
      }
    }
    return null;
  }

  private void printRow(CSVBean bean, CSVPrinter printer) {
    try {
      printer.printRecord(bean.getId(), bean.getSourceLocality(),
              bean.getSuggestedLocality(), bean.getLatLngDMS(), bean.getUncertainty());
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
  }

//  public void writeCsv(List<CSVBean> beans) {
//    try {
//      Path tempFile = Files.createTempFile(null, null);
//      log.info("filePath : {}", tempFile);
//
//    } catch (IOException ex) {
//      log.error(ex.getMessage());
//    }
//  }
}
