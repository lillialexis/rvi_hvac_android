package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    MainActivityUtil.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

    public static HashMap<Integer, List> initializeSeatTempHashArray(){
        HashMap<Integer, List> hm = new HashMap<>();

        hm.put(R.id.left_seat_temp_button,  Arrays.asList(R.drawable.left_heat_seat_off,
                                                          R.drawable.left_heat_seat_5,
                                                          R.drawable.left_heat_seat_3,
                                                          R.drawable.left_heat_seat_1));
        hm.put(R.id.right_seat_temp_button, Arrays.asList(R.drawable.right_heat_seat_off,
                                                          R.drawable.right_heat_seat_5,
                                                          R.drawable.right_heat_seat_3,
                                                          R.drawable.right_heat_seat_1));

        return hm;
    }

    public static HashMap<Integer, Object> initializeButtonServices() {
        HashMap<Integer, Object> hm = new HashMap<>();

        hm.put(R.id.fan_down_button,        "airflow_direction");
        hm.put(R.id.fan_right_button,       "airflow_direction");
        hm.put(R.id.fan_up_button,          "airflow_direction");
        hm.put(R.id.ac_button,              null);
        hm.put(R.id.auto_button,            Arrays.asList("airflow_direction",
                                                          "defrost_rear",
                                                          "defrost_front",
                                                          "fan_speed",
                                                          "temp_left",
                                                          "temp_right"));
        hm.put(R.id.circ_button,            "air_circ");
        hm.put(R.id.max_fan_button,         Arrays.asList("defrost_rear",
                                                          "defrost_front",
                                                          "fan_speed"));
        hm.put(R.id.defrost_rear_button,    "defrost_rear");
        hm.put(R.id.defrost_front_button,   "defrost_front");
        hm.put(R.id.left_seat_temp_button,  "seat_heat_left");
        hm.put(R.id.right_seat_temp_button, "seat_heat_right");

        return hm;
    }
}
