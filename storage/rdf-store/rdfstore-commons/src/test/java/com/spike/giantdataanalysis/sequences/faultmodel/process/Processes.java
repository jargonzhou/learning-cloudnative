package com.spike.giantdataanalysis.sequences.faultmodel.process;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.rdfstore.commons.lang.MoreBytes;
import com.spike.giantdataanalysis.sequences.faultmodel.store.StoreConfiguration;
import com.spike.giantdataanalysis.sequences.faultmodel.store.Stores;

// ---------------------------------------------------------------------------
// external behavior: store_read(), store_write(), message_send(), message_get()
// ---------------------------------------------------------------------------
public final class Processes {
  private static final Logger LOG = LoggerFactory.getLogger(Processes.class);

  final Process[] processes = new Process[ProcessConfiguration.PROCESS_NUMBER];
  private final Random random = new Random(new Date().getTime());
  final Stores stores;
  volatile Process primaryProcess;
  private final ProcessConfiguration configuration;

  private final Map<Integer, Integer> PAIR_MAP = Maps.newHashMap();

  public Processes(ProcessConfiguration configuration, Stores stores) {
    this.configuration = configuration;
    this.configuration.makeStatic();
    this.stores = stores;

    this.initialize();
  }

  private synchronized void initialize() {
    for (int i = 0; i < ProcessConfiguration.PROCESS_NUMBER; i++) {
      processes[i] = new Process(i, new State());
      processes[i].injectEnvironment(this);
    }
  }

  // ---------------------------------------------------------------------------
  // process decay
  // ---------------------------------------------------------------------------
  void process_execution(int processIndex) {
    if (processIndex >= ProcessConfiguration.PROCESS_NUMBER) return;
    Process process = processes[processIndex];
    if (process == null) process = new Process(processIndex, new State());
    process.toRegular();

    long processFailTime;
    long processRepairDuration;

    while (true) {

      processFailTime = (long) (new Date().getTime() + ProcessConfiguration.PROCESS_mttpf);
      processRepairDuration = (long) ProcessConfiguration.PROCESS_mttpr;

      while (new Date().getTime() < processFailTime) {
        process.start();
      }

      // repair
      try {
        Thread.sleep(processRepairDuration);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // reset
      process.reset();

      // drop all messages in queue
      while (process.message_get(//
        new byte[ProcessConfiguration.MESSAGE_DATA_SIZE], new boolean[] { false })) {
      }

    }
  }

  // ---------------------------------------------------------------------------
  // a process generating sequence, use checkpoint and restart
  // ---------------------------------------------------------------------------
  void checkpoint_restart_process(int processIndex) {
    if (processIndex >= ProcessConfiguration.PROCESS_NUMBER) return;
    Process process = processes[processIndex];
    if (process == null) process = new Process(processIndex, new State()); // ???

    final int pageSize = stores.getConfiguration().getPage_size();

    class Value {
      long ticketno;

      byte[] asPage() {
        // fill to full page
        byte[] page = new byte[pageSize];
        byte[] actual = MoreBytes.toBytes(ticketno);
        System.arraycopy(actual, 0, page, 0, actual.length);
        return page;
      }
    }

    // class Msg {
    // int processIndex;
    //
    // byte[] asPage() {
    // // fill to full page
    // byte[] page = new byte[pageSize];
    // byte[] actual = MoreBytes.toBytes(processIndex);
    // System.arraycopy(actual, 0, page, 0, actual.length);
    // return page;
    // }
    // }

    Value[] values = new Value[2]; // index at 0: current ticketno
    for (int i = 0; i < 2; i++) {
      values[i] = new Value();
    }

    // TODO(zhoujiagen) implement process's private store (group)
    int storeGroupIndex = 0;
    int[] pageIndexes = { 0, 1 }; // 2 copies of state
    int old;
    byte[] storeValueData = new byte[pageSize];
    byte[] storeMessageData = new byte[pageSize];

    /** logic of restart: read from page 0/1, determine which one is the latest */
    for (old = 0; old <= 1; old++) {
      // PRE: construct process's store view
      boolean reliableReadFlag =
          stores.reliable_store_read(storeGroupIndex, pageIndexes[old], storeValueData); // R
      values[old].ticketno = MoreBytes.toLong(storeValueData);
      if (!reliableReadFlag) {
        this.panic(processIndex);
      }
    }
    if (values[1].ticketno < values[0].ticketno) {
      old = 1;
    } else {
      old = 0;
      values[0].ticketno = values[1].ticketno;
    }

    /** logic of execution: write to page 0/1 */
    while (true) {
      while (!process.get_available_message(storeMessageData)) { // wait to handle sequence request
      }
      int processIndexInMsg = MoreBytes.toInt(storeMessageData);
      values[0].ticketno = values[0].ticketno + 1;
      // override old page value
      if (!stores.reliable_store_write(storeGroupIndex, pageIndexes[old], values[0].asPage())) { // W
        this.panic(processIndex);
      }
      old = (old + 1) % 2;
      process.message_send(processIndexInMsg, values[0].asPage());
    }
  }

  // ---------------------------------------------------------------------------
  // process pair
  // ---------------------------------------------------------------------------
  Thread[] process_pair_process(final int firstProcessIndex, final int secondProcessIndex) {
    if (firstProcessIndex >= ProcessConfiguration.PROCESS_NUMBER) throw new RuntimeException();
    if (secondProcessIndex >= ProcessConfiguration.PROCESS_NUMBER) throw new RuntimeException();
    if (firstProcessIndex == secondProcessIndex) throw new RuntimeException();

    Thread[] result = new Thread[2];

    PAIR_MAP.put(firstProcessIndex, secondProcessIndex);
    PAIR_MAP.put(secondProcessIndex, firstProcessIndex);

    Process firstProcess = processes[firstProcessIndex];
    if (firstProcess == null) firstProcess = new Process(firstProcessIndex, new State()); // ???
    Process secondProcess = processes[secondProcessIndex];
    if (secondProcess == null) secondProcess = new Process(secondProcessIndex, new State()); // ???
    final Process pair0 = firstProcess;
    final Process pair1 = secondProcess;
    pair0.toPair();
    pair1.toPair();

    // byte[] message = new byte[ProcessConfiguration.MESSAGE_DATA_SIZE];
    // MoreBytes.putByte(message, 0, MessageProtocol.MP_IS_PRIMARY);
    // pick one as the primary process
    if (random.nextBoolean()) {
      // message_send(firstProcessIndex, message);
      LOG.info("Process {} is the primary.", firstProcessIndex);
      pair0.isPrimary = true;
      primaryProcess = pair0;

    } else {
      // message_send(secondProcessIndex, message);
      LOG.info("Process {} is the primary.", secondProcessIndex);
      pair1.isPrimary = true;
      primaryProcess = pair1;
    }

    Thread pair0JavaThread = new Thread(new Runnable() {
      @Override
      public void run() {
        pair0.start();
      }
    });

    Thread pair1JavaThread = new Thread(new Runnable() {
      @Override
      public void run() {
        pair1.start();
      }
    });

    result[0] = pair0JavaThread;
    result[1] = pair1JavaThread;

    return result;
  }

  synchronized void takeover(Process sourceProcess) {
    Integer targetProcessIndex = PAIR_MAP.get(sourceProcess.processId);
    if (targetProcessIndex == null) return;
    Process targetProcess = processes[targetProcessIndex];
    if (targetProcess == null) return;

    sourceProcess.isPrimary = true;
    targetProcess.isPrimary = false;
    primaryProcess = sourceProcess;
  }

  boolean message_send_to_pair(Process sourceProcess, byte[] value) {
    Integer targetProcessIndex = PAIR_MAP.get(sourceProcess.processId);
    if (targetProcessIndex == null) return false;

    return this.message_send(targetProcessIndex, value);
  }

  boolean message_send(int processIndex, byte[] value) {
    if (processIndex >= ProcessConfiguration.PROCESS_NUMBER) return false;
    Process process = processes[processIndex];
    if (process == null) return false;

    return this.do_message_send(process, value);
  }

  boolean message_send(Process process, byte[] value) {
    return this.do_message_send(process, value);
  }

  private boolean do_message_send(Process process, byte[] value) {
    // mock message lost or disrupt
    if (random.nextDouble() < ProcessConfiguration.MESSAGE_pmf) return false;

    process.messageQueue.add(new Message(true, value));

    // mock duplicated message
    if (random.nextDouble() < ProcessConfiguration.MESSAGE_pmd) {
      do_message_send(process, value);
    }
    return true;
  }

  void panic(int processIndex) {
    if (processIndex >= ProcessConfiguration.PROCESS_NUMBER) return;
    Process process = processes[processIndex];
    if (process == null) return;

    processes[processIndex] = null;
    process = null;
  }

  String dump(int processIndex) {
    if (processIndex >= ProcessConfiguration.PROCESS_NUMBER) return "NA";
    Process process = processes[processIndex];
    if (process == null) return "NULL";

    return process.toString();
  }

  public ProcessConfiguration getConfiguration() {
    return configuration;
  }

  public static void main(String[] args) {
    StoreConfiguration storeConfiguration = StoreConfiguration._default();
    storeConfiguration.setStore_number(16);
    storeConfiguration.setPage_number_in_store(4);
    storeConfiguration.setPage_size(32);
    Stores stores = new Stores(storeConfiguration);

    ProcessConfiguration processConfiguration = ProcessConfiguration._default();
    processConfiguration.setProcess_number(3); // pair + client
    processConfiguration.setProcess_program_size(32);
    processConfiguration.setProcess_data_size(32);
    processConfiguration.setMessage_data_size(32);
    final Processes processes = new Processes(processConfiguration, stores);

    System.out.println(processes.dump(0));

    // start pair
    final int pair0ProcessIndex = 0;
    final int pair1ProcessIndex = 1;

    Thread[] threads = processes.process_pair_process(pair0ProcessIndex, pair1ProcessIndex);
    for (Thread thread : threads) {
      thread.start();
    }

    // start client
    final Process clientProcess = processes.processes[2];
    clientProcess.toRegular();
    Thread clientJavaThread = new Thread(new Runnable() {
      @Override
      public void run() {
        byte[] kickOffMessage = new byte[ProcessConfiguration.MESSAGE_DATA_SIZE];
        int offset = 0;
        MoreBytes.putByte(kickOffMessage, offset, (byte) MessageProtocol.MP_CLIENT_KICKOFF);
        offset += Message.PROTOCOL_BYTE_SIZE;
        MoreBytes.putInt(kickOffMessage, offset, 3); // retry times
        offset += Integer.SIZE / Byte.SIZE;
        MoreBytes.putInt(kickOffMessage, offset, 10); // total send times

        processes.message_send(clientProcess, kickOffMessage);

        clientProcess.start();
      }
    });

    clientJavaThread.start();

    try {
      for (Thread thread : threads) {
        thread.join();
      }
      clientJavaThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }
}
