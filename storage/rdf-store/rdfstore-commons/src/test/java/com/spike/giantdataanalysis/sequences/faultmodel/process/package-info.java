/**
 * features or bugs
 * 
 * <pre>
 * 0 Protocols
 * Message format:
 * firt byte
 * 0      you are primary - no need now 20180707     
 * 1      checkpoint message
 * 2      regular kick off: retry times, total send times
 * ...      
 * 126    ticket request: request process id
 * 127    ticket request response: sequence
 * 
 * State format:
 * first byte
 * 0          regualr start
 * 1          start as pair
 * ...
 * 
 * 1 Process.messageQueue
 * not thread safe! - now it is! 20180707
 * not FIFO! - now it is! use {@link java.util.concurrent.ConcurrentLinkedQueue<Message>} :-) 20180707
 * Notes: i am not really good at pointer/array/linkedlist operations :-(
 * 
 * 2 process pair
 * not support takeover!!! - now it is! 20180707 using <code>Processes.primaryProcess</code>
 * 
 * 3 Process.start()
 * not support codeing and cache, i.e. the State is hard coded
 * Notes: well, it's rather too complicated for this prototype.
 * </pre>
 */
package com.spike.giantdataanalysis.sequences.faultmodel.process;