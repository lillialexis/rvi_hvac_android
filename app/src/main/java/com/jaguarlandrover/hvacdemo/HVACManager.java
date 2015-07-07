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

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

public class HVACManager implements RVIApp.RVIAppListener
{
    private final static String TAG = "HVACDemo:HVACManager";

    private final static String RVI_DOMAIN   = "jlr.com";
    private final static String RVI_APP_NAME = "/hvac";

    private static HVACManager ourInstance = new HVACManager();

    private final static ArrayList<String> serviceIdentifiers =
            new ArrayList<>(Arrays.asList(
                    "air_circ",
                    "airflow_direction",
                    "defrost_front",
                    "defrost_rear",
                    "fan_speed",
                    "seat_heat_left",
                    "seat_heat_right",
                    "temp_left",
                    "temp_right",
                    "subscribe",
                    "unsubscribe"
            ));

    private HVACManagerListener mListener;

    public interface HVACManagerListener {
        void onServiceUpdated(String service, Object value);
    }

    private HVACManager() {
        RVINode.setListener(new RVINode.RVINodeListener()
        {
            @Override
            public void rviNodeDidConnect() {
                updateService("subscribe", "{\"node\":\"jlr.com/android/987654321/\"}"); // TODO: Make dynamic, obvs
            }

            @Override
            public void rviNodeDidFailToConnect() {

            }

            @Override
            public void rviNodeDidDisconnect() {

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
            ourInstance.mRVIApp.setRemotePrefix(vin);
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

        ourInstance.mRVIApp = new RVIApp(RVI_APP_NAME, RVI_DOMAIN, "/vin/" + getVin(), serviceIdentifiers);
        ourInstance.mRVIApp.setListener(ourInstance);

        RVINode.connect();
        RVINode.addApp(ourInstance.mRVIApp);
    }

    public static void updateService(String service, String value) {
        ourInstance.mRVIApp.getService(service).setValue(value);
        ourInstance.mRVIApp.updateService(service);
    }

    @Override
    public void onServiceUpdated(RVIService service) {
        mListener.onServiceUpdated(service.getServiceIdentifier(), service.getValue());
    }

    public static HVACManagerListener getListener() {
        return ourInstance.mListener;
    }

    public static void setListener(HVACManagerListener listener) {
        ourInstance.mListener = listener;
    }
}
