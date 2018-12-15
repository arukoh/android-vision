package custom.core;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Data {
    private static String KEY1 = "KEY1";
    private JSONObject obj;

    Data(JSONObject obj) {
        this.obj = obj;
    }

    public int getValue1() {
        return getInt(KEY1);
    }

    private int getInt(String key) {
        try {
            return obj.getInt(key);
        } catch (JSONException e) {
            // e.printStackTrace();
            // TODO
        }
        return 0;
    }

    @Override
    public String toString() {
        try {
            return obj.toString(4);
        } catch (JSONException e) {
            return obj.toString();
        }
    }

    public static class Generator {

        private int mHoldTimeSec;
        private long mStartTime;
        private List<Float> mData;

        private float mCount;

        public Generator(int holdTimeSec) {
            mHoldTimeSec = holdTimeSec;
            mData = Collections.synchronizedList(new ArrayList<>());

            mCount = 0;
        }

        public void add(Bitmap bitmap, Face[] faces) {
            float value = ++mCount;

            if (mData.size() == 0) {
                mStartTime = getCurrentEpochSecond();
            }
            if (!isCompleted()) {
                mData.add(value);
            }
        }

        public boolean isCompleted() {
            long current = getCurrentEpochSecond();
            return mStartTime + mHoldTimeSec < current;
        }

        public Data generate() throws MeasureException {
            HttpClient client = new HttpClient();
            try {
                JSONObject res = client.request(mData);
                return new Data(res);
            } catch (Exception e) {
                throw new MeasureException(e.getMessage());
            }
        }

        @TargetApi(Build.VERSION_CODES.O)
        private long getCurrentEpochSecond() {
            return Instant.now().getEpochSecond();
        }
    }
}
