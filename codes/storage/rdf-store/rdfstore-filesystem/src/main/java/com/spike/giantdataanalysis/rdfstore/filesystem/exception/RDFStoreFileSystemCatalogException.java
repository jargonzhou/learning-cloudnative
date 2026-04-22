package com.spike.giantdataanalysis.rdfstore.filesystem.exception;

public class RDFStoreFileSystemCatalogException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static RDFStoreFileSystemCatalogException newE() {
    return new RDFStoreFileSystemCatalogException();
  }

  public static RDFStoreFileSystemCatalogException newE(String message) {
    return new RDFStoreFileSystemCatalogException(message);
  }

  public static RDFStoreFileSystemCatalogException newE(String message, Throwable cause) {
    return new RDFStoreFileSystemCatalogException(message, cause);
  }

  public static RDFStoreFileSystemCatalogException newE(Throwable cause) {
    return new RDFStoreFileSystemCatalogException(cause);
  }

  public static RDFStoreFileSystemCatalogException newE(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    return new RDFStoreFileSystemCatalogException(message, cause, enableSuppression,
        writableStackTrace);
  }

  public RDFStoreFileSystemCatalogException() {
    super();
  }

  public RDFStoreFileSystemCatalogException(String message) {
    super(message);
  }

  public RDFStoreFileSystemCatalogException(String message, Throwable cause) {
    super(message, cause);
  }

  public RDFStoreFileSystemCatalogException(Throwable cause) {
    super(cause);
  }

  public RDFStoreFileSystemCatalogException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}