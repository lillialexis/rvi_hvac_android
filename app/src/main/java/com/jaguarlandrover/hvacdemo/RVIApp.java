package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIApp.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.util.ArrayList;

public class RVIApp
{
    private final static String TAG = "HVACDemo:RVIApp";

    private String mAppName;
    private String mDomain;
    private String mVin;

    private String mBackend;

    private ArrayList<RVIService> mServices;

    public RVIApp(String appName, String domain, String vin) {
        mAppName  = appName;
        mDomain   = domain;
        mVin      = "/vin/" + vin;

        mBackend  = "/android/987654321"; // TODO: Generate randomly

        mServices = new ArrayList<>();
    }

    public RVIService getService(String name) {
        for (RVIService service : mServices)
            if (service.getServiceName().equals(name) || service.getServiceName().equals("/" + name))
                return service;

        RVIService service = new RVIService(name, mAppName, mDomain, mVin, mBackend);
        mServices.add(service);

        return service;
    }

    public String getDomain() {
        return mDomain;
    }

    public String getVin() {
        return mVin;
    }

    public void setVin(String vin) {
        mVin = vin;
        mServices.removeAll(mServices);
    }

    public ArrayList<RVIService> getServices() {
        return mServices;
    }

    public void updateService(String service) {
        //RPCRequest request = new RPCRequest("message", getService(service));
        RVIServiceInvokeJSONObject serviceInvokeJSONObject = new RVIServiceInvokeJSONObject(getService(service));
        RVIRemoteConnectionManager.sendRviRequest(serviceInvokeJSONObject);
    }
}
