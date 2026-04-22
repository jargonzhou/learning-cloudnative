package com.spike.giantdataanalysis.rdfstore.filesystem.configuration.value;

import com.spike.giantdataanalysis.rdfstore.configuration.value.OverridableConfigurationValue;

public class RDFStoreFileSystemConfigurationValue
    implements OverridableConfigurationValue<RDFStoreFileSystemConfigurationValue> {
  public String root = "/tmp";
  public String catalogRoot = "/tmp/catalog";
  public Long fileSizeMax = 4294967296L;
  public Integer blockSize = 8192;
  public Double pageCompactRatio = 40D;
  public Double pageFreePercentMin = 20D;

  @Override
  public void override(RDFStoreFileSystemConfigurationValue another) {
    if (another == null) {
      return;
    }

    // TODO(zhoujiagen) may add argument check here.
    if (another.root != null) {
      this.root = another.root;
    }
    if (another.fileSizeMax != null) {
      this.fileSizeMax = another.fileSizeMax;
    }
    if (another.blockSize != null) {
      this.blockSize = another.blockSize;
    }
    if (another.pageCompactRatio != null) {
      this.pageCompactRatio = another.pageCompactRatio;
    }
    if (another.pageFreePercentMin != null) {
      this.pageFreePercentMin = another.pageFreePercentMin;
    }
    if (another.catalogRoot != null) {
      this.catalogRoot = another.catalogRoot;
    }
  }

  @Override
  public String repr() {
    StringBuilder sb = new StringBuilder();
    sb.append("root=").append(root).append(System.lineSeparator());
    sb.append("catalogRoot=").append(catalogRoot).append(System.lineSeparator());
    sb.append("fileSizeMax=").append(fileSizeMax).append(System.lineSeparator());
    sb.append("blockSize=").append(blockSize).append(System.lineSeparator());
    sb.append("pageCompactRatio=").append(pageCompactRatio).append(System.lineSeparator());
    sb.append("pageFreePercentMin=").append(pageFreePercentMin).append(System.lineSeparator());
    return sb.toString();
  }

}