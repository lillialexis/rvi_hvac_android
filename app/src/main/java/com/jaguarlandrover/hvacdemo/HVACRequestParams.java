package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    HVACRequestParams.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.util.HashMap;

public class HVACRequestParams
{
    private final static String TAG = "HVACDemo:HVACRequestParams";

    /**
     * The name of the RVI HVAC service.
     */
    private String mServiceName;

    /**
     * The timeout
     */
    private long mTimeout;

    /**
     * The sub-parameters
     */
    private Object mParams;

    /**
     * Serializes parameters into an object that can be turned into a json string
     */
    public HashMap<String, Object> toSerializable() {
        HashMap<String, Object> paramHash = new HashMap<>(4);

        paramHash.put("service_name", mServiceName);
        paramHash.put("parameters", mParams);
        paramHash.put("timeout", mTimeout);

        return paramHash;
    }

    /**
     * Constructor that takes the service name and the parameters object
     *
     * @param serviceName The service mame that this request is for
     * @param params Some parameters to send along with the request
     */
    public HVACRequestParams(String serviceName, Object params) {
        mServiceName = serviceName;
        mParams      = params;

        mTimeout     = System.currentTimeMillis() + 5000;

    }
}
