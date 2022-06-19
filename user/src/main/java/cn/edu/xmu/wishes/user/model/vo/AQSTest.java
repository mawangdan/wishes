package cn.edu.xmu.wishes.user.model.vo;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.stream.IntStream;

public class AQSTest extends AbstractQueuedSynchronizer {
    volatile boolean canAcquire = true;
    AtomicInteger tryAcquireTimes = new AtomicInteger(0);
    @Override
    protected boolean tryAcquire(int arg) {
        System.out.println(tryAcquireTimes.incrementAndGet());
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (canAcquire && !hasQueuedPredecessors() &&
                    compareAndSetState(0, arg)) {
                setExclusiveOwnerThread(current);
                canAcquire = false;
                return true;
            }
        }
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + arg;
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }

    @Override
    protected final boolean tryRelease(int releases) {
        int c = getState() - releases;
        if (Thread.currentThread() != getExclusiveOwnerThread())
            throw new IllegalMonitorStateException();
        boolean free = false;
        if (c == 0) {
            free = true;
            setExclusiveOwnerThread(null);
        }
        setState(c);
        return free;
    }

    public void lock() {
        acquire(1);
    }
    public void unlock() {
        release(1);
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch waitGroup = new CountDownLatch(3);
        ExecutorService threadPool = new ThreadPoolExecutor(3, 3, 0, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadFactory() {
            AtomicInteger id = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                String name = "from AQSTest:" + id.incrementAndGet();
                Thread thread = new Thread(null, r, name, 0, false);
                return thread;
            }
        });
        AQSTest lock = new AQSTest();
        IntStream.range(0, 3).forEach(i -> threadPool.execute(getRunnable(lock, waitGroup)));
        waitGroup.await();
        threadPool.shutdown();
    }

    private static Runnable getRunnable(AQSTest lock, CountDownLatch waitGroup) {
        return () -> {
            lock.lock();
            try {
                System.out.println("in " + Thread.currentThread().getName());
            } finally {
                waitGroup.countDown();
                lock.unlock();
            }
        };
    }
}
