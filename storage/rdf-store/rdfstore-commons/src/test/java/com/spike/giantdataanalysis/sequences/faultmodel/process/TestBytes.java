package com.spike.giantdataanalysis.sequences.faultmodel.process;

import com.spike.giantdataanalysis.rdfstore.commons.lang.MoreBytes;

// can we just support a byte message encoder/decoder???
// now we have Stringable representation. - 20180713
public class TestBytes {

  public static void main(String[] args) {
    byte[] kickOffMessage = new byte[ProcessConfiguration.MESSAGE_DATA_SIZE];
    int offset = 0;
    MoreBytes.putByte(kickOffMessage, offset, MessageProtocol.MP_CLIENT_KICKOFF);
    System.out.println(MoreBytes.toHex(kickOffMessage));
    offset += Message.PROTOCOL_BYTE_SIZE;
    MoreBytes.putInt(kickOffMessage, offset, 3); // retry times
    System.out.println(MoreBytes.toHex(kickOffMessage));
    offset += Integer.SIZE / Byte.SIZE;
    MoreBytes.putInt(kickOffMessage, offset, 10); // total send times
    System.out.println(MoreBytes.toHex(kickOffMessage));

    offset = Message.PROTOCOL_BYTE_SIZE;
    int maxTryTimes = MoreBytes.getInt(kickOffMessage, offset);
    offset += Integer.SIZE / Byte.SIZE;
    int totalTimes = MoreBytes.getInt(kickOffMessage, offset);
    if (totalTimes <= 0) totalTimes = 1; // default

    System.out.println("maxTryTimes=" + maxTryTimes);
    System.out.println("totalTimes=" + totalTimes);
  }
}
