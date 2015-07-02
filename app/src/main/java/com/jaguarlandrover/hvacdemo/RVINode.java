package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVINode.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 7/1/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.util.Log;

import java.util.ArrayList;

public class RVINode implements RVIRemoteConnectionListener
{
    private final static String TAG = "HVACDemo:RVINode";

    private static RVINode ourInstance = new RVINode();
    private RVINode() {
        RVIRemoteConnectionManager.setListener(this);
    }

    private ArrayList<RVIApp> mRVIApps = new ArrayList<>();

    public static RVINodeListener getListener() {
        return ourInstance.mListener;
    }

    public static void setListener(RVINodeListener listener) {
        ourInstance.mListener = listener;
    }

    public interface RVINodeListener {
        public void rviNodeDidConnect();
        public void rviNodeDidFailToConnect();
        public void rviNodeDidDisconnect();

    }
    private RVINodeListener mListener;

    public static void connect() {
        // are we configured
        // connect
        RVIRemoteConnectionManager.connect();

    }

    public static void disconnect() {
        // disconnect

        RVIRemoteConnectionManager.disconnect();
    }

    public static void addApp(RVIApp app) {
        RVINode.ourInstance.mRVIApps.add(app);
        RVINode.ourInstance.announceServices();
    }

    public static void removeApp(RVIApp app) {
        RVINode.ourInstance.mRVIApps.remove(app);
        RVINode.ourInstance.announceServices();
    }

    private void announceServices() {
        ArrayList<RVIService> allServices = new ArrayList<>();
        for (RVIApp app : mRVIApps)
            allServices.addAll(app.getServices());

        RVIRemoteConnectionManager.sendPacket(new RVIDlinkServiceAnnouncePacket(allServices));
    }

    @Override
    public void onRVIDidConnect() {
        RVIRemoteConnectionManager.sendPacket(new RVIDlinkAuthPacket());

        announceServices();

        mListener.rviNodeDidConnect();
    }

    @Override
    public void onRVIDidFailToConnect(Error error) {
        mListener.rviNodeDidFailToConnect();
    }

    @Override
    public void onRVIDidDisconnect() {
        mListener.rviNodeDidDisconnect();
    }

    @Override
    public void onRVIDidReceiveData(String data) {
        Log.d(TAG, "Data to parse: " + data);
        parseData(data);
        // parse data into packets
        // parse packets
        // do updates appropriately
    }

    // TODO: This method assumes that all strings start with a '{'
    private int getLengthOfJsonObject(String serverMessage) {
        int numberOfOpens  = 0;
        int numberOfCloses = 0;

        for (int i = 0; i < serverMessage.length(); i++) {
            if (serverMessage.charAt(i) == '{') numberOfOpens++;
            else if (serverMessage.charAt(i) == '}') numberOfCloses++;

            if (numberOfOpens == numberOfCloses) return i + 1;
        }

        return -1;
    }

    private void parseData(String data) {


//        // TODO: Buffer data for a complete json object
//
//        int lengthOfJsonObject = getLengthOfJsonObject(byteArrayOutputStream.toString("UTF-8"));
//
//        //Log.d(TAG, "Length of json object: " + lengthOfJsonObject);
//
//        if (lengthOfJsonObject == bytesRead) { /* Current data is 1 json object */
//            publishProgress(ConnectTask.DATA_UPDATE, byteArrayOutputStream.toString("UTF-8"));
//            byteArrayOutputStream.reset();
//
//            //Log.d(TAG, "AAAAAAA Current response: " + byteArrayOutputStream.toString("UTF-8"));
//
//        } else if (lengthOfJsonObject < bytesRead && lengthOfJsonObject > 0) {
//            publishProgress(ConnectTask.DATA_UPDATE, byteArrayOutputStream.toString("UTF-8").substring(0, lengthOfJsonObject - 1));
//            byteArrayOutputStream.reset();
//
//            byteArrayOutputStream.write(buffer, lengthOfJsonObject, bytesRead - lengthOfJsonObject);
//
//            //Log.d(TAG, "BBBBBBB Current response: " + byteArrayOutputStream.toString("UTF-8"));
//
//        } else {
//            //Log.d(TAG, "CCCCCCC Current response: " + byteArrayOutputStream.toString("UTF-8"));
//            ;
//        }
    }

    @Override
    public void onRVIDidSendData() {

    }

    @Override
    public void onRVIDidFailToSendData(Error error) {

    }
}
