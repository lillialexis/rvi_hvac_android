package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Dao of Development.
 *
 * All rights reserved.
 *
 * ${FILE_NAME}
 * rvi_hvac_android
 *
 * Created by Lilli Szafranski on 5/13/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.util.HashMap;

public class MainActivityUtil
{
    private final static String TAG = "HVACDemo:MainActivityUtil";

    public static HashMap<Integer, Integer> initializeButtonOffHashMap() {
        HashMap<Integer, Integer> hm = new HashMap<>();

        hm.put(R.id.fan_down_button,      R.drawable.fan_dir_down_off);
        hm.put(R.id.fan_right_button,     R.drawable.fan_dir_right_off);
        hm.put(R.id.fan_up_button,        R.drawable.fan_dir_up_off);
        hm.put(R.id.ac_button,            R.drawable.fan_control_ac_off);
        hm.put(R.id.auto_button,          R.drawable.fan_control_auto_off);
        hm.put(R.id.circ_button,          R.drawable.fan_control_circ_off);
        hm.put(R.id.max_fan_button,       R.drawable.defrost_max_off);
        hm.put(R.id.defrost_rear_button,  R.drawable.defrost_rear_off);
        hm.put(R.id.defrost_front_button, R.drawable.defrost_front_off);

        return hm;
    }

    public static HashMap<Integer, Integer> initializeButtonOnHashMap() {
        HashMap<Integer, Integer> hm = new HashMap<>();

        hm.put(R.id.fan_down_button,      R.drawable.fan_dir_down_on);
        hm.put(R.id.fan_right_button,     R.drawable.fan_dir_right_on);
        hm.put(R.id.fan_up_button,        R.drawable.fan_dir_up_on);
        hm.put(R.id.ac_button,            R.drawable.fan_control_ac_on);
        hm.put(R.id.auto_button,          R.drawable.fan_control_auto_on);
        hm.put(R.id.circ_button,          R.drawable.fan_control_circ_on);
        hm.put(R.id.max_fan_button,       R.drawable.defrost_max_on);
        hm.put(R.id.defrost_rear_button,  R.drawable.defrost_rear_on);
        hm.put(R.id.defrost_front_button, R.drawable.defrost_front_on);

        return hm;
    }

}
