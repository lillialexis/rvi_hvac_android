package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIJSONObject.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 6/15/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import com.google.gson.Gson;

import java.util.HashMap;

public class RVIJSONObject
{
    private final static String TAG = "HVACDemo:RVIJSONObject";


    /**
     * The TID.
     */
    protected Integer mTid;

    /**
     * The cmd that was used in the request.
     */
    protected String mCmd;

    protected HashMap<String, Object> jsonHash () {
        HashMap<String, Object> jsonHash = new HashMap<>(4);

        jsonHash.put("tid", mTid);
        jsonHash.put("cmd", mCmd);

        return jsonHash;
    }

    /**
     * Serializes the object into json string
     */
    public String jsonString() {
        Gson gson = new Gson();
        return gson.toJson(jsonHash());
    }

    /**
     * Base constructor of the RVIJSONObject
     */
    public RVIJSONObject() {
        mTid = 1;
    }


}
