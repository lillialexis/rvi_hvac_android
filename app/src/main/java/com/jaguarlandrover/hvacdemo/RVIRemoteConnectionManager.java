package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIRemoteConnectionManager.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class RVIRemoteConnectionManager
{
    private final static String TAG = "HVACDemo:RVIRemoteConnectionManager";

    private static RVIRemoteConnectionManager ourInstance = new RVIRemoteConnectionManager();

    //private static RVIRemoteConnectionManager getInstance() {
    //    return ourInstance;
    //}

    private RVIRemoteConnectionManager() {
        mProxyServerConnection  = new RVIProxyServerConnection();
        mBluetoothConnection    = new RVIBluetoothConnection();
        mDirectServerConnection = new RVIDirectServerConnection();
    }

    private RVIProxyServerConnection  mProxyServerConnection;
    private RVIBluetoothConnection    mBluetoothConnection;
    private RVIDirectServerConnection mDirectServerConnection;

    public static void setProxyServerUrl(String proxyServerUrl) {
        RVIRemoteConnectionManager.ourInstance.mProxyServerConnection.setProxyServerUrl(proxyServerUrl);
    }

    public static void sendRviRequest(RPCRequest request) {
        RVIRemoteConnection remoteConnection = ourInstance.selectRemoteConnection();

        if (remoteConnection == null) return;

        remoteConnection.sendRviRequest(request);
    }

    private RVIRemoteConnection selectRemoteConnection() {
        if (mDirectServerConnection.isConfigured() && mDirectServerConnection.isConnected())
            return mDirectServerConnection;
        if (mBluetoothConnection.isConfigured() && mBluetoothConnection.isConnected())
            return mBluetoothConnection;
        if (mProxyServerConnection.isConfigured() && mProxyServerConnection.isConnected())
            return mProxyServerConnection;

        return null;
    }
}
