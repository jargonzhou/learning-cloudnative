package com.spike.nio2.async.file;

import com.spike.Constants;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AsyncFileReadWithCompletionHandler {
    static Thread currentThread;

    public static void main(String[] args) {
        ByteBuffer bb = ByteBuffer.allocate(100);
        Path path = Paths.get(Constants.USER_HOME, "a.txt");

        try (AsynchronousFileChannel channel =
                     AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            currentThread = Thread.currentThread();

            String attachment = "read operation status";
            channel.read(bb, 0, attachment, new MyCompletionHandler());

            // wait for read operation done
            try {
                currentThread.join();
            } catch (InterruptedException ignore) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        bb.flip();
        System.out.println(Charset.forName(Constants.DEFAULT_FILE_ENCODING).decode(bb));
        bb.clear();
    }

    private static class MyCompletionHandler implements CompletionHandler<Integer, Object> {

        @Override
        public void completed(Integer result, Object attachment) {
            System.out.println(attachment);
            System.out.println("read " + result + " bytes");

            currentThread.interrupt();
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            System.out.println(attachment);
            System.out.println("failed: " + exc);

            currentThread.interrupt();
        }
    }

}
