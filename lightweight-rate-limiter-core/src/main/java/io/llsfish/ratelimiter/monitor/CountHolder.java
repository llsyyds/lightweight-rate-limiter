package io.llsfish.ratelimiter.monitor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Redick01
 */
public class CountHolder {

    private final AtomicInteger count = new AtomicInteger(0);

    /**
     * metrics count.
     */
    public void add() {
        count.addAndGet(1);
    }

    /**
     * get metrics count.
     * @return count
     */
    public Integer getCount() {
        return this.count.get();
    }
}
