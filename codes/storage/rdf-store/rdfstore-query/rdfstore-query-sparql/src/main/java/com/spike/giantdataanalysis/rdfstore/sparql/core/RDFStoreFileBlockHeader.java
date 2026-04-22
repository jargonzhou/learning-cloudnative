package com.spike.giantdataanalysis.rdfstore.sparql.core;

import com.spike.giantdataanalysis.db.filesystem.core.AbstractFileBlockHeader;
import com.spike.giantdataanalysis.db.filesystem.core.FileBlockEntity;
import com.spike.giantdataanalysis.db.filesystem.core.FileBlockId;

public class RDFStoreFileBlockHeader extends AbstractFileBlockHeader {
  private static final long serialVersionUID = 1L;

  private int blockType;
  private FileBlockId nextBlockId;
  private int recordCount;
  private int freeByteOffset = 0;

  public RDFStoreFileBlockHeader(Integer blockSizeInByte, FileBlockEntity fileBlockEntity) {
    super(blockSizeInByte, fileBlockEntity);
    // TODO Implement constructor
  }

  @Override
  public byte[] toBytes() {
    // TODO Implement Byteable<AbstractFileBlockHeader>.toBytes
    return null;
  }

  @Override
  public int size() {
    // TODO Implement Byteable<AbstractFileBlockHeader>.size
    return 0;
  }

  @Override
  public RDFStoreFileBlockHeader fromBytes(byte[] bytes) {
    // TODO Implement Byteable<AbstractFileBlockHeader>.fromBytes
    return null;
  }

  @Override
  public int getReprBitSize(int blockSizeType) {
    // TODO Implement AbstractFileBlockHeader.getReprBitSize
    return 0;
  }

  @Override
  public int getReprByteSize(int blockSizeType) {
    // TODO Implement AbstractFileBlockHeader.getReprByteSize
    return 0;
  }

}
