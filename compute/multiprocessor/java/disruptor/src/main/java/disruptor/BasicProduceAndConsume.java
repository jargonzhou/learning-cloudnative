package disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * Basic Produce and Consume.
 */
public class BasicProduceAndConsume {
    private static class LongEvent {
        private long value;

        public void set(long value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "LongEvent{" + "value=" + value + '}';
        }
    }

    private static class LongEventFactory implements EventFactory<LongEvent> {

        @Override
        public LongEvent newInstance() {
            return new LongEvent();
        }
    }

    private static class LongEventHandler implements EventHandler<LongEvent> {

        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("Event: " + event);
        }
    }

    private static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
        event.set(buffer.getLong(0));
    }

    // see LongEventHandler
    private static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println(event);
    }

    public static void main(String[] args) throws InterruptedException {
        // construct
        final int bufferSize = 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<>(
                LongEvent::new, // see LongEventFactory
                bufferSize,
                DaemonThreadFactory.INSTANCE,
                ProducerType.SINGLE,
                new BlockingWaitStrategy());

        // consume
        disruptor.handleEventsWith(BasicProduceAndConsume::handleEvent);

        // start
        disruptor.start();

        // publish
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; l < 10; l++) {
            bb.putLong(0, l);
            // EventTranslatorOneArg
            ringBuffer.publishEvent(BasicProduceAndConsume::translate, bb);
            Thread.sleep(500L);
        }
    }

}
