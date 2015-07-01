package com.jaguarlandrover.hvacdemo;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIListener.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 6/30/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
public interface RVIListener
{
    public void onRVIDidConnect();

    public void onRVIDidFailToConnect(Error error);

    public void onRVIDidDisconnect();

    public void onRVIDidReceiveData(String data);

    public void onRVIDidSendData();

    public void onRVIDidFailToSendData(Error error);
}
