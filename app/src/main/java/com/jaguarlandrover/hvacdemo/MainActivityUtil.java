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

    public static HashMap<Integer, Object> initializeControlServices() {
        HashMap<Integer, Object> hm = new HashMap<>();

        hm.put(R.id.hazard_button,                HVACServiceIdentifier.HAZARD.value());

        hm.put(R.id.left_temp_picker,             HVACServiceIdentifier.TEMP_LEFT.value());
        hm.put(R.id.right_temp_picker,            HVACServiceIdentifier.TEMP_RIGHT.value());
        hm.put(R.id.left_seat_temp_button,        HVACServiceIdentifier.SEAT_HEAT_LEFT.value());
        hm.put(R.id.right_seat_temp_button,       HVACServiceIdentifier.SEAT_HEAT_RIGHT.value());

        hm.put(R.id.fan_power_seekbar,            HVACServiceIdentifier.FAN_SPEED.value());

        hm.put(R.id.fan_down_button,              HVACServiceIdentifier.AIRFLOW_DIRECTION.value());
        hm.put(R.id.fan_right_button,             HVACServiceIdentifier.AIRFLOW_DIRECTION.value());
        hm.put(R.id.fan_up_button,                HVACServiceIdentifier.AIRFLOW_DIRECTION.value());

        hm.put(R.id.defrost_rear_button,          HVACServiceIdentifier.DEFROST_REAR.value());
        hm.put(R.id.defrost_front_button,         HVACServiceIdentifier.DEFROST_FRONT.value());
        hm.put(R.id.max_fan_button, Arrays.asList(HVACServiceIdentifier.DEFROST_REAR.value(),
                                                  HVACServiceIdentifier.DEFROST_FRONT.value(),
                                                  HVACServiceIdentifier.FAN_SPEED.value(),
                                                  HVACServiceIdentifier.DEFROST_MAX.value()));

        hm.put(R.id.circ_button,                  HVACServiceIdentifier.AIR_CIRC.value());
        hm.put(R.id.ac_button,                    HVACServiceIdentifier.AC.value());
        hm.put(R.id.auto_button,    Arrays.asList(HVACServiceIdentifier.AIRFLOW_DIRECTION.value(),
                                                  HVACServiceIdentifier.DEFROST_REAR.value(),
                                                  HVACServiceIdentifier.DEFROST_FRONT.value(),
                                                  HVACServiceIdentifier.FAN_SPEED.value(),
                                                  HVACServiceIdentifier.TEMP_LEFT.value(),
                                                  HVACServiceIdentifier.TEMP_RIGHT.value(),
                                                  HVACServiceIdentifier.AUTO.value()));

        return hm;
    }

    public static HashMap<String, Object> initializeServiceControls() {
        HashMap<String, Object> hm = new HashMap<>();

        hm.put(HVACServiceIdentifier.HAZARD.value(),          R.id.hazard_button);

        hm.put(HVACServiceIdentifier.TEMP_LEFT.value(),       R.id.left_temp_picker);
        hm.put(HVACServiceIdentifier.TEMP_RIGHT.value(),      R.id.right_temp_picker);

        hm.put(HVACServiceIdentifier.SEAT_HEAT_LEFT.value(),  R.id.left_seat_temp_button);
        hm.put(HVACServiceIdentifier.SEAT_HEAT_RIGHT.value(), R.id.right_seat_temp_button);

        hm.put(HVACServiceIdentifier.FAN_SPEED.value(),       R.id.fan_power_seekbar);

        hm.put(HVACServiceIdentifier.DEFROST_REAR.value(),    R.id.defrost_rear_button);
        hm.put(HVACServiceIdentifier.DEFROST_FRONT.value(),   R.id.defrost_front_button);

        hm.put(HVACServiceIdentifier.AIR_CIRC.value(),        R.id.circ_button);
        hm.put(HVACServiceIdentifier.AC.value(),              R.id.ac_button);

        return hm;
    }

}
