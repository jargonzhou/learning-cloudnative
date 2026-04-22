package com.spike.giantdataanalysis.sequences.faultmodel.process;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.spike.giantdataanalysis.rdfstore.commons.lang.MoreBytes;

class Process {
  private static final Logger LOG = LoggerFactory.getLogger(Process.class);

  final int processId; // process id, i.e. the index in Processes

  final State initial; // initial state
  final State current; // current state
  ConcurrentLinkedQueue<Message> messageQueue;

  // / environment information
  private Processes environment;
  volatile boolean isPrimary = false;

  Process(int processId, State initial) {
    this.processId = processId;
    this.initial = initial;
    this.current = initial;
    messageQueue = new ConcurrentLinkedQueue<>();
  }

  void injectEnvironment(Processes processes) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("inject environment information to {}", this.simpleToString());
    }
    this.environment = processes;
  }

  public void toRegular() {
    initial.program[0] = StateProtocol.SP_P_START_REGUALR;
  }

  public void toPair() {
    initial.program[0] = StateProtocol.SP_P_START_PAIR;
  }

  public void start() {
    int programProtocl = initial.protocol();
    switch (programProtocl) {
    case StateProtocol.SP_P_START_REGUALR:
      this.execution();
      break;
    case StateProtocol.SP_P_START_PAIR:
      this.pair_execution();
      break;
    default:
      throw new UnsupportedOperationException();
    }
  }

  // ---------------------------------------------------------------------------
  // regular start
  // DECISION: 20180706 make the runtime Processes give hint on the primary
  // ---------------------------------------------------------------------------
  protected void execution() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} start to execution", this.simpleToString());
    }

    // put client logic here

    byte[] inputMessage = new byte[ProcessConfiguration.MESSAGE_DATA_SIZE];
    byte[] outputMessage = new byte[ProcessConfiguration.MESSAGE_DATA_SIZE];
    int maxTryTimes = -1;
    int totalTimes = -1;
    // change to a queue???
    // Map<Integer, Long> requestResponseMap = Maps.newHashMap();
    // long INVALID_SEQUENCE = -1L;

    long end = (long) (new Date().getTime() + ProcessConfiguration.PROCESS_mttpf);
    while (new Date().getTime() < end) {

      MoreBytes.zero(inputMessage);
      while (!get_available_message(inputMessage)) {
        MoreBytes.zero(inputMessage);
      }

      int protocol = Message.protocol(inputMessage);
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} call get_available_message got: [{}]{}", this.simpleToString(), protocol,
          MoreBytes.toHex(inputMessage));
      }

      switch (protocol) {
      case MessageProtocol.MP_CLIENT_KICKOFF: {
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} encounter kickoff message: [{}]{}", this.simpleToString(), protocol,
            MoreBytes.toHex(inputMessage));
        }

        int offset = Message.PROTOCOL_BYTE_SIZE;
        maxTryTimes = MoreBytes.toInt(inputMessage, offset, Integer.SIZE / Byte.SIZE);
        offset += Integer.SIZE / Byte.SIZE;
        totalTimes = MoreBytes.toInt(inputMessage, offset, Integer.SIZE / Byte.SIZE);
        if (totalTimes <= 0) totalTimes = 1; // default
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} execution parameter, maxTryTimes={}, totalTimes={}", //
            this.simpleToString(), maxTryTimes, totalTimes);
        }

        for (int requestIndex = 0; requestIndex < totalTimes; requestIndex++) {
          MoreBytes.zero(outputMessage);
          MoreBytes.putByte(outputMessage, 0, MessageProtocol.MP_TICKET_REQUEST);
          MoreBytes.putInt(outputMessage, Message.PROTOCOL_BYTE_SIZE, processId);
          boolean sendResult = environment.message_send(environment.primaryProcess, outputMessage);
          // if (sendResult) {
          // requestResponseMap.put(requestIndex, INVALID_SEQUENCE); // pending
          // }

          // retry
          if (maxTryTimes > 0 && !sendResult) {
            for (int i = 0; i < maxTryTimes; i++) {
              sendResult = environment.message_send(environment.primaryProcess, outputMessage);
              // if (sendResult) {
              // requestResponseMap.put(requestIndex, INVALID_SEQUENCE); // pending
              // break;
              // }
            }
          }
        }

        break;
      }
      case MessageProtocol.MP_TICKET_RESPONSE: {
        long sequence =
            MoreBytes.toLong(inputMessage, Message.PROTOCOL_BYTE_SIZE, Long.SIZE / Byte.SIZE);
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} encounter sequence response message: {}", this.simpleToString(), sequence);
        }
        break;
      }

      default: {
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} ENCOUNTER UNKNOWN MESSAGE PROTOCOL: {}", this.simpleToString(), protocol);
        }
        throw new UnsupportedOperationException();
      }
      } // END OF SWITCH

      break; // finish executing
    } // END OF WHILE LOOP
    LOG.info("{} encounter fault or finished!!!", this.simpleToString());
  }

  // ---------------------------------------------------------------------------
  // pair start
  // CONDITION: pair need to know each other
  // CONFITION: each one of pair need to know whether himself is primary
  // ---------------------------------------------------------------------------
  protected void pair_execution() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} start to pair execution", this.simpleToString());
    }

    long currentSequence = 0L;

    long end = (long) (new Date().getTime() + ProcessConfiguration.PROCESS_mttpf);

    long lastCheckpointTime = -1L;

    while (true) {
      long now = new Date().getTime();
      if (now > end) {
        LOG.info("{} encounter fault, DIE!!!", this.simpleToString());
        break;
      }
      // process take over
      if (!isPrimary && lastCheckpointTime != -1
          && now - lastCheckpointTime > MessageProtocol.MAX_DURATION_OF_MP_CHECK_POINT) {
        LOG.info("{} start to takeover.", this.simpleToString());
        environment.takeover(this);
      }

      byte[] inputMessage = new byte[ProcessConfiguration.MESSAGE_DATA_SIZE];
      MoreBytes.zero(inputMessage);
      while (!get_available_message(inputMessage)) {
        MoreBytes.zero(inputMessage);
      }

      int protocol = Message.protocol(inputMessage);
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} call get_available_message got: [{}]{}", this.simpleToString(), protocol,
          MoreBytes.toHex(inputMessage));
      }

      switch (protocol) {

      // case MessageProtocol.MP_IS_PRIMARY: {
      // isPrimary = true;
      // environment.primaryProcess = this;
      // if (LOG.isDebugEnabled()) {
      // LOG.debug("{} is the primary!", this.simpleToString());
      // }
      // break;
      // }

      case MessageProtocol.MP_CHECK_POINT: {
        if (isPrimary) {
          LOG.warn("{} as primary shall never receive checkpoint messages!", this.simpleToString());
        } else {
          if (LOG.isDebugEnabled()) {
            LOG.debug("{} encounter check point!", this.simpleToString());
          }
          long currentSeq = MoreBytes.getLong(inputMessage, 1);
          if (LOG.isDebugEnabled()) {
            LOG.debug("{} receive checkpoint: {}", this.simpleToString(), currentSeq);
          }
          lastCheckpointTime = now;
        }
        break;
      }

      case MessageProtocol.MP_TICKET_REQUEST: {
        if (!isPrimary) {
          LOG.warn("{} is not primary!", this.simpleToString());
        } else {
          if (LOG.isDebugEnabled()) {
            LOG.debug("{} encounter ticket request!", this.simpleToString());
          }
          currentSequence++;
          int senderProcessId = MoreBytes.toInt(inputMessage, 1, Integer.SIZE / Byte.SIZE);
          LOG.info("{} response to Process {} with {}", this.simpleToString(), senderProcessId,
            currentSequence);

          // checkpoint
          byte[] outputMessage = new byte[ProcessConfiguration.MESSAGE_DATA_SIZE];
          MoreBytes.putByte(outputMessage, 0, MessageProtocol.MP_CHECK_POINT);
          MoreBytes.putLong(outputMessage, 1, currentSequence);
          environment.message_send_to_pair(this, outputMessage);
          // reply
          outputMessage = new byte[ProcessConfiguration.MESSAGE_DATA_SIZE];
          MoreBytes.putByte(outputMessage, 0, MessageProtocol.MP_TICKET_RESPONSE);
          MoreBytes.putLong(outputMessage, 1, currentSequence);
          environment.message_send(senderProcessId, outputMessage);
        }

        break;
      }

      // unknown
      default: {
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} ENCOUNTER UNKNOWN MESSAGE PROTOCOL: {}", this.simpleToString(), protocol);
        }
        throw new UnsupportedOperationException();
      }
      }
    }
  }

  void reset() {
    current.program = initial.program;
    current.data = initial.data;
  }

  /**
   * send message to process
   * @param processId
   * @param value
   * @return
   */
  boolean message_send(int processId, byte[] value) {
    return environment.message_send(processId, value);
  }

  /**
   * get next input message
   * @param value OUT
   * @param msgStatus OUT, only index 0 is used
   * @return
   */
  boolean message_get(byte[] value, boolean[] msgStatus) {
    Message nextMessage = messageQueue.poll();
    if (nextMessage == null) return false;
    if (!nextMessage.status) return false;

    System.arraycopy(nextMessage.value, 0, value, 0, ProcessConfiguration.MESSAGE_DATA_SIZE);
    msgStatus[0] = nextMessage.status;

    return true;
  }

  /**
   * get next available message
   * @param value OUT
   * @return
   */
  boolean get_available_message(byte[] value) {
    boolean[] msgStatus = new boolean[] { false };
    boolean msgExists = false;

    while (!msgStatus[0]) {
      msgExists = message_get(value, msgStatus);
      if (!msgExists) break;
    }

    return msgExists;
  }

  private String simpleToString() {
    return "Process " + processId;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Process[" + processId + "]").append("\n");
    sb.append("initial: ").append(initial).append("\n");
    sb.append("current: ").append(current).append("\n");
    sb.append("messageQueue: ").append(messageQueue).append("\n");

    return sb.toString();
  }

}

class State {
  /**
   * <pre>
   * first byte
   * 0          regualr start
   * 1          start as pair
   * ...
   * </pre>
   */
  byte[] program = new byte[ProcessConfiguration.PROCESS_PROGRAM_SIZE];
  byte[] data = new byte[ProcessConfiguration.PROCESS_DATA_SIZE];

  int protocol() {
    return program[0];
  }

  State() {
  }

  State(byte[] program, byte[] data) {
    this.program = program;
    this.data = data;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    // sb.append("State").append("\n");
    sb.append("\n");
    sb.append("\t").append("program=").append("\n");
    Iterator<String> iter = Splitter.fixedLength(ProcessConfiguration.DUMP_LINE_SIZE)
        .split(MoreBytes.toHex(program)).iterator();
    while (iter.hasNext()) {
      sb.append("\t").append(iter.next()).append("\n");
    }
    sb.append("\t").append("data=").append("\n");
    iter = Splitter.fixedLength(ProcessConfiguration.DUMP_LINE_SIZE)//
        .split(MoreBytes.toHex(data)).iterator();
    while (iter.hasNext()) {
      sb.append("\t").append(iter.next()).append("\n");
    }

    return sb.toString();
  }

}