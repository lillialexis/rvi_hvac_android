package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIDirectServerConnection.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class RVIDirectServerConnection implements RVIRemoteConnection
{
    private final static String TAG = "HVACDemo:RVIDirectServerConnection";

    @Override
    public void sendRviRequest(RPCRequest request) {
        return;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isConfigured() {
        return false;
    }
}
