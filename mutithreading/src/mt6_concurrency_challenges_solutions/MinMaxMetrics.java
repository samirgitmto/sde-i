package mt6_concurrency_challenges_solutions;

import java.util.concurrent.atomic.AtomicLong;

public class MinMaxMetrics {
    
    // Add all necessary member variables
    private AtomicLong min;
    private AtomicLong max;

    /**
     * Initializes all member variables
     */
    public MinMaxMetrics() {
        // Add code here
        this.min = new AtomicLong(Long.MAX_VALUE);
        this.max = new AtomicLong(Long.MIN_VALUE);
    }

    /**
     * Adds a new sample to our metrics.
     */
    public void addSample(long newSample) {
        // Add code here
        // Update minimum
        long currentMin = min.get();
        while (newSample < currentMin && !min.compareAndSet(currentMin, newSample)) {
            currentMin = min.get();
        }
        
        // Update maximum
        long currentMax = max.get();
        while (newSample > currentMax && !max.compareAndSet(currentMax, newSample)) {
            currentMax = max.get();
        }
    }

    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {
        // Add code here
        long currentMin = min.get();
        return currentMin == Long.MAX_VALUE ? 0 : currentMin;
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {
        // Add code here
        long currentMax = max.get();
        return currentMax == Long.MIN_VALUE ? 0 : currentMax;
    }
}