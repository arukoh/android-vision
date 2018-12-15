package custom.core;

public interface MeasureHolder {

    interface Callback {
        void measureCompleted(Data data);

        void measureFailed(MeasureException e);
    }
}
