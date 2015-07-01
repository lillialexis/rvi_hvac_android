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

    private HVACManager() {
        RVIRemoteConnectionManager.setListener(new RVIListener()
        {
            @Override
            public void onRVIDidConnect() {
                updateService("subscribe", "{\"node\":\"jlr.com/android/987654321/\"}"); // TODO: Make dynamic, obvs
            }

            @Override
            public void onRVIDidFailToConnect(Error error) {

            }

            @Override
            public void onRVIDidDisconnect() {

            }

            @Override
            public void onRVIDidReceiveData(String data) {

            }

            @Override
            public void onRVIDidSendData() {

            }

            @Override
            public void onRVIDidFailToSendData(Error error) {

            }
        });
    }

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

    public static String getServerUrl() {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        return sharedPref.getString(HVACApplication.getContext().getResources().getString(R.string.server_url_prefs_string), "");
    }

    public static void setServerUrl(String serverUrl) {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HVACApplication.getContext().getString(R.string.server_url_prefs_string), serverUrl);
        editor.apply();

        RVIRemoteConnectionManager.setServerUrl(serverUrl);
    }

    public static Integer getServerPort() {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        return sharedPref.getInt(HVACApplication.getContext().getResources().getString(R.string.server_port_prefs_string), 0);
    }

    public static void setServerPort(Integer serverPort) {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(HVACApplication.getContext().getString(R.string.server_port_prefs_string), serverPort);
        editor.apply();

        RVIRemoteConnectionManager.setServerPort(serverPort);
    }

    public static String getProxyServerUrl() {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        return sharedPref.getString(HVACApplication.getContext().getResources().getString(R.string.proxy_server_url_prefs_string), "");
    }

    public static void setProxyServerUrl(String proxyUrl) {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(HVACApplication.getContext().getString(R.string.proxy_server_url_prefs_string), proxyUrl);
        editor.apply();

        RVIRemoteConnectionManager.setProxyServerUrl(proxyUrl);
    }

    public static Integer getProxyServerPort() {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        return sharedPref.getInt(HVACApplication.getContext().getResources().getString(R.string.proxy_server_port_prefs_string), 0);
    }

    public static void setProxyServerPort(Integer proxyPort) {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(HVACApplication.getContext().getString(R.string.proxy_server_port_prefs_string), proxyPort);
        editor.apply();

        RVIRemoteConnectionManager.setProxyServerPort(proxyPort);
    }

    public static boolean getUsingProxyServer() {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        return sharedPref.getBoolean(HVACApplication.getContext().getResources()
                                                    .getString(R.string.using_proxy_server_prefs_string), false);
    }

    public static void setUsingProxyServer(boolean usingProxyServer) {
        SharedPreferences sharedPref = HVACApplication.getContext().getSharedPreferences("hvacConfig", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(HVACApplication.getContext()
                                         .getString(R.string.using_proxy_server_prefs_string), usingProxyServer);
        editor.apply();

        RVIRemoteConnectionManager.setUsingProxyServer(usingProxyServer);
    }

    public static boolean isRviConfigured() {
        if (getVin()        == null || getVin().isEmpty())       return false;
        if (getServerUrl()  == null || getServerUrl().isEmpty()) return false;
        if (getServerPort() == 0)                                return false;

        if (getUsingProxyServer()) {
            if (getProxyServerUrl()  == null || getProxyServerUrl().isEmpty()) return false;
            if (getProxyServerPort() == 0)                                     return false;
        }

        return true;
    }

    public static void start() {
        RVIRemoteConnectionManager.setServerUrl(getServerUrl());
        RVIRemoteConnectionManager.setServerPort(getServerPort());

        RVIRemoteConnectionManager.setProxyServerUrl(getProxyServerUrl());
        RVIRemoteConnectionManager.setProxyServerPort(getProxyServerPort());

        RVIRemoteConnectionManager.setUsingProxyServer(getUsingProxyServer());

        ourInstance.mRVIApp = new RVIApp(RVI_APP_NAME, RVI_DOMAIN, getVin());

        RVIRemoteConnectionManager.startListening();
    }

    public static void updateService(String service, String value) {
        ourInstance.mRVIApp.getService(service).setValue(value);
        ourInstance.mRVIApp.updateService(service);
    }
}
