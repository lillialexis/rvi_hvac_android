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

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

public class RVIApp
{
    private final static String TAG = "HVACDemo:RVIApp";

    private String mAppIdentifier;
    private String mDomain;
    private String mRemotePrefix;

    private String mLocalPrefix;

    private ArrayList<RVIService> mServices;

    public interface RVIAppListener
    {
        public void onServiceUpdated(RVIService service);
    }

    private RVIAppListener mListener;

    public RVIApp(Context context, String appIdentifier, String domain, String remotePrefix, ArrayList<String> services) {
        mAppIdentifier = appIdentifier;
        mDomain = domain;
        mRemotePrefix = remotePrefix;

        mLocalPrefix = RVINode.getLocalServicePrefix(context);
                //"/android/" + UUID.randomUUID().toString();//987654321"; // TODO: Generate randomly

        mServices = makeServices(services);
    }

    private ArrayList<RVIService> makeServices(ArrayList<String> serviceIdentifiers) {
        ArrayList<RVIService> services = new ArrayList<>(serviceIdentifiers.size());
        for (String serviceIdentifier : serviceIdentifiers)
            services.add(makeService(serviceIdentifier));

        return services;
    }

    private RVIService makeService(String serviceIdentifier) {
        return new RVIService(serviceIdentifier, mAppIdentifier, mDomain, mRemotePrefix, mLocalPrefix);
    }

    public RVIService getService(String serviceIdentifier) {
        for (RVIService service : mServices)
            if (service.getServiceIdentifier().equals(serviceIdentifier) || service.getServiceIdentifier()
                                                                                   .equals("/" + serviceIdentifier))
                return service;

        return null;
    }

    public void updateService(String service) {
        RVIDlinkReceivePacket serviceInvokeJSONObject = new RVIDlinkReceivePacket(getService(service));
        RVIRemoteConnectionManager.sendPacket(serviceInvokeJSONObject);
    }

    public void serviceUpdated(RVIService service) {
        RVIService ourService = getService(service.getServiceIdentifier());

        ourService.setValue(service.getValue());

        mListener.onServiceUpdated(ourService);
    }

    public String getDomain() {
        return mDomain;
    }

    public String getRemotePrefix() {
        return mRemotePrefix;
    }

    public void setRemotePrefix(String remotePrefix) {
        mRemotePrefix = remotePrefix;
        //mServices.removeAll(mServices);

        for (RVIService service : mServices)
            service.setRemotePrefix(remotePrefix);
    }


    public RVIAppListener getListener() {
        return mListener;
    }

    public void setListener(RVIAppListener listener) {
        mListener = listener;
    }

    public ArrayList<RVIService> getServices() {
        return mServices;
    }

    public String getAppIdentifier() {
        return mAppIdentifier;
    }

    public void setAppIdentifier(String appIdentifier) {
        mAppIdentifier = appIdentifier;
    }
}
