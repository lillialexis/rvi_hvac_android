package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIAuthJSONObject.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 6/15/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.util.Base64;

import java.math.MathContext;
import java.util.HashMap;

public class RVIAuthJSONObject extends RVIJSONObject
{
    private final static String TAG = "HVACDemo:RVIAuthJSONObject";

    private String mAddr;

    private Integer mPort;

    private String mVer;

    private String mCert;

    private String mSign;

    protected HashMap<String, Object> jsonHash() {
        HashMap<String, Object> jsonHash = super.jsonHash();

        jsonHash.put("addr", mAddr);
        jsonHash.put("port", mPort);
        jsonHash.put("ver",  mVer);
        jsonHash.put("cert", mCert);
        jsonHash.put("sign", mSign);

        return jsonHash;
    }

//    /**
//     * Serializes request object into json string
//     */
//    public String jsonString() {
//        HashMap<String, Object> requestHash = new HashMap<>(4);
//
//
//        Gson gson = new Gson();
//        return gson.toJson(requestHash);
//    }

    /**
     * Helper method to get an authorization json object
     */
    public RVIAuthJSONObject() {
        super();

        mCmd  = "au";

        mAddr = "0.0.0.0";
        mPort = 0;
        mVer  = "1.0";
        mCert = "";
        mSign = "";
    }

}
