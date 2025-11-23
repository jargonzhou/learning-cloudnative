package com.spike.giantdataanalysis.rdfstore.filesystem.exception;

/**
 * RDF Store File System Exception.
 */
public class RDFStoreFileSystemException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static RDFStoreFileSystemException newE() {
    return new RDFStoreFileSystemException();
  }

  public static RDFStoreFileSystemException newE(String message) {
    return new RDFStoreFileSystemException(message);
  }

  public static RDFStoreFileSystemException newE(String message, Throwable cause) {
    return new RDFStoreFileSystemException(message, cause);
  }

  public static RDFStoreFileSystemException newE(Throwable cause) {
    return new RDFStoreFileSystemException(cause);
  }

  public static RDFStoreFileSystemException newE(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    return new RDFStoreFileSystemException(message, cause, enableSuppression, writableStackTrace);
  }

  public RDFStoreFileSystemException() {
    super();
  }

  public RDFStoreFileSystemException(String message) {
    super(message);
  }

  public RDFStoreFileSystemException(String message, Throwable cause) {
    super(message, cause);
  }

  public RDFStoreFileSystemException(Throwable cause) {
    super(cause);
  }

  public RDFStoreFileSystemException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}