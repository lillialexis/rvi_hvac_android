package com.jaguarlandrover.hvacdemo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import android.os.Handler;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity
{
    private final static String TAG = "HVACDemo:MainActivity";

    private boolean mHazardsAreFlashing = false;
    private boolean mHazardsImageIsOn;

    private ImageButton mHazardButton;

    private final Handler mHandler = new Handler();
    private Runnable      mRunnable;

    private HashMap<Integer, Integer> mButtonImagesOff;
    private HashMap<Integer, Integer> mButtonImagesOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonImagesOff = MainActivityUtil.initializeButtonOffHashMap();
        mButtonImagesOn  = MainActivityUtil.initializeButtonOnHashMap();

        mHazardButton    = (ImageButton) findViewById(R.id.hazard_button);

        configurePicker((NumberPicker) findViewById(R.id.left_temp_picker));
        configurePicker((NumberPicker) findViewById(R.id.right_temp_picker));

        configureSeekBar((SeekBar) findViewById(R.id.fan_power_seekbar));
    }

    private void configureSeekBar(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, Util.getMethodName());

                // TODO: Call backend code to update RVI node
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void configurePicker(NumberPicker picker) {
        // TODO - Barbara, see this on making the picker not so ugly: http://stackoverflow.com/questions/15031624/how-to-change-number-picker-style-in-android

        picker.setMinValue(15);
        picker.setMaxValue(26);

        picker.setWrapSelectorWheel(false);
        picker.setDisplayedValues( new String[] { "LO", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "HI" } );

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, Util.getMethodName());

                // TODO: Call backend code to update RVI node
            }
        });
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void stepAnimation() {
        Log.d(TAG, Util.getMethodName());

        if (mHazardsImageIsOn)
            mHazardButton.setImageResource(R.drawable.hazard_off);
        else
            mHazardButton.setImageResource(R.drawable.hazard_on);

        mHazardsImageIsOn = !mHazardsImageIsOn;
    }

    public void startAnimation() {
        Log.d(TAG, Util.getMethodName());

        mHandler.postDelayed(mRunnable = new Runnable()
        {
            @Override
            public void run() {
                /* do what you need to do */
                stepAnimation();

                /* and here comes the "trick" */
                mHandler.postDelayed(this, 1000);
            }
        }, 0);
    }

    public void stopAnimation() {
        Log.d(TAG, Util.getMethodName());

        if (mRunnable != null)
            mHandler.removeCallbacks(mRunnable);
    }

    public void hazardButtonPressed(View view) {
        Log.d(TAG, Util.getMethodName());

        mHazardsAreFlashing = !mHazardsAreFlashing;

        if (mHazardsAreFlashing) {
            startAnimation();
        } else { // if (!mHazardsAreFlashing)
            stopAnimation();
            mHazardButton.setImageResource(R.drawable.hazard_off);
        }

        // TODO: Call backend code to update RVI node
    }

    public void toggleButtonPressed(View view) {
        Log.d(TAG, Util.getMethodName());

        ImageButton toggleButton = (ImageButton) view;
        toggleButton.setSelected(!toggleButton.isSelected());

        if (toggleButton.isSelected())
            toggleButton.setImageResource(mButtonImagesOn.get(toggleButton.getId()));
        else
            toggleButton.setImageResource(mButtonImagesOff.get(toggleButton.getId()));

        // TODO: Call backend code to update RVI node
    }

    public void seatTempButtonPressed(View view) {
        Log.d(TAG, Util.getMethodName());

        // TODO: Update the image
        // TODO: Call backend code to update RVI node
    }
}
