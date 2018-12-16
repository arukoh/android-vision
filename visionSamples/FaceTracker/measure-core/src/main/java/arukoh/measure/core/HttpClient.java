package arukoh.measure.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

class HttpClient {
    private Random r;

    HttpClient() {
        r = new Random();
    }

    JSONObject request(List<Float> data) throws JSONException {
        String res = post(data);
        return new JSONObject(res);
    }

    private String post(List<Float> data) throws JSONException {
        // TODO request
        JSONObject obj = new JSONObject();
        int i = 1;
        for (float v : data) {
            obj.put("KEY" + i++, (int) v);
        }
        obj.put("SCORE", String.valueOf(r.nextInt(100) + 1));
        return obj.toString();
    }
}
