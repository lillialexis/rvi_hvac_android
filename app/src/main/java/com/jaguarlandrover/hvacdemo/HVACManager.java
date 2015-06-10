package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    HVACManager.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class HVACManager
{
    private final static String TAG = "HVACDemo:HVACManager";

    private final static String RVI_DOMAIN   = "jlr.com";
    private final static String RVI_APP_NAME = "/hvac";

    private static HVACManager ourInstance = new HVACManager();

    private HVACManager() {}

    private RVIApp mRVIApp;

    public static String getVin() {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        return sharedPref.getString(HVACApplication.getContext().getResources().getString(R.string.vehicle_vin_prefs_string), "");
    }

    public static void setVin(String vin) {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HVACApplication.getContext().getString(R.string.vehicle_vin_prefs_string), vin);
        editor.apply();

        if (ourInstance.mRVIApp != null)
            ourInstance.mRVIApp.setVin(vin);
    }

    public static String getProxyUrl() {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        return sharedPref.getString(HVACApplication.getContext().getResources().getString(R.string.proxy_server_url_prefs_string), "");
    }

    public static void setProxyUrl(String proxyUrl) {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HVACApplication.getContext().getString(R.string.proxy_server_url_prefs_string), proxyUrl);
        editor.apply();

        RVIRemoteConnectionManager.setProxyServerUrl(proxyUrl);
    }

    public static boolean isRviConfigured() {
        if (getVin()      == null || getVin().isEmpty())      return false;
        if (getProxyUrl() == null || getProxyUrl().isEmpty()) return false;

        return true;
    }

    public static void start() {
        RVIRemoteConnectionManager.setProxyServerUrl(getProxyUrl());

        ourInstance.mRVIApp = new RVIApp(RVI_APP_NAME, RVI_DOMAIN, getVin());

        RVIRemoteConnectionManager.startListening();
    }

    public static void updateService(String service, String value) {
        ourInstance.mRVIApp.getService(service).setValue(value);
        ourInstance.mRVIApp.updateService(service);
    }

}
