package com.jaguarlandrover.hvacdemo;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIRemoteConnectionManagerListener.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 6/30/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public interface RVIRemoteConnectionManagerListener
{
    public void onRVIDidConnect();

    public void onRVIDidFailToConnect(Error error);

    public void onRVIDidDisconnect();

    public void onRVIDidReceivePacket(RVIDlinkPacket packet);

    public void onRVIDidSendPacket();

    public void onRVIDidFailToSendPacket(Error error);
}
