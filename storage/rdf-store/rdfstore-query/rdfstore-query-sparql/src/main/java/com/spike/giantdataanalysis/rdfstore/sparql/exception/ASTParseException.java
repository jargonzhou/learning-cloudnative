package com.spike.giantdataanalysis.rdfstore.sparql.exception;

public class ASTParseException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static ASTParseException newE() {
    return new ASTParseException();
  }

  public static ASTParseException newE(String message) {
    return new ASTParseException(message);
  }

  public static ASTParseException newE(String message, Throwable cause) {
    return new ASTParseException(message, cause);
  }

  public static ASTParseException newE(Throwable cause) {
    return new ASTParseException(cause);
  }

  public static ASTParseException newE(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    return new ASTParseException(message, cause, enableSuppression, writableStackTrace);
  }

  public ASTParseException() {
    super();
  }

  public ASTParseException(String message) {
    super(message);
  }

  public ASTParseException(String message, Throwable cause) {
    super(message, cause);
  }

  public ASTParseException(Throwable cause) {
    super(cause);
  }

  public ASTParseException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}