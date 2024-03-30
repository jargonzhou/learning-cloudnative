package com.spike.nio2.async.file;

import com.spike.Constants;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncFileReadWithFutureUsingTimeout {

    public static void main(String[] args) {
        ByteBuffer bb = ByteBuffer.allocate(100);
        Path path = Paths.get(Constants.USER_HOME, "a.txt");

        Future<Integer> future = null;
        int bytes = 0;
        try (AsynchronousFileChannel channel =
                     AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            future = channel.read(bb, 0);

            // bytes = future.get(1, TimeUnit.NANOSECONDS);
            bytes = future.get(10, TimeUnit.SECONDS);

            if (future.isDone()) {
                System.out.println("done! ");
                System.out.println("read " + bytes + " bytes");
            }
        } catch (TimeoutException e) {
            if (future != null) {
                future.cancel(true);
            }
            System.out.println("cancelled?" + future.isCancelled());
        } catch (Exception e) {
            e.printStackTrace();
        }

        bb.flip();
        System.out.println(Charset.forName(Constants.DEFAULT_FILE_ENCODING).decode(bb));
        bb.clear();
    }

}
