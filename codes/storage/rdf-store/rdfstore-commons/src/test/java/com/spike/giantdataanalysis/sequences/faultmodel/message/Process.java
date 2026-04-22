package com.spike.giantdataanalysis.sequences.faultmodel.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.spike.giantdataanalysis.rdfstore.commons.lang.MoreBytes;

class Process {
  private static final Logger LOG = LoggerFactory.getLogger(Process.class);

  final int processId;

  Messages environment;

  // other process id => Session
  Map<Integer, Session> sessionMap = new ConcurrentHashMap<>(MessageConfiguration.PROCESS_NUMBER);
  // input message queue list, index: other processId, value: input messages from other processId
  List<ConcurrentLinkedQueue<Message>> ackedMessageQueues =
      new ArrayList<>(MessageConfiguration.PROCESS_NUMBER);

  // MOCKING: session message queue
  ConcurrentLinkedQueue<SessionMessage> inputSessionMessageQueue = new ConcurrentLinkedQueue<>();

  public Process(int processId) {
    this.processId = processId;
    initialize();
  }

  public void registerSession(int targetProcessId) {
    Session session = new Session(processId, targetProcessId);
    sessionMap.put(targetProcessId, session);
  }

  private void initialize() {
    Session session = new Session(processId, processId);
    sessionMap.put(processId, session);

    for (int i = 0; i < MessageConfiguration.PROCESS_NUMBER; i++) {
      ackedMessageQueues.add(new ConcurrentLinkedQueue<Message>());
    }
  }

  void injectEnvironment(Messages messages) {
    this.environment = messages;
  }

  void start() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} start to execution", this.simpleToString());
    }

    byte[] inputMessage = new byte[MessageConfiguration.MESSAGE_DATA_SIZE];

    // kickoff
    final byte[] outputMessage = new byte[MessageConfiguration.MESSAGE_DATA_SIZE];
    MoreBytes.putInt(outputMessage, 0, processId);
    for (Integer toProcessId : sessionMap.keySet()) {
      if (toProcessId == processId) continue;
      reliable_send_message(toProcessId, outputMessage);
    }

    long end = (long) (new Date().getTime() + MessageConfiguration.PROCESS_mttpf);
    while (new Date().getTime() < end) {

      MoreBytes.zero(inputMessage);
      while (!get_available_message(inputMessage)) {
        MoreBytes.zero(inputMessage);
      }
      LOG.info("{} RECEIVED MESSAGE: {}", this.simpleToString(), MoreBytes.toHex(inputMessage));

      // send again
      for (Integer toProcessId : sessionMap.keySet()) {
        if (toProcessId.equals(processId)) continue;
        reliable_send_message(toProcessId, outputMessage);
      }

      try {
        Thread.sleep(100L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    } // END OF WHILE LOOP
    LOG.info("{} encounter fault or finished!!!", this.simpleToString());
  }

  Thread startListen() {
    return new Thread(new Runnable() {
      @Override
      public void run() {
        listen();
      }
    });
  }

  // ---------------------------------------------------------------------------
  // GET MESSAGE FROM MESSAGE QUEUE
  // ---------------------------------------------------------------------------
  /**
   * get next input message
   * @param value OUT
   * @param msgStatus OUT, only index 0 is used
   * @return
   */
  boolean acked_message_get(byte[] value, boolean[] msgStatus) {
    for (ConcurrentLinkedQueue<Message> queue : ackedMessageQueues) {
      Message nextMessage = queue.poll();
      if (nextMessage == null || !nextMessage.status) {
        continue;
      }

      System.arraycopy(nextMessage.value, 0, value, 0, MessageConfiguration.MESSAGE_DATA_SIZE);
      msgStatus[0] = nextMessage.status;
      return true;
    }

    return false;
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
      msgExists = acked_message_get(value, msgStatus);
      if (!msgExists) break;
    }

    return msgExists;
  }

  // ---------------------------------------------------------------------------
  // MOCKING SESSION AVAILABLE TRIGGER
  // ---------------------------------------------------------------------------
  // get next available session
  private boolean session_get(Session[] session, MessageType[] messageType, byte[] pageValue) {
    SessionMessage sessionMessage = inputSessionMessageQueue.poll();
    if (sessionMessage == null) return false;

    session[0] = sessionMessage.session;
    messageType[0] = sessionMessage.messageType;
    System.arraycopy(sessionMessage.value, 0, pageValue, 0, MessageConfiguration.MESSAGE_DATA_SIZE);
    return true;
  }

  // ---------------------------------------------------------------------------
  // SEND MESSAGE THROUGH SESSION
  // ---------------------------------------------------------------------------

  // send messaage to process
  void reliable_send_message(int processId, byte[] pageValue) {
    Session thisToOtherSession = sessionTo(processId);
    thisToOtherSession.out++;
    // checkpoint
    checkpoint(thisToOtherSession, pageValue);

    // if failed, when wait-for-ack window passed, do the retransition
    do {
      session_send(thisToOtherSession, MessageType.MSG_NEW, pageValue);

      try {
        Thread.sleep(MessageConfiguration.PROCESS_WAIT_ACK_DURATION_MS);
      } catch (InterruptedException e) {
      }
    } while (thisToOtherSession.out != thisToOtherSession.ack);
  }

  // send message through session
  // WARN: not maintained out sequence in sessionF
  private boolean session_send(Session session, MessageType messageType, byte[] pageValue) {
    return environment.message_send(//
      session.sourceProcessId, session.targetProcessId, messageType, pageValue);
  }

  // ---------------------------------------------------------------------------
  // WRAPPER SESSION AROUND REGULAR MESSAGE
  // ---------------------------------------------------------------------------
  // listen to input message
  private void listen() {

    while (true) {

      // do the snapshot
      if (LOG.isDebugEnabled()) {
        LOG.debug("\n{}", this.toString());
      }

      Session[] session = new Session[1];
      MessageType[] messageType = new MessageType[1];
      byte[] pageValue = new byte[MessageConfiguration.MESSAGE_DATA_SIZE];

      // get next session message: session target must be myself!!!
      while (session_get(session, messageType, pageValue)) {
        Session session0 = session[0];
        int otherPid = session0.sourceProcessId;
        Session mySession = sessionTo(otherPid);
        long seqno = session0.out;
        if (LOG.isDebugEnabled()) {
          LOG.debug("\n{}[Listen] got session message:  {}[{}] {}", this.simpleToString(),
            session[0], messageType[0], MoreBytes.toHex(pageValue));
        }

        switch (messageType[0]) {
        case MSG_NEW: {

          if (mySession.in == seqno - 1) {
            Message message = new Message(true, pageValue);
            ackedMessageQueues.get(otherPid).add(message);
            mySession.in++;
            checkpoint(session[0], pageValue);
          }
          session_send(mySession, MessageType.MSG_ACK, pageValue);

          break;
        }

        case MSG_ACK: {
          if (session0.in == mySession.out) { // does not make any sense!!!
            mySession.ack++;
          }
          break;
        }

        case MSG_TAKEOVER: {
          // TODO
          break;
        }
        default: {
          break;
        }
        }
      }

      // wait
      try {
        Thread.sleep(MessageConfiguration.MESSAGE_LISTEN_WAIT_DURATION);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

  void checkpoint(Session session, byte[] pageValue) {
    // throw new UnsupportedOperationException();
    if (LOG.isDebugEnabled()) {
      LOG.debug("checkpointing...");
    }
  }

  private Session sessionTo(int processId) {
    if (processId > MessageConfiguration.PROCESS_NUMBER || processId < 0)
      throw new RuntimeException();

    return sessionMap.get(processId);
  }

  private String simpleToString() {
    return "Process " + processId;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Process[" + processId + "]").append("\n");

    sb.append("sessionMap: ");
    for (Integer key : sessionMap.keySet()) {
      if (key.equals(processId)) {
        // classified inner information
        sb.append("Session[" + key + " => " + key + ", ******] ");
      } else {
        sb.append(sessionMap.get(key) + " ");
      }
    }
    sb.append("\n");

    sb.append("inputSessionMessageQueue: ").append(inputSessionMessageQueue).append("\n");
    sb.append("ackedMessageQueues: ").append(ackedMessageQueues).append("\n");
    return sb.toString();
  }
}

class State {
  byte[] program = new byte[MessageConfiguration.PROCESS_PROGRAM_SIZE];
  byte[] data = new byte[MessageConfiguration.PROCESS_DATA_SIZE];

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
    Iterator<String> iter = Splitter.fixedLength(MessageConfiguration.DUMP_LINE_SIZE)
        .split(MoreBytes.toHex(program)).iterator();
    while (iter.hasNext()) {
      sb.append("\t").append(iter.next()).append("\n");
    }
    sb.append("\t").append("data=").append("\n");
    iter = Splitter.fixedLength(MessageConfiguration.DUMP_LINE_SIZE)//
        .split(MoreBytes.toHex(data)).iterator();
    while (iter.hasNext()) {
      sb.append("\t").append(iter.next()).append("\n");
    }

    return sb.toString();
  }

}
