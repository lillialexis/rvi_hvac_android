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

import android.content.Context;
import android.content.SharedPreferences;
import com.jaguarlandrover.rvi.*;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

public class HVACManager implements VehicleApplication.RVIAppListener
{
    private final static String TAG = "HVACDemo:HVACManager";

    private final static String RVI_DOMAIN   = "jlr.com";
    private final static String RVI_APP_NAME = "/hvac";

    private static Context applicationContext = HVACApplication.getContext();
    private static VehicleApplication rviApp;

    private static HVACManager ourInstance = new HVACManager();

    private final static ArrayList<String> serviceIdentifiers =
            new ArrayList<>(Arrays.asList(
                    HVACServiceIdentifier.HAZARD.value(),
                    HVACServiceIdentifier.TEMP_LEFT.value(),
                    HVACServiceIdentifier.TEMP_RIGHT.value(),
                    HVACServiceIdentifier.SEAT_HEAT_LEFT.value(),
                    HVACServiceIdentifier.SEAT_HEAT_RIGHT.value(),
                    HVACServiceIdentifier.FAN_SPEED.value(),
                    HVACServiceIdentifier.AIRFLOW_DIRECTION.value(),
                    HVACServiceIdentifier.DEFROST_REAR.value(),
                    HVACServiceIdentifier.DEFROST_FRONT.value(),
                    HVACServiceIdentifier.DEFROST_MAX.value(),
                    HVACServiceIdentifier.AIR_CIRC.value(),
                    HVACServiceIdentifier.AC.value(),
                    HVACServiceIdentifier.AUTO.value(),
                    HVACServiceIdentifier.SUBSCRIBE.value(),
                    HVACServiceIdentifier.UNSUBSCRIBE.value()
            ));

    private HVACManagerListener mListener;

    public interface HVACManagerListener
    {
        void onServiceUpdated(String service, Object value);
    }

    private HVACManager() {
        RemoteVehicleNode.setListener(new RemoteVehicleNode.RVINodeListener()
        {
            @Override
            public void rviNodeDidConnect() {
                updateService("/subscribe", "{\"node\":\"" + RVI_DOMAIN + RemoteVehicleNode
                        .getLocalServicePrefix(applicationContext) + "/\"}");
            }

            @Override
            public void rviNodeDidFailToConnect() {

            }

            @Override
            public void rviNodeDidDisconnect() {

            }
        });
    }

    private static SharedPreferences getPrefs() {
        return applicationContext
                .getSharedPreferences(applicationContext.getString(R.string.hvac_shared_prefs_string), MODE_PRIVATE);
    }

    private static String getStringFromPrefs(String key, String defaultValue) {
        return getPrefs().getString(key, defaultValue);
    }

    private static Integer getIntFromPrefs(String key, Integer defaultValue) {
        return getPrefs().getInt(key, defaultValue);
    }

    private static Boolean getBoolFromPrefs(String key, Boolean defaultValue) {
        return getPrefs().getBoolean(key, defaultValue);
    }

    private static void putStringInPrefs(String key, String value) {
        SharedPreferences sharedPref = getPrefs();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static void putIntInPrefs(String key, Integer value) {
        SharedPreferences sharedPref = getPrefs();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static void putBoolInPrefs(String key, Boolean value) {
        SharedPreferences sharedPref = getPrefs();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String getVin() {
        return getStringFromPrefs(applicationContext.getResources().getString(R.string.vehicle_vin_prefs_string), "");
    }

    public static void setVin(String vin) {
        putStringInPrefs(applicationContext.getString(R.string.vehicle_vin_prefs_string), vin);

        if (rviApp != null)
            rviApp.setRemotePrefix(vin);
    }

    public static String getServerUrl() {
        return getStringFromPrefs(applicationContext.getResources().getString(R.string.server_url_prefs_string), "");
    }

    public static void setServerUrl(String serverUrl) {
        putStringInPrefs(applicationContext.getString(R.string.server_url_prefs_string), serverUrl);

        RemoteConnectionManager.setServerUrl(serverUrl);
    }

    public static Integer getServerPort() {
        return getIntFromPrefs(applicationContext.getResources().getString(R.string.server_port_prefs_string), 0);
    }

    public static void setServerPort(Integer serverPort) {
        putIntInPrefs(applicationContext.getString(R.string.server_port_prefs_string), serverPort);

        RemoteConnectionManager.setServerPort(serverPort);
    }

    public static String getProxyServerUrl() {
        return getStringFromPrefs(applicationContext.getResources()
                                                    .getString(R.string.proxy_server_url_prefs_string), "");
    }

    public static void setProxyServerUrl(String proxyUrl) {
        putStringInPrefs(applicationContext.getString(R.string.proxy_server_url_prefs_string), proxyUrl);

        RemoteConnectionManager.setProxyServerUrl(proxyUrl);
    }

    public static Integer getProxyServerPort() {
        return getIntFromPrefs(applicationContext.getResources().getString(R.string.proxy_server_port_prefs_string), 0);
    }

    public static void setProxyServerPort(Integer proxyPort) {
        putIntInPrefs(applicationContext.getString(R.string.proxy_server_port_prefs_string), proxyPort);

        RemoteConnectionManager.setProxyServerPort(proxyPort);
    }

    public static boolean getUsingProxyServer() {
        return getBoolFromPrefs(applicationContext.getResources()
                                                  .getString(R.string.using_proxy_server_prefs_string), false);
    }

    public static void setUsingProxyServer(boolean usingProxyServer) {
        putBoolInPrefs(applicationContext.getString(R.string.using_proxy_server_prefs_string), usingProxyServer);

        RemoteConnectionManager.setUsingProxyServer(usingProxyServer);
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
        RemoteConnectionManager.setServerUrl(getServerUrl());
        RemoteConnectionManager.setServerPort(getServerPort());

        RemoteConnectionManager.setProxyServerUrl(getProxyServerUrl());
        RemoteConnectionManager.setProxyServerPort(getProxyServerPort());

        RemoteConnectionManager.setUsingProxyServer(getUsingProxyServer());

        if (rviApp != null)
            RemoteVehicleNode.removeApp(rviApp);

        rviApp = new VehicleApplication(applicationContext, RVI_APP_NAME, RVI_DOMAIN, "/vin/" + getVin(), serviceIdentifiers);
        rviApp.setListener(ourInstance);

        RemoteVehicleNode.addApp(rviApp);
        RemoteVehicleNode.connect();
    }

    public static void updateService(String service, String value) {
        rviApp.getService(service).setValue(value);
        rviApp.updateService(service);
    }

    @Override
    public void onServiceUpdated(VehicleService service) {
        mListener.onServiceUpdated(service.getServiceIdentifier(), service.getValue());
    }

    public static HVACManagerListener getListener() {
        return ourInstance.mListener;
    }

    public static void setListener(HVACManagerListener listener) {
        ourInstance.mListener = listener;
    }
}
