package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    HVACState.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 8/20/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class HVACState
{
    private final static String TAG = "HVACDemo:HVACState";

    private Integer mAirDirection;
    private Integer mFanSpeed;
    private Boolean mAc;
    private Boolean mCirc;
    private Boolean mDefrostMax;

    public HVACState (Integer airDirection, Integer fanSpeed, Boolean ac, Boolean circ, Boolean defrostMax) {

        mAirDirection = airDirection;
        mFanSpeed = fanSpeed;
        mAc = ac;
        mCirc = circ;
        mDefrostMax = defrostMax;
    }

    public Integer getAirDirection() {
        return mAirDirection;
    }

    public Integer getFanSpeed() {
        return mFanSpeed;
    }

    public Boolean getAc() {
        return mAc;
    }

    public Boolean getCirc() {
        return mCirc;
    }

    public Boolean getDefrostMax() {
        return mDefrostMax;
    }
}
