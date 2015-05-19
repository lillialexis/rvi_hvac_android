package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RPCRequest.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

 import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Random;

public class RPCRequest
{
    private final static String TAG = "HVACDemo:RPCRequest";


    /**
     * The RPC Version.
     * This client only supports version 2.0 at the moment.
     */
    private String mVersion;

    /**
     * The id that was used in the request. If id is null the request is treated like a notification.
     */
    private String mId;

    /**
     * Method to call on the RPC Server.
     */
    private String mMethod;

    /**
     * Request params. Either named, un-named, or nil
     */
    private Object mParams;

    /**
     * Callback to call whenever request is finished
     */
    private RPCRequestListener mListener;

    /**
     * Serializes request object into json string
     */
    public String jsonString() {
        HashMap<String, Object> requestHash = new HashMap<>(4);

        requestHash.put("jsonrpc", mVersion);
        requestHash.put("params", mParams);
        requestHash.put("id", mId);
        requestHash.put("method", mMethod);

        Gson gson = new Gson();
        return gson.toJson(requestHash);
    }

    /**
     * Helper method to get a request object
     *
     * @param  method The method that this request is for
     */
    public RPCRequest(String method) {
        this(method, null, null);
    }

    /**
     * Helper method to get a request object
     *
     * @param method The method that this request is for
     * @param params Some parameters to send along with the request, either named, un-named, or nil
     */
    public RPCRequest(String method, Object params) {
        this(method, params, null);
    }

    /**
     * Helper method to get a request object
     *
     * @param method The method that this request is for
     * @param params Some parameters to send along with the request, either named, un-named, or nil
     * @param listener The listener to call once the request is finished
     */
    public RPCRequest(String method, Object params, RPCRequestListener listener) {
        mVersion  = "2.0";
        mMethod   = method;
        mParams   = params;
        mListener = listener;

        mId = (new Random()).toString();

    }

}
