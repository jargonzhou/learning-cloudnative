package com.spike.codesnippet;

import org.rocksdb.*;
import org.rocksdb.util.StdErrLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    static {
        // loads the RocksDB C++ library
        RocksDB.loadLibrary();
    }

    public static void main(String[] args) {

        examplePutAndGet();

//        exampleColumnFamily();
    }


    /**
     * @see RocksDB
     * @see Options
     * @see org.rocksdb.MutableDBOptions.MutableDBOptionsBuilder#setMaxTotalWalSize(long)
     * @see RocksDB#put(byte[], byte[])
     * @see RocksDB#get(byte[])
     * @see RocksDB#delete(byte[])
     * @see RocksDBException
     * @see Logger
     */
    private static void examplePutAndGet() {
        // Opening a Database
        try (StdErrLogger stdErrLogger = new StdErrLogger(InfoLogLevel.DEBUG_LEVEL)) {
            try (final Options options = new Options().setCreateIfMissing(true).setLogger(stdErrLogger).setWriteBufferSize(64 << 20) // write buffer size
                    .setMaxTotalWalSize(64L << 28) // total WAL size
            ) {
                try (final RocksDB db = RocksDB.open(options, "db")) {

                    // Reads and Writes
                    final byte[] key1 = "hello".getBytes();
                    final byte[] value1 = "world".getBytes();
                    db.put(key1, value1);
                    byte[] res = db.get(key1);
                    System.out.println(new String(res));

                    db.delete(key1);

                    db.syncWal();
                }
            } catch (RocksDBException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @see ColumnFamilyOptions
     * @see ColumnFamilyDescriptor
     * @see ColumnFamilyHandle
     * @see DBOptions
     * @see RocksDB#put(ColumnFamilyHandle, WriteOptions, byte[], byte[])
     * @see RocksDB#get(ColumnFamilyHandle, byte[])
     * @see WriteBatch
     * @see RocksDB#dropColumnFamily(ColumnFamilyHandle)
     * @see ColumnFamilyHandle#close()
     * @see org.rocksdb.MutableColumnFamilyOptions.MutableColumnFamilyOptionsBuilder#setWriteBufferSize(long)
     */
    private static void exampleColumnFamily() {
        try (ColumnFamilyOptions cfOptions = new ColumnFamilyOptions().optimizeUniversalStyleCompaction()) {
            // create column family
            final ColumnFamilyDescriptor firstCf = new ColumnFamilyDescriptor("first_cf".getBytes(), cfOptions);
            try (final Options options = new Options().setCreateIfMissing(true); final RocksDB db = RocksDB.open(options, "db")) {

                try (final ColumnFamilyHandle cfHandle = db.createColumnFamily(firstCf)) {
                    System.out.println("Create CF: " + new String(cfHandle.getName()));
                }
            } catch (RocksDBException e) {
                e.printStackTrace();
            }

            final List<ColumnFamilyDescriptor> cfDescriptors = Arrays.asList(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOptions), firstCf);
            final List<ColumnFamilyHandle> cfHandles = new ArrayList<>();
            try (final DBOptions options = new DBOptions().setCreateIfMissing(true).setCreateMissingColumnFamilies(true); final RocksDB db = RocksDB.open(options, "db", cfDescriptors, cfHandles // OUT
            )) {

                MutableColumnFamilyOptions mutableOptions = MutableColumnFamilyOptions.builder().setWriteBufferSize(64 << 20) // column family level write buffer
                        .build();
                db.setOptions(cfHandles.get(1), mutableOptions);

                try {
                    final byte[] key1 = "key".getBytes();
                    final byte[] value1 = "value".getBytes();
                    db.put(cfHandles.get(1), new WriteOptions(), key1, value1);
                    byte[] res = db.get(cfHandles.get(1), key1);
                    System.out.println(new String(res));

                    // atomic write
                    try (final WriteBatch wb = new WriteBatch()) {
                        wb.put(cfHandles.get(0), "key2".getBytes(), "value2".getBytes());
                        wb.put(cfHandles.get(1), "key3".getBytes(), "value3".getBytes());
                        wb.delete(cfHandles.get(1), "key".getBytes());
                        db.write(new WriteOptions(), wb);
                    }

                    // drop column family
                    db.dropColumnFamily(cfHandles.get(1));
                } finally {
                    for (final ColumnFamilyHandle cfHandle : cfHandles) {
                        cfHandle.close();
                    }
                }

                db.syncWal();
            } catch (RocksDBException e) {
                e.printStackTrace();
            }
        }
    }
}