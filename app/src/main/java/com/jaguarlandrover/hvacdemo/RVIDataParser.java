package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIDataParser.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 7/2/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.util.Log;
import com.google.gson.Gson;

import java.util.HashMap;

public class RVIDataParser
{
    private final static String TAG = "HVACDemo:RVIDataParser";

    private String mBuffer;
    private RVIDataParserListener mDataParserListener;

    public interface RVIDataParserListener
    {
        void onPacketParsed(RVIDlinkPacket packet);
    }

    public RVIDataParser(RVIDataParserListener listener) {
        mDataParserListener = listener;
    }

    // TODO: This method assumes that all strings start with a '{'
    private int getLengthOfJsonObject(String buffer) {
        int numberOfOpens  = 0;
        int numberOfCloses = 0;

        for (int i = 0; i < buffer.length(); i++) {
            if (buffer.charAt(i) == '{') numberOfOpens++;
            else if (buffer.charAt(i) == '}') numberOfCloses++;

            if (numberOfOpens == numberOfCloses) return i + 1;
        }

        return -1;
    }

    private RVIDlinkPacket stringToPacket(String string) {
        Log.d(TAG, "Parsing json blob: " + string);
        Gson gson = new Gson();
        HashMap jsonHash = gson.fromJson(string, HashMap.class);

//        String command = (String) jsonHash.get("cmd");
//
//        if (command.equals(RVIDlinkPacket.Command.AUTHORIZE.strVal())) {
//            return new RVIDlinkAuthPacket(jsonHash);
//        } else if (command.equals(RVIDlinkPacket.Command.SERVICE_ANNOUNCE.strVal())) {
//            return new RVIDlinkServiceAnnouncePacket(jsonHash);
//        } else if (command.equals(RVIDlinkPacket.Command.RECEIVE.strVal())) {
//            return new RVIDlinkReceivePacket(jsonHash);
//        } else {
//            return null;
//        }
        return null;
    }

    private String recurse(String buffer) {
        int lengthOfString     = buffer.length();
        int lengthOfJsonObject = getLengthOfJsonObject(buffer);

        if (lengthOfJsonObject == lengthOfString) { /* Current data is 1 json object */
            mDataParserListener.onPacketParsed(stringToPacket(buffer));

            return "";

        } else if (lengthOfJsonObject < lengthOfString && lengthOfJsonObject > 0) { /* Current data is more than 1 json object */
            mDataParserListener.onPacketParsed(stringToPacket(buffer.substring(0, lengthOfJsonObject - 1)));

            return recurse(buffer.substring(lengthOfJsonObject));

        } else { /* Current data is less than 1 json object */
            return buffer;
        }
    }

    public void parseData(String data) {
        if (mBuffer == null) mBuffer = "";

        mBuffer = recurse(mBuffer + data);
    }

    public void clear() {
        mBuffer = null;
    }
}
