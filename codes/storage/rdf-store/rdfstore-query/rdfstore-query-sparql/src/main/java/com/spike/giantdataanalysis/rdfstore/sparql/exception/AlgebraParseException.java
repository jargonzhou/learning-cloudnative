package com.spike.giantdataanalysis.rdfstore.sparql.exception;

public class AlgebraParseException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public static AlgebraParseException newE() {
    return new AlgebraParseException();
  }

  public static AlgebraParseException newE(String message) {
    return new AlgebraParseException(message);
  }

  public static AlgebraParseException newE(String message, Throwable cause) {
    return new AlgebraParseException(message, cause);
  }

  public static AlgebraParseException newE(Throwable cause) {
    return new AlgebraParseException(cause);
  }

  public static AlgebraParseException newE(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    return new AlgebraParseException(message, cause, enableSuppression, writableStackTrace);
  }

  public AlgebraParseException() {
    super();
  }

  public AlgebraParseException(String message) {
    super(message);
  }

  public AlgebraParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public AlgebraParseException(Throwable cause) {
    super(cause);
  }

  public AlgebraParseException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
