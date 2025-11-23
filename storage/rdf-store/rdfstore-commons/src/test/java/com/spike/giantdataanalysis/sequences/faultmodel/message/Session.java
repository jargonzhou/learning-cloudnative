package com.spike.giantdataanalysis.sequences.faultmodel.message;

/** source =[message]=> target */
class Session {
  String sessionId; // session id
  final int sourceProcessId;// source process id
  final int targetProcessId;// target process id

  long in; // latest input message sequence
  long out; // latest output message sequence
  long ack; // latest acked message sequence

  Session(int sourceProcessId, int targetProcessId) {
    this.sourceProcessId = sourceProcessId;
    this.targetProcessId = targetProcessId;
    this.sessionId = sourceProcessId + " => " + targetProcessId;
    this.in = 0L;
    this.out = 0L;
    this.ack = 0L;
  }

  @Override
  public String toString() {
    return "Session[" + sessionId + "," + " in:" + in + ",out:" + out + ",ack:" + ack + "]";
  }

}
