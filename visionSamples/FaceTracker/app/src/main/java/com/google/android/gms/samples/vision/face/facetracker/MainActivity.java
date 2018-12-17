package com.google.android.gms.samples.vision.face.facetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_TRACK = 1;

    private TextView mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScore = (TextView) findViewById(R.id.textViewScore);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();

        switch (requestCode) {
            case REQUEST_TRACK:
                if (resultCode == RESULT_OK) {
                    int score = bundle.getInt("SCORE", 0);
                    mScore.setText(String.valueOf(score));
                } else if (resultCode == RESULT_CANCELED) {
                    String msg = bundle.getString("TYPE", "");
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    public void startFaceTracker(View view) {
        Intent intent = new Intent(this, FaceTrackerActivity.class);
        startActivityForResult(intent, REQUEST_TRACK);
    }
}
