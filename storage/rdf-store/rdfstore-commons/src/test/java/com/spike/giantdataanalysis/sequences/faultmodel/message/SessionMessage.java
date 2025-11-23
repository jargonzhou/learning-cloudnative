package com.spike.giantdataanalysis.sequences.faultmodel.message;

import com.spike.giantdataanalysis.rdfstore.commons.lang.MoreBytes;

// ---------------------------------------------------------------------------
// Message Definition
// ---------------------------------------------------------------------------
class SessionMessage {
  Session session;
  MessageType messageType;
  byte[] value = new byte[MessageConfiguration.MESSAGE_DATA_SIZE];

  @Override
  public String toString() {
    return "SM[" + messageType + "]: " + MoreBytes.toHex(value);
  }

}

class Message {
  boolean status;
  byte[] value = new byte[MessageConfiguration.MESSAGE_DATA_SIZE];

  Message() {
  }

  Message(boolean status, byte[] value) {
    this.status = status;
    this.value = value;
  }

  @Override
  public String toString() {
    return "SM[" + status + "]: " + MoreBytes.toHex(value);
  }

}

enum MessageType {
  MSG_NEW, MSG_ACK, MSG_TAKEOVER
}