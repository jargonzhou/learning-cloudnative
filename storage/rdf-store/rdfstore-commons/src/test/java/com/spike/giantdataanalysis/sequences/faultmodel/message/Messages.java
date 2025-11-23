package com.spike.giantdataanalysis.sequences.faultmodel.message;

import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Messages {
  private static final Logger LOG = LoggerFactory.getLogger(Messages.class);

  final Process[] processes = new Process[MessageConfiguration.PROCESS_NUMBER];
  private final Random random = new Random(new Date().getTime());

  public Messages() {
  }

  public void inititalze() {
    for (int i = 0; i < MessageConfiguration.PROCESS_NUMBER; i++) {
      processes[i] = new Process(i);
      processes[i].injectEnvironment(this);
    }
  }

  // ---------------------------------------------------------------------------
  // MOCKING CHANNEL, ALSO ACT AS A KICKOFF
  // ---------------------------------------------------------------------------
  boolean message_send(int fromProcessId, int toProcessId, MessageType messageType, byte[] value) {

    if (fromProcessId >= MessageConfiguration.PROCESS_NUMBER || fromProcessId < 0) return false;
    Process fromProcess = processes[fromProcessId];
    if (toProcessId >= MessageConfiguration.PROCESS_NUMBER || toProcessId < 0) return false;
    Process toProcess = processes[toProcessId];
    if (fromProcess == null || toProcess == null) return false;

    // mock message lost or disrupt
    if (random.nextDouble() < MessageConfiguration.MESSAGE_pmf) return false;

    SessionMessage message = new SessionMessage();
    message.session = fromProcess.sessionMap.get(toProcessId);
    message.messageType = messageType;
    System.arraycopy(value, 0, message.value, 0, MessageConfiguration.MESSAGE_DATA_SIZE);
    toProcess.inputSessionMessageQueue.add(message); // send to session message queue
    if (LOG.isDebugEnabled()) {
      LOG.debug("\nProcess {} => {} with message = {}", fromProcessId, toProcessId, message);
    }

    // mock duplicated message
    if (random.nextDouble() < MessageConfiguration.MESSAGE_pmd) {
      message_send(fromProcessId, toProcessId, messageType, value);
    }
    return true;
  }

  public static void main(String[] args) {
    // kickoff process

    MessageConfiguration.PROCESS_NUMBER = 2;
    MessageConfiguration.MESSAGE_DATA_SIZE = 8;

    final Messages messages = new Messages();
    messages.inititalze();

    // handshake
    final Process process0 = messages.processes[0];
    final Process process1 = messages.processes[1];
    process0.registerSession(1);
    process1.registerSession(0);

    Thread pair0JavaThread = new Thread(new Runnable() {
      @Override
      public void run() {
        process0.start();
      }
    });
    Thread pair0ListenJavaThread = process0.startListen();

    Thread pair1JavaThread = new Thread(new Runnable() {
      @Override
      public void run() {
        process1.start();
      }
    });
    Thread pair1ListenJavaThread = process1.startListen();

    Thread[] threads = new Thread[] { pair0JavaThread, pair0ListenJavaThread, pair1JavaThread,
        pair1ListenJavaThread };
    for (Thread t : threads) {
      t.start();
    }

    try {
      Thread.sleep(500L);

      for (Thread t : threads) {
        t.join();
      }
    } catch (InterruptedException e) {
    }
  }

}
