package se.nrm.georg.service.logic.exceptions;

import javax.ejb.ApplicationException;

/**
 *
 * @author idali
 */
@ApplicationException
public class GeorgException extends RuntimeException {

  private int errorCode;
  private String errorMsg;

  public GeorgException() {
  }

  public GeorgException(String s) {
    super(s);
  }

  public GeorgException(String s, int errorCode) {
    super(s);
    this.errorCode = errorCode;

  }

  public int getErrorCode() {
    return errorCode;
  } 
}
