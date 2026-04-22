package generated.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;

import org.junit.Assert;
import org.junit.Test;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import com.spike.giantdataanalysis.rdfstore.commons.lang.MoreBytes;

import generated.filesystem.FileStructureProtos.Block;
import generated.filesystem.FileStructureProtos.BlockHeader;
import generated.filesystem.FileStructureProtos.BlockId;
import generated.filesystem.FileStructureProtos.DataFileHeader;
import generated.filesystem.FileStructureProtos.FileAccessMode;
import generated.filesystem.FileStructureProtos.FileCatalog;
import generated.filesystem.FileStructureProtos.FileId;
import generated.filesystem.FileStructureProtos.FileNo;
import generated.filesystem.FileStructureProtos.FileType;
import generated.filesystem.client.ProcessStructureProtos.ClientId;

public class TestFileStructure {

  // 测试序列化和反序列化
  @Test
  public void testSerDe() throws InvalidProtocolBufferException {
    DataFileHeader fileHeader = DataFileHeader.newBuilder().setMagicNumber(42)//
        .setFileType(FileType.DATA_FILE).build();
    System.out.println(fileHeader);

    byte[] bytes = fileHeader.toByteArray();

    DataFileHeader returnDataFileHeader = DataFileHeader.parseFrom(bytes);
    System.out.println(returnDataFileHeader);
  }

  @Test
  public void testCatalog() throws IOException {

    Path catalogFile = Paths.get("target/catalog");
    // catalogFile.toFile().deleteOnExit();

    OutputStream os = Files.newOutputStream(catalogFile);

    FileNo fileNo = FileNo.newBuilder().setFileNo(1).build();
    FileCatalog fileCatalog = FileCatalog.newBuilder()//
        .setFileno(fileNo).setFileName("file1").build();
    fileCatalog.writeDelimitedTo(os);

    fileNo = FileNo.newBuilder().setFileNo(2).build();
    fileCatalog = FileCatalog.newBuilder()//
        .setFileno(fileNo).setFileName("file2").build();
    fileCatalog.writeDelimitedTo(os);

    Parser<FileCatalog> parser = FileCatalog.parser();
    InputStream is = Files.newInputStream(catalogFile);
    FileCatalog result = parser.parseDelimitedFrom(is);
    System.out.println(result.toString());

    result = parser.parseDelimitedFrom(is);
    System.out.println(result.toString());

    os.close();
    is.close();
  }

  // 测试文件头部DataFileHeader
  @Test
  public void testDataFileHeader() throws IOException {
    RandomAccessFile raf = new RandomAccessFile("/tmp/datafileheader", "rw");

    DataFileHeader fileHeader = DataFileHeader.newBuilder().setMagicNumber(42)//
        .setFileType(FileType.DATA_FILE).build();
    System.out.println(fileHeader.getSerializedSize());// 4
    raf.write(fileHeader.toByteArray());

    raf.seek(0);
    byte[] bytes = new byte[fileHeader.getSerializedSize()];
    raf.read(bytes);
    System.out.println(DataFileHeader.parseFrom(bytes));

    // fileHeader = DataFileHeader.newBuilder().setMagicNumber(42)//
    // .setFileType(null).build();
    // System.out.println(fileHeader.getSerializedSize());// NPE
    fileHeader = DataFileHeader.newBuilder().setMagicNumber(Integer.MAX_VALUE)//
        .setFileType(FileType.DATA_FILE).build();
    System.out.println(fileHeader.getSerializedSize());// 8

    raf.close();
  }

  @Test
  public void testBitSet() {
    BitSet bitSet = new BitSet(10);
    bitSet.set(1, true);
    System.out.println(bitSet);
    System.out.println(MoreBytes.toHex(bitSet.toByteArray()));
  }

  @Test
  public void testBlock() {
    final int blockSize = 8192;
    FileNo fileNo = FileNo.newBuilder().setFileNo(1).build();

    BlockId blockId = BlockId.newBuilder()//
        .setBlockId(0).build();

    FileId fileId = FileId.newBuilder()//
        .setFileno(fileNo)//
        .setAccessMode(FileAccessMode.WRITE)//
        .build();

    BlockHeader blockHeader = BlockHeader.newBuilder()//
        .setFlip(true)//
        .setFileno(fileId.getFileno())//
        .setBlockno(blockId).build();

    Block block = Block.newBuilder()//
        .setHeader(blockHeader)//
        // .setContents(contents)//
        .setFlop(true).build();

    block = this.fixSize(blockSize, block);
    Assert.assertEquals(blockSize, block.getSerializedSize());
  }

  private Block fixSize(final int blockSize, final Block sourceBlock) {

    int currentBlockSize = sourceBlock.getSerializedSize();
    int guessContentSize = blockSize - sourceBlock.getSerializedSize();

    while (blockSize != currentBlockSize) {
      System.out.println("currentBlockSize=" + currentBlockSize);
      System.out.println("guessContentSize=" + guessContentSize);

      final int lastGuessContentSize = guessContentSize;
      guessContentSize -= CodedOutputStream.computeUInt32SizeNoTag(guessContentSize);
      Block resultBlock = Block.newBuilder(sourceBlock)
          .setContents(ByteString.copyFrom(new byte[guessContentSize])).build();
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
}
