package arukoh.measure.core;

public class Progress {
    private long mRemaining;

    Progress(long remaining) {
        mRemaining = remaining;
    }

    public long getRemainingSecond() {
        return mRemaining;
    }
}
