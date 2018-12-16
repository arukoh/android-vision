package arukoh.measure.core;

public interface MeasureHolder {

    interface Callback {
        void measureCompleted(Data data);

        void measureProgress(Progress progress);

        void measureFailed(MeasureException e);
    }
}
