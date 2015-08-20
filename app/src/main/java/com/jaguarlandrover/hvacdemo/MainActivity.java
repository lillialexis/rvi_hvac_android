package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    MainActivity.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import android.os.Handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity implements HVACManager.HVACManagerListener
{
    private final static String TAG = "HVACDemo:MainActivity";

    private final Handler mHandler = new Handler();
    private Runnable mRunnable;

    private HashMap<Integer, String> mViewIdsToServiceIds;
    private HashMap<String, Integer> mServiceIdsToViewIds;

    private HashMap<Integer, Integer> mButtonOffImages;
    private HashMap<Integer, Integer> mButtonOnImages;
    private HashMap<Integer, List>    mSeatTempImages;

    private List<Integer> mSeatTempValues = Arrays.asList(0, 5, 3, 1);

    private int mLeftSeatTempState  = 0;
    private int mRightSeatTempState = 0;

    private boolean mMaxDefrostIsOn;
    private boolean mAutoIsOn;

    private HVACState mSavedState;

    private boolean mHazardsAreFlashing = false;
    private boolean mHazardsImageIsOn;

    private ImageButton mHazardButton;
    private SeekBar     mFanSpeedSeekBar;

    private SeekBar.OnSeekBarChangeListener mFanSpeedSeekBarListener;

    private final static Integer DEFAULT_FAN_SPEED = 3;
    private final static Integer MAX_FAN_SPEED     = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewIdsToServiceIds = MainActivityUtil.initializeViewToServiceIdMap();
        mServiceIdsToViewIds = MainActivityUtil.initializeServiceToViewIdMap();

        mButtonOffImages = MainActivityUtil.initializeButtonOffImagesMap();
        mButtonOnImages = MainActivityUtil.initializeButtonOnImagesMap();

        mSeatTempImages = MainActivityUtil.initializeSeatTempImagesMap();

        mHazardButton = (ImageButton) findViewById(R.id.hazard_button);

        configurePicker((NumberPicker) findViewById(R.id.left_temp_picker));
        configurePicker((NumberPicker) findViewById(R.id.right_temp_picker));

        configureSeekBar((SeekBar) findViewById(R.id.fan_speed_seekbar));

        mFanSpeedSeekBar = (SeekBar) findViewById(R.id.fan_speed_seekbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        HVACManager.setListener(this);
        if (!HVACManager.isRviConfigured())
            startActivity(new Intent(this, SettingsActivity.class));
        else
            HVACManager.start();
    }

    private void configureSeekBar(SeekBar seekBar) {
        seekBar.setMax(MAX_FAN_SPEED);
        seekBar.setOnSeekBarChangeListener(mFanSpeedSeekBarListener = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, Util.getMethodName());

                if (fromUser) {
                    HVACManager.invokeService(getServiceIdentifiersFromViewId(seekBar.getId()), Integer.toString(progress));

                    if (progress == 0) {
                        setAirflowDirectionButtons(0);
                        HVACManager.invokeService(HVACServiceIdentifier.AIRFLOW_DIRECTION.value(), Integer.toString(0));
                    }
                }
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

        picker.setMinValue(15);
        picker.setMaxValue(29);

        picker.setWrapSelectorWheel(false);
        picker.setDisplayedValues(new String[]{"LO", "16˚", "17˚", "18˚", "19˚", "20˚", "21˚", "22˚", "23˚", "24˚", "25˚", "26˚", "27˚", "28˚", "HI"});

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, Util.getMethodName());

                HVACManager.invokeService(getServiceIdentifiersFromViewId(picker.getId()), Integer.toString(newVal));
            }
        });
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

    private void toggleHazardButtonFlashing(boolean shouldBeFlashing) {
        mHazardsAreFlashing = shouldBeFlashing;

        if (mHazardsAreFlashing) {
            startAnimation();
        } else { /* if (!mHazardsAreFlashing) */
            stopAnimation();
            mHazardButton.setImageResource(R.drawable.hazard_off);
        }
    }

    public void hazardButtonPressed(View view) {
        Log.d(TAG, Util.getMethodName());

        toggleHazardButtonFlashing(!mHazardsAreFlashing);

        HVACManager.invokeService(getServiceIdentifiersFromViewId(view.getId()), Boolean.toString(mHazardsAreFlashing));
    }

    public void updateToggleButtonImage(ImageButton toggleButton) {
        if (toggleButton.isSelected())
            toggleButton.setImageResource(mButtonOnImages.get(toggleButton.getId()));
        else
            toggleButton.setImageResource(mButtonOffImages.get(toggleButton.getId()));
    }

    private void toggleTheButton(ImageButton toggleButton) {
        toggleButton.setSelected(!toggleButton.isSelected());
        updateToggleButtonImage(toggleButton);
    }

    private Integer getAirflowDirectionValue() {
        return ((findViewById(R.id.fan_down_button)).isSelected()  ? 1 : 0) +
               ((findViewById(R.id.fan_right_button)).isSelected() ? 2 : 0) +
               ((findViewById(R.id.fan_up_button)).isSelected()    ? 4 : 0);
    }

    private void setAirflowDirectionButtons (Integer value) {

        findViewById(R.id.fan_down_button) .setSelected(value % 2 == 1); value /= 2;
        findViewById(R.id.fan_right_button).setSelected(value % 2 == 1); value /= 2;
        findViewById(R.id.fan_up_button)   .setSelected(value % 2 == 1);

        updateToggleButtonImage((ImageButton) findViewById(R.id.fan_down_button));
        updateToggleButtonImage((ImageButton) findViewById(R.id.fan_right_button));
        updateToggleButtonImage((ImageButton) findViewById(R.id.fan_up_button));
    }

    public void seatTempButtonPressed(View view) {
        Log.d(TAG, Util.getMethodName());

        ImageButton seatTempButton = (ImageButton) view;

        int newSeatTempState;
        if (seatTempButton == findViewById(R.id.left_seat_temp_button))
            newSeatTempState = ++mLeftSeatTempState % 4;
        else
            newSeatTempState = ++mRightSeatTempState % 4;

        seatTempButton.setImageResource((Integer) mSeatTempImages.get(seatTempButton.getId()).get(newSeatTempState));

        HVACManager.invokeService(getServiceIdentifiersFromViewId(seatTempButton.getId()),
                Integer.toString(mSeatTempValues.get(newSeatTempState)));
    }

    public void setSeatTempImageFromValue(ImageButton seatTempButton, Integer value) {
        int index = mSeatTempValues.indexOf(value);
        seatTempButton.setImageResource((Integer) mSeatTempImages.get(seatTempButton.getId()).get(index));
    }

    private boolean shouldBreakOutOfAuto(HVACServiceIdentifier serviceIdentifier) {
        switch (serviceIdentifier) {

            case FAN_SPEED:
            case AIRFLOW_DIRECTION:
            case DEFROST_MAX:
            case AIR_CIRC:
            case AC:
            case AUTO:
             return true;
        }
        return false;
    }

    private void breakOutOfAuto(HVACServiceIdentifier serviceIdentifier) {
        mAutoIsOn = false;

        if (serviceIdentifier != HVACServiceIdentifier.FAN_SPEED && mSavedState.getFanSpeed() != 0) {
            mFanSpeedSeekBar.setProgress(mSavedState.getFanSpeed());
            HVACManager.invokeService(HVACServiceIdentifier.FAN_SPEED.value(), Integer.toString(mSavedState.getFanSpeed()));
        }

        if (serviceIdentifier != HVACServiceIdentifier.AIRFLOW_DIRECTION) {
            setAirflowDirectionButtons(mSavedState.getAirDirection());
            HVACManager.invokeService(HVACServiceIdentifier.AIRFLOW_DIRECTION.value(), Integer.toString(mSavedState.getAirDirection()));
        }

        if (serviceIdentifier != HVACServiceIdentifier.AC) {
            if (findViewById(R.id.ac_button).isSelected() != mSavedState.getAc())
                toggleTheButton((ImageButton) findViewById(R.id.ac_button));
            HVACManager.invokeService(HVACServiceIdentifier.AC.value(), Boolean.toString(mSavedState.getAc()));
        }

        if (serviceIdentifier != HVACServiceIdentifier.AIR_CIRC) {
            if (findViewById(R.id.circ_button).isSelected() != mSavedState.getCirc())
                toggleTheButton((ImageButton) findViewById(R.id.circ_button));
            HVACManager.invokeService(HVACServiceIdentifier.AIR_CIRC.value(), Boolean.toString(mSavedState.getCirc()));
        }

        if (serviceIdentifier != HVACServiceIdentifier.AUTO) {
//            if (findViewById(R.id.auto_button).isSelected() != mSavedState.getCirc())
                toggleTheButton((ImageButton) findViewById(R.id.auto_button));
            HVACManager.invokeService(HVACServiceIdentifier.AUTO.value(), Boolean.toString(false));
        }
    }

    public void toggleButtonPressed(View view) {
        Log.d(TAG, Util.getMethodName());

        ImageButton toggleButton = (ImageButton) view;
        String serviceIdentifier = getServiceIdentifiersFromViewId(toggleButton.getId());

        toggleTheButton(toggleButton);

        if (mAutoIsOn && shouldBreakOutOfAuto(HVACServiceIdentifier.get(serviceIdentifier)))
            breakOutOfAuto(HVACServiceIdentifier.get(serviceIdentifier));

        switch (toggleButton.getId()) {
            case R.id.fan_down_button:
            case R.id.fan_up_button:
            case R.id.fan_right_button:

                /* If the fan speed is off, turn it on */
                if (mFanSpeedSeekBar.getProgress() == 0) {
                    mFanSpeedSeekBar.setProgress(DEFAULT_FAN_SPEED);
                    HVACManager.invokeService(HVACServiceIdentifier.FAN_SPEED.value(), Integer.toString(DEFAULT_FAN_SPEED));
                }

                HVACManager.invokeService(getServiceIdentifiersFromViewId(toggleButton.getId()),
                        Integer.toString(getAirflowDirectionValue()));

                break;

            case R.id.auto_button:

                if (toggleButton.isSelected()) {
                    mSavedState = new HVACState(getAirflowDirectionValue(), mFanSpeedSeekBar.getProgress(),
                            findViewById(R.id.ac_button).isSelected(), findViewById(R.id.circ_button).isSelected());

                    setAirflowDirectionButtons(0);
                    HVACManager.invokeService(HVACServiceIdentifier.AIRFLOW_DIRECTION.value(), Integer.toString(0));

                    mFanSpeedSeekBar.setProgress(0);
                    HVACManager.invokeService(HVACServiceIdentifier.FAN_SPEED.value(), Integer.toString(0));

                    if (!findViewById(R.id.ac_button).isSelected())
                        toggleButtonPressed(findViewById(R.id.ac_button));

                    if (findViewById(R.id.circ_button).isSelected())
                        toggleButtonPressed(findViewById(R.id.circ_button));

                    mAutoIsOn = true;
                }

            case R.id.defrost_max_button:
            case R.id.ac_button:
            case R.id.defrost_rear_button:
            case R.id.defrost_front_button:
            case R.id.circ_button:

                HVACManager.invokeService(getServiceIdentifiersFromViewId(toggleButton.getId()),
                        Boolean.toString(toggleButton.isSelected()));

                break;
        }
    }

    @Override
    public void onServiceInvoked(String serviceIdentifierString, Object parameters) {

        Integer id;
        View view = null;

        HVACServiceIdentifier serviceIdentifier = HVACServiceIdentifier.get(serviceIdentifierString);
        if ((id = getViewIdFromServiceIdentifier(serviceIdentifierString)) != null)
            view = findViewById(id);

        switch (serviceIdentifier) {
            case DEFROST_MAX:
            case AUTO:

                Boolean newToggleButtonState = Boolean.parseBoolean((String) parameters);

                /* Special extra work for auto/max_defrost */
                if (newToggleButtonState)
                    mSavedState = new HVACState(getAirflowDirectionValue(), mFanSpeedSeekBar.getProgress(), findViewById(R.id.ac_button).isSelected(), findViewById(R.id.circ_button).isSelected());

                if (serviceIdentifier == HVACServiceIdentifier.AUTO)
                    mAutoIsOn = newToggleButtonState;

                if (serviceIdentifier == HVACServiceIdentifier.DEFROST_MAX)
                    mMaxDefrostIsOn = newToggleButtonState;

                /* Pass through... */

            case AC:
            case AIR_CIRC:
            case DEFROST_FRONT:
            case DEFROST_REAR:
                if (view != null && view.isSelected() != Boolean.parseBoolean((String) parameters))
                    toggleTheButton((ImageButton) view);

                break;

            case AIRFLOW_DIRECTION:
                setAirflowDirectionButtons(Integer.parseInt((String) parameters));

                break;

            case FAN_SPEED:
                if (view != null) ((SeekBar) view).setProgress(Integer.parseInt((String) parameters));

                break;

            case SEAT_HEAT_LEFT:
            case SEAT_HEAT_RIGHT:
                setSeatTempImageFromValue((ImageButton) view, Integer.parseInt((String) parameters));

                break;

            case TEMP_LEFT:
            case TEMP_RIGHT:
                if (view != null) ((NumberPicker) view).setValue(Integer.parseInt((String) parameters));

                break;

            case HAZARD:
                toggleHazardButtonFlashing(Boolean.parseBoolean((String) parameters));

                break;

            case SUBSCRIBE:
            case UNSUBSCRIBE:
            case NONE:
                break;
        }
    }


    String getServiceIdentifiersFromViewId(Integer uiControlId) {
        return mViewIdsToServiceIds.get(uiControlId);
    }

    Integer getViewIdFromServiceIdentifier(String serviceIdentifier) {
        return mServiceIdsToViewIds.get(serviceIdentifier);
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

}
