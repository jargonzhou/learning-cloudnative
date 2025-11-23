package com.spike.giantdataanalysis.sequences.faultmodel.process;

import com.spike.giantdataanalysis.rdfstore.commons.lang.MoreBytes;

/**
 * The message abstration.
 * 
 * <pre>
 * Message format
 * 
 * firt byte
 * 0      you are primary - no need now 20180707     
 * 1      checkpoint message
 * 2      regular kick off: retry times, total send times
 * ...      
 * 126    ticket request: request process id
 * 127    ticket request response: sequence
 * </pre>
 */
class Message {

  static int PROTOCOL_BYTE_SIZE = 1;

  static int protocol(byte[] b) {
    return b[0];
  }

  boolean status;
  byte[] value = new byte[ProcessConfiguration.MESSAGE_DATA_SIZE];

  Message() {
  }

  Message(boolean status) {
    this.status = status;
  }

  Message(boolean status, byte[] value) {
    this.status = status;
    this.value = value;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("[").append(status);
    sb.append(", ").append(MoreBytes.toHex(value));
    sb.append("]");

    return sb.toString();
  }
}
