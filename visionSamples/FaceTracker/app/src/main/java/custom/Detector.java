package custom;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.SparseArray;

import arukoh.measure.core.Data;
import arukoh.measure.core.MeasureException;
import arukoh.measure.core.MeasureHolder;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;

public class Detector extends com.google.android.gms.vision.Detector<Face> {
    private com.google.android.gms.vision.Detector<Face> mDelegate;
    private Data.Generator mMeasure;
    private MeasureHolder.Callback mCallback;

    private static final int STATUS_READY = 0;
    private static final int STATUS_PROGRESS = 1;
    private static final int STATUS_FINISH = 2;
    private int mStatus;

    public Detector(com.google.android.gms.vision.Detector<Face> delegate, int holdTimeSec, MeasureHolder.Callback callback) {
        mDelegate = delegate;
        mMeasure = new Data.Generator(holdTimeSec);
        mCallback = callback;
        mStatus = STATUS_READY;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public SparseArray<Face> detect(Frame frame) {
        SparseArray<Face> faces =  mDelegate.detect(frame);
        switch (mStatus) {
            case STATUS_READY:
                if (faces.size() == 1) {
                    mStatus = STATUS_PROGRESS;
                    mMeasure.add(frame.getBitmap(), toFace(faces.valueAt(0)));
                }
                break;
            case STATUS_PROGRESS:
                if (faces.size() == 1) {
                    mMeasure.add(frame.getBitmap(), toFace(faces.valueAt(0)));
                    if (mMeasure.isCompleted()) {
                        mStatus = STATUS_FINISH;
                        try {
                            Data data = mMeasure.generate();
                            mCallback.measureCompleted(data);
                        } catch (MeasureException e) {
                            mCallback.measureFailed(e);
                        }
                    } else {
                        mCallback.measureProgress(mMeasure.getProgress());
                    }
                } else {
                    mStatus = STATUS_FINISH;
                    if (faces.size() > 0) {
                        mCallback.measureFailed(new ManyFacesException());
                    } else {
                        mCallback.measureFailed(new FadeOutException());
                    }
                }
                break;
            case STATUS_FINISH:
                break;
        }
        return faces;
    }

    public boolean isOperational() {
        return mDelegate.isOperational();
    }

    public boolean setFocus(int id) {
        return mDelegate.setFocus(id);
    }

    private arukoh.measure.core.Face toFace(Face face) {
        return new arukoh.measure.core.Face(
                face.getPosition().x,
                face.getPosition().y,
                face.getWidth(),
                face.getHeight());
    }

    public static class FadeOutException extends MeasureException {
        FadeOutException() {
            super("FadeOutException");
        }
    }

    public static class ManyFacesException extends MeasureException {
        ManyFacesException() {
            super("ManyFacesException");
        }
    }
}
