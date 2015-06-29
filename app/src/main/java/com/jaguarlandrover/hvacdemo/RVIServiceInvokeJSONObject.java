package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIServiceInvokeJSONObject.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 6/15/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.util.Base64;

import java.util.HashMap;
import java.util.Random;

public class RVIServiceInvokeJSONObject extends RVIJSONObject
{
    private final static String TAG = "HVACDemo:RVIServiceInvokeJSONObject";

    /**
     * The mod parameter.
     * This client is only using 'proto_json_rpc' at the moment.
     */
    private String mMod;

    /**
     * The RVIService used to create the request params
     */
    private RVIService mService;

    protected HashMap<String, Object> jsonHash() {
        HashMap<String, Object> jsonHash = super.jsonHash();

        jsonHash.put("mod", mMod);
        jsonHash.put("data", Base64.encodeToString(mService.jsonString().getBytes(), Base64.DEFAULT));

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
     * Helper method to get a service invoke json object
     *
     * @param service Some parameters to send along with the request, either named, un-named, or nil
     */
    public RVIServiceInvokeJSONObject(RVIService service) {
        super();

        mCmd = "rcv";

        mMod = "proto_json_rpc";
        mService  = service;
    }


}
