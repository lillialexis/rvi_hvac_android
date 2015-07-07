package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIDlinkPacketTest.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 7/6/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.test.AndroidTestCase;
import android.util.Log;
import com.google.gson.Gson;

import java.util.HashMap;

public class RVIDlinkPacketTest extends AndroidTestCase
{
    private final static String TAG = "HVACDemo:RVIDlinkPacketTest";

    private Integer tidCounter = 0;

    private RVIDlinkPacket mPacket;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();


    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private HashMap jsonObjectFromString(String string) {

        Gson gson = new Gson();
        return gson.fromJson(string, HashMap.class);
    }

    public final void testConstructorCommandAuthorize() {
        mPacket = new RVIDlinkPacket(RVIDlinkPacket.Command.AUTHORIZE);

        assertEquals(mPacket.mCmd, RVIDlinkPacket.Command.AUTHORIZE);
        assertEquals(mPacket.mSig, "");
    }

    public final void testConstructorCommandServiceAnnounce() {
        mPacket = new RVIDlinkPacket(RVIDlinkPacket.Command.SERVICE_ANNOUNCE);

        assertEquals(mPacket.mCmd, RVIDlinkPacket.Command.SERVICE_ANNOUNCE);
        assertEquals(mPacket.mSig, "");
    }

    public final void testConstructorCommandReceive() {
        mPacket = new RVIDlinkPacket(RVIDlinkPacket.Command.RECEIVE);

        assertEquals(mPacket.mCmd, RVIDlinkPacket.Command.RECEIVE);
        assertEquals(mPacket.mSig, "");
    }

    public final void testConstructorCommandAuthorizeGoodJson() {
        String jsonString = "{\"cmd\":\"au\",\"sign\":\"\",\"tid\":1}";

        mPacket = new RVIDlinkPacket(RVIDlinkPacket.Command.AUTHORIZE, jsonObjectFromString(jsonString));

        assertEquals(mPacket.mTid, Integer.valueOf(1));
        assertEquals(mPacket.mCmd, RVIDlinkPacket.Command.AUTHORIZE);
        assertEquals(mPacket.mSig, "");
    }

    public final void testConstructorCommandServiceAnnounceGoodJson() {
        String jsonString = "{\"cmd\":\"sa\",\"sign\":\"\",\"tid\":1}";

        mPacket = new RVIDlinkPacket(RVIDlinkPacket.Command.SERVICE_ANNOUNCE, jsonObjectFromString(jsonString));

        assertEquals(mPacket.mTid, Integer.valueOf(1));
        assertEquals(mPacket.mCmd, RVIDlinkPacket.Command.SERVICE_ANNOUNCE);
        assertEquals(mPacket.mSig, "");
    }

    public final void testConstructorCommandReceiveGoodJson() {
        String jsonString = "{\"cmd\":\"rcv\",\"sign\":\"\",\"tid\":1}";

        mPacket = new RVIDlinkPacket(RVIDlinkPacket.Command.RECEIVE, jsonObjectFromString(jsonString));

        assertEquals(mPacket.mTid, Integer.valueOf(1));
        assertEquals(mPacket.mCmd, RVIDlinkPacket.Command.RECEIVE);
        assertEquals(mPacket.mSig, "");
    }

    public final void testConstructorNullCommand() {
        try {
            mPacket = new RVIDlinkPacket(null);
        } catch (IllegalArgumentException e) {
            /* Success */
            return;
        }

        fail("IllegalArgumentException expected but was not thrown.");
    }

    public final void testConstructorNullCommandGoodJson() {
        String jsonString = "{\"sign\":\"\",\"tid\":1}";

        try {
            mPacket = new RVIDlinkPacket(null, jsonObjectFromString(jsonString));
        } catch (IllegalArgumentException e) {
            /* Success */
            return;
        }

        fail("IllegalArgumentException expected but was not thrown.");
    }

    public final void testConstructorNullJson() {

        try {
            mPacket = new RVIDlinkPacket(RVIDlinkPacket.Command.AUTHORIZE, null);
        } catch (IllegalArgumentException e) {
            /* Success */
            return;
        }

        fail("IllegalArgumentException expected but was not thrown.");
    }

    public final void testConstructorEmptyJson() {
        String jsonString = "{}";

        mPacket = new RVIDlinkPacket(RVIDlinkPacket.Command.AUTHORIZE, jsonObjectFromString(jsonString));

        assertNull(mPacket.mTid);
        assertNull(mPacket.mSig);
    }

    public final void testJsonString() {
        mPacket = new RVIDlinkPacket(RVIDlinkPacket.Command.AUTHORIZE);

        Log.d(TAG, mPacket.toJsonString());

    }
}
