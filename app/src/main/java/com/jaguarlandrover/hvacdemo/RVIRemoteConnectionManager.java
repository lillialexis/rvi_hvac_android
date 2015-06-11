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

public class RVIRemoteConnectionManager implements RVIRemoteConnectionInterface.RemoteConnectionListener
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

    public static void startListening() {
        RVIRemoteConnectionInterface remoteConnection = ourInstance.selectEnabledRemoteConnection();

        if (remoteConnection == null) return;

        remoteConnection.setRemoteConnectionListener(ourInstance); // TODO: Doing it this way, dynamically selecting a connection at the beginning and later when sending messages,
                                                                   // TODO, cont': but only setting the listener here, will lead to funny async race conditions later; fix.
        remoteConnection.connect();
    }

    public static void sendRviRequest(RPCRequest request) {
        RVIRemoteConnectionInterface remoteConnection = ourInstance.selectConnectedRemoteConnection();

        if (remoteConnection == null) return;

        remoteConnection.sendRviRequest(request);
    }

    private RVIRemoteConnectionInterface selectConnectedRemoteConnection() {
        if (mDirectServerConnection.isEnabled() && mDirectServerConnection.isConnected())
            return mDirectServerConnection;
        if (mBluetoothConnection.isEnabled() && mBluetoothConnection.isConnected())
            return mBluetoothConnection;
        if (mProxyServerConnection.isEnabled() && mProxyServerConnection.isConnected())
            return mProxyServerConnection;

        return null;
    }

    private RVIRemoteConnectionInterface selectEnabledRemoteConnection() {
        if (mDirectServerConnection.isEnabled())
            return mDirectServerConnection;
        if (mBluetoothConnection.isEnabled())
            return mBluetoothConnection;
        if (mProxyServerConnection.isEnabled())
            return mProxyServerConnection;

        return null;
    }

    @Override
    public void onRemoteConnectionDidConnect() {

    }

    @Override
    public void onRemoteConnectionDidFailToConnect(Error error) {

    }

    @Override
    public void onRemoteConnectionDidReceiveData(String data) {

    }

    @Override
    public void onDidSendDataToRemoteConnection() {

    }

    @Override
    public void onDidFailToSendDataToRemoteConnection(Error error) {

    }
}
