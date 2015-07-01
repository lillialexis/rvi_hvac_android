package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIDlinkAuthPacket.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 6/15/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.util.HashMap;

public class RVIDlinkAuthPacket extends RVIDlinkPacket
{
    private final static String TAG = "HVACDemo:RVIDlinkAuthPacket";

    private String mAddr;

    private Integer mPort;

    private String mVer;

    private String mCert;

    protected HashMap<String, Object> jsonHash() {
        HashMap<String, Object> jsonHash = super.jsonHash();

        jsonHash.put("addr", mAddr);
        jsonHash.put("port", mPort);
        jsonHash.put("ver",  mVer);
        jsonHash.put("cert", mCert);

        return jsonHash;
    }

    /**
     * Helper method to get an authorization json object
     */
    public RVIDlinkAuthPacket() {
        super(Command.AUTHORIZE);

        mAddr = "0.0.0.0";
        mPort = 0;
        mVer  = "1.0";
        mCert = "";
    }

    public RVIDlinkAuthPacket(HashMap jsonHash) {
        super(Command.AUTHORIZE, jsonHash);

        mAddr = (String)  jsonHash.get("addr");
        mPort = (Integer) jsonHash.get("port");
        mVer  = (String)  jsonHash.get("ver");
        mCert = (String)  jsonHash.get("cert");
    }

}
