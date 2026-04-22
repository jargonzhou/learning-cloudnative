package com.spike.giantdataanalysis.rdfstore.filesystem;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import com.spike.giantdataanalysis.rdfstore.constant.RDFStoreConstant;
import com.spike.giantdataanalysis.rdfstore.filesystem.catalog.RDFStoreFileSystemCatalogManager;
import com.spike.giantdataanalysis.rdfstore.filesystem.configuration.RDFStoreFileSystemConfiguration;
import com.spike.giantdataanalysis.rdfstore.filesystem.core.RDFStoreFileDescriptor;
import com.spike.giantdataanalysis.rdfstore.filesystem.core.RDFStoreFileSystemMemoryStore;
import com.spike.giantdataanalysis.rdfstore.filesystem.exception.RDFStoreFileSystemException;
import com.spike.giantdataanalysis.rdfstore.metric.RDFStoreMetricRegistry;
import com.spike.giantdataanalysis.rdfstore.metric.key.FileSystemMetricKeys;

import generated.filesystem.FileStructureProtos.Block;
import generated.filesystem.FileStructureProtos.BlockHeader;
import generated.filesystem.FileStructureProtos.BlockId;
import generated.filesystem.FileStructureProtos.DataFileHeader;
import generated.filesystem.FileStructureProtos.FileAccessMode;
import generated.filesystem.FileStructureProtos.FileId;
import generated.filesystem.FileStructureProtos.FileNo;
import generated.filesystem.FileStructureProtos.FileType;

/**
 * Default RDF Store File System implementation.
 */
public class DefaultRDFStoreFileSystem implements RDFStoreFileSystem {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultRDFStoreFileSystem.class);

  private final RDFStoreFileSystemConfiguration fsConfiguration;
  private final RDFStoreMetricRegistry metricRegistry;

  private RDFStoreFileSystemCatalogManager fsCatalogManager;
  private RDFStoreFileSystemMemoryStore fsMemoryStore;

  private String fsRoot = null;
  private int blockSize = -1;
  private int dataFileHeaderSize = -1;

  public DefaultRDFStoreFileSystem(RDFStoreFileSystemConfiguration configuration,
      RDFStoreMetricRegistry metricRegistry) {
    this.fsConfiguration = configuration;

    this.fsCatalogManager = new RDFStoreFileSystemCatalogManager(configuration, metricRegistry);
    this.fsMemoryStore = new RDFStoreFileSystemMemoryStore();

    this.metricRegistry = metricRegistry;

    this.fsRoot = configuration.rdfStore.fileSystem.root;
    this.blockSize = configuration.rdfStore.fileSystem.blockSize;
    this.dataFileHeaderSize = this.getDataFileHeader().toByteArray().length;
  }

  private DataFileHeader getDataFileHeader() {
    return DataFileHeader.newBuilder()//
        .setMagicNumber(RDFStoreConstant.MAGIC_NUMBER)//
        .setFileType(FileType.DATA_FILE).build();
  }

  @Override
  public long getUsableSpaceBytes() {
    Path rootPath = Paths.get(fsRoot);
    try {
      FileStore store = Files.getFileStore(rootPath);
      return store.getUsableSpace();
    } catch (IOException e) {
      throw RDFStoreFileSystemException.newE(e);
    }
  }

  @Override
  public RDFStoreFileSystemCatalogManager getCatalogManager() {
    return this.fsCatalogManager;
  }

  @Override
  public RDFStoreFileSystemMemoryStore getMemoryStore() {
    return this.fsMemoryStore;
  }

  @Override
  public void create(String fileName) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("create file {}", fileName);
    }

    Path filePath = this.getFilePath(fileName);
    if (Files.exists(filePath)) {
      throw RDFStoreFileSystemException.newE("file " + fileName + " already exist!");
    }

    try {
      // create file
      Files.createFile(filePath);
      // register in catalog
      fsCatalogManager.createRegularFileNo(fileName);
      // write file header
      DataFileHeader dataFileHeader = DataFileHeader.newBuilder()//
          .setMagicNumber(RDFStoreConstant.MAGIC_NUMBER)//
          .setFileType(FileType.DATA_FILE).build();
      RandomAccessFile raf = new RandomAccessFile(filePath.toFile(), "rw");
      raf.write(dataFileHeader.toByteArray());
      this.collectRAFMetrics(raf.getFilePointer());
      raf.getFD().sync();
      raf.close();

      if (LOG.isDebugEnabled()) {
        LOG.debug("generate file header {}", dataFileHeader);
      }
    } catch (IOException e) {
      throw RDFStoreFileSystemException.newE("create file " + fileName + " failed!", e);
    }

  }

  private Path getFilePath(String fileName) {
    return Paths.get(fsRoot, fileName);
  }

  @Override
  public void delete(String fileName) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("delete file {}", fileName);
    }

    Path filePath = this.getFilePath(fileName);

    if (!Files.exists(filePath)) {
      return;
    }

    try {
      Files.delete(filePath);
      fsCatalogManager.deleteRegularFileNo(fileName);
    } catch (IOException e) {
      throw RDFStoreFileSystemException.newE("delete " + fileName + " failed!", e);
    }
  }

  @Override
  public FileId open(String fileName, FileAccessMode fam) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("open file {} with mode {}", fileName, fam);
    }

    FileNo fileNo = fsCatalogManager.getRegularFileNo(fileName);
    if (fileNo == null) {
      switch (fam) {
      case READ:
      case APPEND:
        throw RDFStoreFileSystemException.newE("file " + fileName + " not exist!");
      case WRITE:
        this.create(fileName);
        fileNo = fsCatalogManager.getRegularFileNo(fileName);
        break;
      default: // UNRECOGNIZED
        throw RDFStoreFileSystemException.newE("unsupported accessmode: " + fam + "!");
      }
    }

    FileId fileId = FileId.newBuilder()//
        .setFileno(fileNo)//
        .setAccessMode(fam).build();

    fsMemoryStore.initializeFileHandle(fsConfiguration.rdfStore.fileSystem.root, fileName, fileId);

    if (LOG.isDebugEnabled()) {
      LOG.debug(this.debugDump(fileId));
    }

    return fileId;
  }

  @Override
  public void close(FileId fileId) {
    fsMemoryStore.destroyFileHandle(fileId);
  }

  @Override
  public void extend(FileId fileId) {
    RDFStoreFileDescriptor fd = fsMemoryStore.getFileHandle(fileId);

    try {
      fd.raf.skipBytes(blockSize);
    } catch (IOException e) {
      throw RDFStoreFileSystemException.newE(e);
    }
  }

  @Override
  public Block read(FileId fileId, BlockId blockId) {
    this.checkReadable(fileId);

    if (LOG.isDebugEnabled()) {
      LOG.debug("read fileId={}, blockId={}", fileId, blockId);
      LOG.debug(this.debugDump(fileId));
    }

    RDFStoreFileDescriptor fd = fsMemoryStore.getFileHandle(fileId);

    try {
      long currentPos = fd.raf.getFilePointer();
      long seekToPos = dataFileHeaderSize + blockId.getBlockId() * blockSize;
      fd.raf.seek(seekToPos);
      this.collectRAFMetrics(currentPos - seekToPos);

      byte[] blockData = new byte[blockSize];
      this.collectMemoryMetrics(blockSize);

      int readByteCount = fd.raf.read(blockData);
      Preconditions.checkState(readByteCount == blockSize, "read non block part!");
      this.collectRAFMetrics(blockSize);

      return Block.parseFrom(blockData);
    } catch (IOException e) {
      throw RDFStoreFileSystemException.newE(e);
    } catch (IllegalStateException e) {
      throw RDFStoreFileSystemException.newE(e);
    }
  }

  @Override
  public Block[] readc(FileId fileId, BlockId startBlockId, int blockCount) {
    Preconditions.checkArgument(blockCount >= 1, "blockCount should be greater or equal to 1!");
    this.checkReadable(fileId);

    if (LOG.isDebugEnabled()) {
      LOG.debug("readc fileId={}, blockId={}, blockCount={}", fileId, startBlockId, blockCount);
      LOG.debug(this.debugDump(fileId));
    }

    RDFStoreFileDescriptor fd = fsMemoryStore.getFileHandle(fileId);

    Block[] result = new Block[blockCount];
    try {
      long currentPos = fd.raf.getFilePointer();
      long seekToPos = dataFileHeaderSize + startBlockId.getBlockId() * blockSize;
      fd.raf.seek(seekToPos);
      this.collectRAFMetrics(currentPos - seekToPos);

      for (int i = 0; i < blockCount; i++) {
        byte[] blockData = new byte[blockSize];
        this.collectMemoryMetrics(blockSize);
        int readByteCount = fd.raf.read(blockData);
        Preconditions.checkState(readByteCount == blockSize, "read non block part!");
        this.collectRAFMetrics(blockSize);
        result[i] = Block.parseFrom(blockData);
      }
    } catch (IOException e) {
      throw RDFStoreFileSystemException.newE(e);
    } catch (IllegalStateException e) {
      throw RDFStoreFileSystemException.newE(e);
    }

    return result;
  }

  @Override
  public void write(FileId fileId, Block block) {
    this.checkWritable(fileId);

    BlockId blockId = block.getHeader().getBlockno();

    block = this.fixBlockSize(blockSize, block);

    RDFStoreFileDescriptor fd = fsMemoryStore.getFileHandle(fileId);
    try {
      long currentPos = fd.raf.getFilePointer();
      long seekToPos = dataFileHeaderSize + blockId.getBlockId() * blockSize;
      fd.raf.seek(seekToPos);
      this.collectRAFMetrics(currentPos - seekToPos);
      fd.raf.write(block.toByteArray());
      this.collectRAFMetrics(blockSize);

    } catch (IOException e) {
      throw RDFStoreFileSystemException.newE(e);
    }

    this.flush(fileId);

    if (LOG.isDebugEnabled()) {
      LOG.debug("write fileId={}, block={}", fileId, block.getHeader());
      LOG.debug(this.debugDump(fileId));
    }
  }

  /** pad block. */
  private Block fixBlockSize(final int blockSize, final Block sourceBlock) {

    int currentBlockSize = sourceBlock.getSerializedSize();
    int guessContentSize = blockSize - sourceBlock.getSerializedSize();
    byte[] sourceContent = sourceBlock.getContents().toByteArray();

    while (blockSize != currentBlockSize) {
      final int lastGuessContentSize = guessContentSize;
      guessContentSize -= CodedOutputStream.computeUInt32SizeNoTag(guessContentSize);
      byte[] bytes = new byte[guessContentSize];
      System.arraycopy(sourceContent, 0, bytes, 0, sourceContent.length);
      Block resultBlock =
          Block.newBuilder(sourceBlock).setContents(ByteString.copyFrom(bytes)).build();
      currentBlockSize = resultBlock.getSerializedSize();

      if (currentBlockSize > blockSize) {
        guessContentSize = lastGuessContentSize - 1;
      } else if (currentBlockSize < blockSize) {
        guessContentSize = lastGuessContentSize + 1;
      } else {
        return resultBlock;
      }
    }

    return sourceBlock;
  }

  @Override
  public void writec(FileId fileId, BlockId startBlockId, Block[] blocks) {
    if (blocks.length == 0) {
      return;
    }

    this.checkWritable(fileId);

    RDFStoreFileDescriptor fd = fsMemoryStore.getFileHandle(fileId);
    BlockId currentBlockId = startBlockId;
    for (Block block : blocks) {
      block = this.fixBlockSize(blockSize, block);
      try {
        fd.raf.seek(dataFileHeaderSize + currentBlockId.getBlockId() * blockSize);
        fd.raf.write(block.toByteArray());

        currentBlockId =
            BlockId.newBuilder(currentBlockId).setBlockId(currentBlockId.getBlockId() + 1).build();
      } catch (IOException e) {
        throw RDFStoreFileSystemException.newE(e);
      }
    }

    this.flush(fileId);

    if (LOG.isDebugEnabled()) {
      List<BlockHeader> blockHeaders = Lists.newArrayList();
      for (Block block : blocks) {
        blockHeaders.add(block.getHeader());
      }
      LOG.debug("writec fileId={}, startBlockId={}, blocks={}", fileId, startBlockId, blockHeaders);
      LOG.debug(this.debugDump(fileId));
    }
  }

  @Override
  public void flush(FileId fileId) {
    RDFStoreFileDescriptor fd = fsMemoryStore.getFileHandle(fileId);
    try {
      // fd.raf.getFD().sync();
      fd.raf.getChannel().force(true);
    } catch (IOException e) {
      throw RDFStoreFileSystemException.newE(e);
    }
  }

  // ---------------------------------------------------------------------------
  // check access mode
  // ---------------------------------------------------------------------------

  private void checkReadable(FileId fileId) {
    FileAccessMode am = fileId.getAccessMode();
    switch (am) {
    case UNRECOGNIZED:
      throw RDFStoreFileSystemException.newE("cannot read file " + fileId);
    default:
      break;
    }
  }

  private void checkWritable(FileId fileId) {
    FileAccessMode am = fileId.getAccessMode();
    switch (am) {
    case READ:
    case UNRECOGNIZED:
      throw RDFStoreFileSystemException.newE("cannot write file  " + fileId);
    default:
      break;
    }
  }

  // ---------------------------------------------------------------------------
  // collect meters
  // ---------------------------------------------------------------------------

  private void collectMemoryMetrics(int sizeInByte) {
    metricRegistry.meter(FileSystemMetricKeys.FS_KEY_MEM_ALLOCATE_METER).mark();
    metricRegistry.histogram(FileSystemMetricKeys.FS_KEY_MEM_ALLOCATE_HISTOGRAM).update(blockSize);
  }

  private void collectRAFMetrics(long deltaPosition) {
    metricRegistry.counter(FileSystemMetricKeys.FS_KEY_RAF_SEEK_COUNTER).inc();
    metricRegistry.histogram(FileSystemMetricKeys.FS_KEY_RAF_SEEK_SKIP_HISTOGRAM)
        .update(Math.abs(deltaPosition));
  }

  // ---------------------------------------------------------------------------
  // debug
  // ---------------------------------------------------------------------------

  private String debugDump(FileId fileId) {
    StringBuilder sb = new StringBuilder();
    RDFStoreFileDescriptor fd = fsMemoryStore.getFileHandle(fileId);

    sb.append(System.lineSeparator());
    sb.append("========================= DUMP START ========================= ");
    sb.append(System.lineSeparator());
    try {
      long currentReadByteCount = 0L;

      byte[] dataFileHeaderBytes = new byte[this.dataFileHeaderSize];
      fd.raf.seek(0);
      fd.raf.read(dataFileHeaderBytes);
      sb.append(DataFileHeader.parseFrom(dataFileHeaderBytes)).append(System.lineSeparator());
      currentReadByteCount += this.dataFileHeaderSize;

      while (currentReadByteCount < fd.raf.length()) {
        byte[] blockBytes = new byte[this.blockSize];
        fd.raf.read(blockBytes);
        sb.append(Block.parseFrom(blockBytes)).append(System.lineSeparator());

        currentReadByteCount += this.blockSize;
      }

    } catch (IOException e) {
      throw RDFStoreFileSystemException.newE(e);
    }
    sb.append("========================= DUMP END ========================= ");
    return sb.toString();
  }

}
