package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIDlinkServiceAnnouncePacket.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 7/1/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class RVIDlinkServiceAnnouncePacket extends RVIDlinkPacket
{
    private final static String TAG = "HVACDemo:RVIDlinkServiceAnnouncePacket";

    @SerializedName("stat")
    private String mStatus;

    @SerializedName("svcs")
    private ArrayList<String> mServices;

//    protected HashMap<String, Object> jsonHash() {
////        Gson gson = new Gson();
//        HashMap<String, Object> jsonHash = super.jsonHash();
//
//        jsonHash.put("stat", mStatus);
//        jsonHash.put("svcs", mServices);
//
//        return jsonHash;
//    }

    ArrayList<String> getServiceFQNames(ArrayList<RVIService> services) {
        ArrayList<String> newList = new ArrayList<>(services.size());
        for (RVIService service : services)
            newList.add(service.getFullyQualifiedLocalServiceName());

        return newList;
    }

    /**
     * Helper method to get a service announce dlink json object
     *
     * @param services The array of services to announce
     *
     */
    public RVIDlinkServiceAnnouncePacket(ArrayList<RVIService> services) {
        super(Command.SERVICE_ANNOUNCE);

        mStatus   = "av"; // TODO: Confirm what this is/where is comes from
        mServices = getServiceFQNames(services);
    }

    public RVIDlinkServiceAnnouncePacket(HashMap jsonHash) {
        super(Command.SERVICE_ANNOUNCE, jsonHash);

        mStatus   = (String) jsonHash.get("stat");
        mServices = (ArrayList<String>) jsonHash.get("svcs");
    }

}
