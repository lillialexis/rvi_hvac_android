package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RPCResponse.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/**
 * RPC Response object
 *
 * This object is created when the server responds.
 */
public class RPCResponse
{
    private final static String TAG = "HVACDemo:RPCResponse";

    /**
     * The RPC Version.
     */
    private String mVersion;

    /**
     * The id that was used in the request.
     */
    private String mId;

    /**
     * RPC Error. If nil, no error occurred
     */
    private Error mError;

    /**
     * Helper method to get an RPCResponse object with an error set
     *
     * @param Error error The error for the response
     */
    public RPCResponse(Error error) {

    }

    public RPCResponse() {

    }
}
