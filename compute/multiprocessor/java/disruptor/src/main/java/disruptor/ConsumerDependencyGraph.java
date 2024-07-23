package disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * Consumer Dependency Graph.
 */
public class ConsumerDependencyGraph {
    private static class AppEvent<T> {
        T val;

        public void set(T val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "AppEvent: " + val;
        }

        public void clear() {
            this.val = null;
        }
    }

    private static class ApplicationConsumer implements EventHandler<AppEvent<Long>> {

        @Override
        public void onEvent(AppEvent<Long> event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("Application: " + event);
            event.clear();
        }
    }

    private static class JournalConsumer implements EventHandler<AppEvent<Long>> {

        @Override
        public void onEvent(AppEvent<Long> event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("Journal: " + event);
        }
    }

    private static class ReplicationConsumer implements EventHandler<AppEvent<Long>> {

        @Override
        public void onEvent(AppEvent<Long> event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("Replication: " + event);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        // construct
        final int bufferSize = 1024;
        Disruptor<AppEvent<Long>> disruptor = new Disruptor<>(
                AppEvent::new,
                bufferSize,
                DaemonThreadFactory.INSTANCE,
                ProducerType.SINGLE,
                new BlockingWaitStrategy());

        // consume
        JournalConsumer journalConsumer = new JournalConsumer();
        ReplicationConsumer replicationConsumer = new ReplicationConsumer();
        ApplicationConsumer applicationConsumer = new ApplicationConsumer();
        disruptor.handleEventsWith(journalConsumer, replicationConsumer)
                .then(applicationConsumer);

//        disruptor.handleEventsWith(journalConsumer, replicationConsumer);
//        disruptor.after(journalConsumer, replicationConsumer).then(applicationConsumer);

        // start
        disruptor.start();

        // publish
        RingBuffer<AppEvent<Long>> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; l < 10; l++) {
            bb.putLong(0, l);
            // EventTranslatorOneArg
            ringBuffer.publishEvent(
                    (event, sequence, buffer) -> event.set(buffer.getLong(0)),
                    bb);
            Thread.sleep(500L);
        }
    }
}
