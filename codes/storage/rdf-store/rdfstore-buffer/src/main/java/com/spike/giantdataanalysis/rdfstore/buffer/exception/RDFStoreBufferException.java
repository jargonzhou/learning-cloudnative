package com.spike.giantdataanalysis.rdfstore.buffer.exception;

/**
 * RDF Store Buffer Exception.
 */
public class RDFStoreBufferException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static RDFStoreBufferException newE() {
    return new RDFStoreBufferException();
  }

  public static RDFStoreBufferException newE(String message) {
    return new RDFStoreBufferException(message);
  }

  public static RDFStoreBufferException newE(String message, Throwable cause) {
    return new RDFStoreBufferException(message, cause);
  }

  public static RDFStoreBufferException newE(Throwable cause) {
    return new RDFStoreBufferException(cause);
  }

  public static RDFStoreBufferException newE(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    return new RDFStoreBufferException(message, cause, enableSuppression, writableStackTrace);
  }

  public RDFStoreBufferException() {
    super();
  }

  public RDFStoreBufferException(String message) {
    super(message);
  }

  public RDFStoreBufferException(String message, Throwable cause) {
    super(message, cause);
  }

  public RDFStoreBufferException(Throwable cause) {
    super(cause);
  }

  public RDFStoreBufferException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}