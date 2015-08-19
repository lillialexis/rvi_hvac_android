package com.jaguarlandrover.hvacdemo;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    HVACServiceIdentifier.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 7/7/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public enum HVACServiceIdentifier
{
    HAZARD("hazard"),
    TEMP_LEFT("temp_left"),
    TEMP_RIGHT("temp_right"),
    SEAT_HEAT_LEFT("seat_heat_left"),
    SEAT_HEAT_RIGHT("seat_heat_right"),
    FAN_SPEED("fan_speed"),
    AIRFLOW_DIRECTION("airflow_direction"),
    DEFROST_REAR("defrost_rear"),
    DEFROST_FRONT("defrost_front"),
    DEFROST_MAX("defrost_max"),
    AIR_CIRC("air_circ"),
    AC("fan"),
    AUTO("control_auto"),
    SUBSCRIBE("subscribe"),
    UNSUBSCRIBE("unsubscribe"),
    NONE("none");

    private final String mIdentifier;

    HVACServiceIdentifier(String identifier) {
        mIdentifier = identifier;
    }

    public final String value() {
        return mIdentifier;
    }

    public static HVACServiceIdentifier get(String identifier) {
        switch (identifier) {
            case "hazard":            return HAZARD;
            case "temp_left":         return TEMP_LEFT;
            case "temp_right":        return TEMP_RIGHT;
            case "seat_heat_left":    return SEAT_HEAT_LEFT;
            case "seat_heat_right":   return SEAT_HEAT_RIGHT;
            case "fan_speed":         return FAN_SPEED;
            case "airflow_direction": return AIRFLOW_DIRECTION;
            case "defrost_rear":      return DEFROST_REAR;
            case "defrost_front":     return DEFROST_FRONT;
            case "defrost_max":       return DEFROST_MAX;
            case "air_circ":          return AIR_CIRC;
            case "fan":               return AC;
            case "control_auto":      return AUTO;
            case "subscribe":         return SUBSCRIBE;
            case "unsubscribe":       return UNSUBSCRIBE;
        }

        return NONE;
    }

}
