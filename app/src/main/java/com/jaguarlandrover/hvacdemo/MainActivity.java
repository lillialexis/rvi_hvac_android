package com.jaguarlandrover.hvacdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import android.os.Handler;
import java.util.logging.LogRecord;


public class MainActivity extends ActionBarActivity
{
    private final static String TAG = "HVACDemo:MainActivity";

    private boolean mHazardsAreFlashing = false;
    private ImageButton mHazardButton;
    private boolean mHazardsImageIsOn;
    private final Handler mHandler = new Handler();
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHazardButton = (ImageButton) findViewById(R.id.hazard_button);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void hazardButtonPressed(View view) {
        Log.d(TAG, "hazardButtonPressed");
        mHazardsAreFlashing = !mHazardsAreFlashing;

        if (mHazardsAreFlashing) {
            startAnimation();
        } else { // if (!mHazardsAreFlashing)
            stopAnimation();
            mHazardButton.setImageResource(R.drawable.hazard_off);
        }
    }


    private void stepAnimation() {
        Log.d(TAG, "stepAnimation");

        if (mHazardsImageIsOn)
            mHazardButton.setImageResource(R.drawable.hazard_off);
        else
            mHazardButton.setImageResource(R.drawable.hazard_on);

        mHazardsImageIsOn = !mHazardsImageIsOn;
    }

    public void startAnimation() {
        Log.d(TAG, "startAnimation");

        mHandler.postDelayed(mRunnable = new Runnable()
        {
            @Override
            public void run() {
/* do what you need to do */
                stepAnimation();
/* and here comes the "trick" */
                mHandler.postDelayed(this, 0);
            }
        }, 1000);
    }

    public void stopAnimation() {
        Log.d(TAG, "stopAnimation");

        if (mRunnable != null)
            mHandler.removeCallbacks(mRunnable);
    }
}
