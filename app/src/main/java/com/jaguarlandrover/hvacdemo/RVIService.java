package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIService.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import javax.xml.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.prefs.BackingStoreException;

public class RVIService
{
    private final static String TAG = "HVACDemo:RVIService";

    private String mServiceName;

    private String mAppName;
    private String mDomain;
    private String mVin;
    private String mBackend;

    private String mValue;

    public RVIService(String serviceName, String appName, String domain, String vin, String backend) {
        mServiceName = "/" + serviceName;
        mAppName     = appName;
        mDomain      = domain;
        mVin         = vin;
        mBackend     = backend;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }

    public String getServiceName() {
        return mServiceName;
    }

    public Object generateRequestParams() {
        HashMap<String, Object> params = new HashMap<>(4);
        HashMap<String, String> subParams = new HashMap<>(2);

        subParams.put("sending_node", mDomain + mBackend);
        subParams.put("value", mValue);

        params.put("service_name", mDomain + mVin + mAppName + mServiceName);
        params.put("parameters", Arrays.asList(subParams));
        params.put("timeout", (System.currentTimeMillis()/1000) + 5000);

        return params;
    }
}
