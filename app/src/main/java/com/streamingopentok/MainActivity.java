package com.streamingopentok;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity implements View.OnClickListener {

    public static final String APP_NAME = "streaming";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                startStreaming();
                break;
            case R.id.end:
                break;
        }
    }

    public void startStreaming() {


        Intent intent = new Intent(getApplicationContext(), StreamerActivity.class);

        startActivity(intent);
    }
}
