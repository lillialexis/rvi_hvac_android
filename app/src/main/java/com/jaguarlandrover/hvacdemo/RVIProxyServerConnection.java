package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIProxyServerConnection.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RVIProxyServerConnection implements RVIRemoteConnection
{
    private final static String TAG = "HVACDemo:RVIProxyServerConnection";

    private String mProxyServerUrl;

    @Override
    public String sendRviRequest(RPCRequest request) {
        if (!isConfigured())
            return null;

        HttpURLConnection connection = null;
        URL url;

        try {
            String urlParameters = request.jsonString();

            Log.d(TAG, "Sending url parameters: " + urlParameters);

            //Create connection
            url = new URL(mProxyServerUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json-rpc");
            connection.setRequestProperty("User-Agent", "objc-JSONRpc/1.0");

            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();

            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            rd.close();

            Log.d(TAG, "Got response: " + response.toString());

            return response.toString();

            } catch (Exception e) {

            e.printStackTrace();
                return null;

            } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }


    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public boolean isConfigured() {
        return !(mProxyServerUrl == null || mProxyServerUrl.isEmpty());
    }

    public String getProxyServerUrl() {
        return mProxyServerUrl;
    }

    public void setProxyServerUrl(String mProxyServerUrl) {
        this.mProxyServerUrl = mProxyServerUrl;
    }
}
