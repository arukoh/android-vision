package custom;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;

import custom.core.Data;
import custom.core.MeasureException;
import custom.core.MeasureHolder;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;

public class Detector extends com.google.android.gms.vision.Detector<Face> {
    private static final String TAG = "Detector";

    private com.google.android.gms.vision.Detector<Face> mDelegate;
    private Data.Generator mMeasure;
    private MeasureHolder.Callback mCallback;

    public Detector(com.google.android.gms.vision.Detector<Face> delegate, int holdTimeSec, MeasureHolder.Callback callback) {
        mDelegate = delegate;
        mMeasure = new Data.Generator(holdTimeSec);
        mCallback = callback;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public SparseArray<Face> detect(Frame frame) {
        Log.i(TAG, "onFrame()");
        SparseArray<Face> faces =  mDelegate.detect(frame);

        mMeasure.add(frame.getBitmap(), toFaces(faces));

        if (mMeasure.isCompleted()) {
            try {
                Data data = mMeasure.generate();
                mCallback.measureCompleted(data);
            } catch (MeasureException e) {
                mCallback.measureFailed(e);
            }
        }

        return faces;
    }

    public boolean isOperational() {
        return mDelegate.isOperational();
    }

    public boolean setFocus(int id) {
        return mDelegate.setFocus(id);
    }

    private custom.core.Face[] toFaces(SparseArray<Face> a) {
        return null;
    }
}
