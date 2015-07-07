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
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class RVIDlinkPacket
{
    private final static String TAG = "HVACDemo:RVIDlinkPacket";

    protected enum Command
    {
        @SerializedName("au")   AUTHORIZE("au"),
        @SerializedName("sa")   SERVICE_ANNOUNCE("sa"),
        @SerializedName("rcv")  RECEIVE("rcv"),
        @SerializedName("ping") PING("ping");

        private final String mString;

        Command(String string) {
            mString = string;
        }

        String strVal() {
            return mString;
        }
    }

    /**
     * The TID.
     */
    @SerializedName("tid")
    protected Integer mTid = null;

    /**
     * The cmd that was used in the request.
     */
    @SerializedName("cmd")
    protected Command mCmd = null;

    @SerializedName("sign")
    protected String mSig = null;

    private static Integer tidCounter = 0;

//    protected HashMap<String, Object> jsonHash() {
//        HashMap<String, Object> jsonHash = new HashMap<>(4);
//
//        jsonHash.put("cmd", mCmd.strVal());
//
//        jsonHash.put("tid", mTid);
//        jsonHash.put("sign", mSig);
//
//        return jsonHash;
//    }

    /**
     * Serializes the object into json strVal
     */
    // TODO: Are there json serialization interfaces that this class can implement that
    protected String jsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);//jsonHash());
    }

    /**
     * Base constructor of the RVIDlinkPacket
     */
    protected RVIDlinkPacket(Command command) {
        if (command == null) {
          throw new IllegalArgumentException("Command can't be null");
        }

        mCmd = command;

        mTid = tidCounter++;
        mSig = "";
    }

    protected RVIDlinkPacket(Command command, HashMap jsonHash) {
        if (command == null || jsonHash == null)  {
          throw new IllegalArgumentException("Constructor arguments can't be null");
        }

        mCmd = command;

        // TODO: What other args should be required?
        if (jsonHash.containsKey("tid"))
            mTid = ((Double) jsonHash.get("tid")).intValue();

        if (jsonHash.containsKey("sign"))
            mSig = (String) jsonHash.get("sign"); // TODO: Push for sign->sig

    }
}
