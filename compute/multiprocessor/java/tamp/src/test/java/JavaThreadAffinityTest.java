import net.openhft.affinity.Affinity;
import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinityStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see <a href="https://man7.org/linux/man-pages/man2/sched_setaffinity.2.html">sched_setaffinity(2)</a>
 * @see <a href="https://github.com/OpenHFT/Java-Thread-Affinity">OpenHFT/Java-Thread-Affinity</a>
 */
public class JavaThreadAffinityTest {
    private static final Logger LOG = LoggerFactory.getLogger(JavaThreadAffinityTest.class);

    public static void main(String[] args) {
        LOG.info("Affinity: {}", Affinity.getAffinity());
        LOG.info("CPU Layout: {}", AffinityLock.cpuLayout());

        final int cpuId = 5;
        try (AffinityLock lock = AffinityLock.acquireLock(cpuId)) {
            LOG.info("CPUID: {}", lock.cpuId());
            LOG.info("Current locks: {}", AffinityLock.dumpLocks());

            Thread child = new Thread(new Runnable() {
                @Override
                public void run() {
                    try (AffinityLock childLock = lock.acquireLock(AffinityStrategies.SAME_SOCKET, AffinityStrategies.ANY)) {
                        LOG.info("CPUID: {}", childLock.cpuId());
                        LOG.info("Current locks: {}", AffinityLock.dumpLocks());
                        while (true) {
                        }
                    }
                }
            }, "child");
            child.start();

            while (true) { // simulate busy work
//                LOG.info(AffinityLock.dumpLocks());
            }
        }
    }
}
