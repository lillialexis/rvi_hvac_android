package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIDlinkPacket.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 6/15/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.util.Log;
import com.google.gson.Gson;

import java.util.HashMap;

public class RVIDlinkPacket
{
    private final static String TAG = "HVACDemo:RVIDlinkPacket";

    protected enum Command
    {
        AUTHORIZE         ("au"),
        SERVICE_ANNOUNCE  ("sa"),
        RECEIVE           ("rcv"),
        PING              ("ping");

        private final String mString;
        Command(String string) {
            mString = string;
        }

        String strVal() { return mString; }
    }

    /**
     * The TID.
     */
    protected Integer mTid = 0;

    /**
     * The cmd that was used in the request.
     */
    protected Command mCmd;

    protected String  mSig = null;

    private static Integer tidCounter = 0;

    protected HashMap<String, Object> jsonHash() {
        HashMap<String, Object> jsonHash = new HashMap<>(4);

        jsonHash.put("cmd",  mCmd.strVal());

        jsonHash.put("tid",  mTid);
        jsonHash.put("sign", mSig);

        return jsonHash;
    }

    /**
     * Serializes the object into json strVal
     */
    public String jsonString() {
        Gson gson = new Gson();
        return gson.toJson(jsonHash());
    }

    /**
     * Base constructor of the RVIDlinkPacket
     */
    protected RVIDlinkPacket(Command command) {
        mCmd = command;

        mTid = tidCounter++;
        mSig = "";
    }

    public RVIDlinkPacket(Command command, HashMap jsonHash) {
        mCmd = command;

        Log.d(TAG, jsonHash.keySet().toString() + jsonHash.values().toString());

        if (jsonHash.containsKey("tid"))
            mTid = ((Double) jsonHash.get("tid")).intValue();

        if (jsonHash.containsKey("sign"))
            mSig = (String) jsonHash.get("sign"); // TODO: Push for sign->sig

    }
}
