package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RVIServerConnection.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class RVIServerConnection implements RVIRemoteConnectionInterface
{
    private final static String TAG = "HVACDemo:RVIServerCo...";
    private RemoteConnectionListener mRemoteConnectionListener;

    private String  mServerUrl;
    private Integer mServerPort;

    Socket mSocket;

    @Override
    public void sendRviRequest(RVIServiceInvokeJSONObject serviceInvokeJSONObject) {
        if (!isConnected() || !isEnabled()) // TODO: Call error on listener
            return;

//        String data = "{\"tid\":1,\n" +
//                "\"cmd\":\"rcv\",\n" +
//                "\"mod\":\"proto_json_rpc\",\n" +
//                "\"data\":\"" + Base64.encodeToString(request.jsonString().getBytes(), Base64.DEFAULT) + "\"}";

        String data = serviceInvokeJSONObject.jsonString();

        new SendDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
    }

    @Override
    public boolean isConnected() {
        return mSocket != null && mSocket.isConnected();//true;
    }

    @Override
    public boolean isEnabled() {
        return !(mServerUrl == null || mServerUrl.isEmpty());
    }

    @Override
    public void connect() {
        connectSocket();
    }

    @Override
    public void disconnect() {
        try {
            if (mSocket != null)
                mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRemoteConnectionListener(RemoteConnectionListener remoteConnectionListener) {
        mRemoteConnectionListener = remoteConnectionListener;
    }

    private void connectSocket() {
        Log.d(TAG, "Connecting the socket: " + mServerUrl + ":" + mServerPort);

        ConnectAndListenTask connectAndAuthorizeTask = new ConnectAndListenTask(mServerUrl, mServerPort);
        connectAndAuthorizeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public class ConnectAndListenTask extends AsyncTask<Void, String, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        private final static String CONNECTION_UPDATE = "CONNECTION_UPDATE";
        private final static String DATA_UPDATE       = "DATA_UPDATE";
        private final static String CONNECTION_DID_SUCCEED = "CONNECTION_DID_SUCCEED";
        private final static String CONNECTION_DID_FAIL    = "CONNECTION_DID_FAIL";

        ConnectAndListenTask(String addr, int port){
           dstAddress = addr;
           dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "Starting auth sequence...");

            try {
                mSocket = new Socket(dstAddress, dstPort);

                publishProgress(ConnectAndListenTask.CONNECTION_UPDATE, ConnectAndListenTask.CONNECTION_DID_SUCCEED);

                //String authorizeMessage = "{\"tid\":1,\"cmd\":\"au\",\"addr\":\"0.0.0.0\",\"port\":0,\"ver\":\"1.0\",\"cert\":\"\",\"sign\":\"\"}"; // TODO: Abstract this out, obvs

                String authorizeMessage = new RVIAuthJSONObject().jsonString();
                new SendDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, authorizeMessage);

                String serviceAnnounceMessage = "{\"tid\":1,\"cmd\":\"au\",\"addr\":\"0.0.0.0\",\"port\":0,\"ver\":\"1.0\",\"cert\":\"\",\"sign\":\"\"}{\"tid\":1,\"cmd\":\"sa\",\"stat\":\"av\",\"svcs\":[\"jlr.com/android/987654321/hvac/unsubscribe\",\"jlr.com/android/987654321/hvac/subscribe\",\"jlr.com/android/987654321/hvac/defrost_max\",\"jlr.com/android/987654321/hvac/defrost_front\",\"jlr.com/android/987654321/hvac/airflow_direction\",\"jlr.com/android/987654321/hvac/seat_heat_left\",\"jlr.com/android/987654321/hvac/seat_heat_right\",\"jlr.com/android/987654321/hvac/hazard\",\"jlr.com/android/987654321/hvac/temp_right\",\"jlr.com/android/987654321/hvac/temp_left\",\"jlr.com/android/987654321/hvac/defrost_rear\",\"jlr.com/android/987654321/hvac/fan_speed\",\"jlr.com/android/987654321/hvac/fan\",\"jlr.com/android/987654321/hvac/air_circ\"],\"sign\":\"\"}";
                new SendDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serviceAnnounceMessage);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = mSocket.getInputStream();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");

                    Log.d(TAG, "Bytes read: " + bytesRead);
                    Log.d(TAG, "Response so far: " + byteArrayOutputStream.toString("UTF-8"));

                    // TODO: Buffer data for a complete json object

                    int lengthOfJsonObject = getLengthOfJsonObject(byteArrayOutputStream.toString("UTF-8"));

                    if (lengthOfJsonObject == bytesRead) {
                    publishProgress(ConnectAndListenTask.DATA_UPDATE, byteArrayOutputStream.toString("UTF-8"));
                        byteArrayOutputStream.reset();
                    } else if (lengthOfJsonObject < bytesRead && lengthOfJsonObject > 0) {
                        publishProgress(ConnectAndListenTask.DATA_UPDATE, byteArrayOutputStream.toString("UTF-8").substring(0, lengthOfJsonObject - 1));
                        byteArrayOutputStream.reset();

                        byteArrayOutputStream.write(buffer, lengthOfJsonObject - 1, bytesRead - lengthOfJsonObject);
                    } else {
                        ;
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();

                publishProgress(ConnectAndListenTask.CONNECTION_UPDATE, ConnectAndListenTask.CONNECTION_DID_FAIL, response);
            } catch (IOException e) {
                e.printStackTrace();
                response = "IOException: " + e.toString();

                publishProgress(ConnectAndListenTask.CONNECTION_UPDATE, ConnectAndListenTask.CONNECTION_DID_FAIL, response);
            } finally {
                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        // TODO: This method assumes that all strings start with a '{'
        private int getLengthOfJsonObject(String serverMessage) {
            int numberOfOpens  = 0;
            int numberOfCloses = 0;

            for (int i = 0; i < serverMessage.length(); i++) {
                if (serverMessage.charAt(i) == '{') numberOfOpens++;
                else if (serverMessage.charAt(i) == '}') numberOfCloses++;

                if (numberOfOpens == numberOfCloses) return i;
            }

            return -1;
        }

        @Override
        protected void onProgressUpdate(String... params) {
            super.onProgressUpdate(params);

            String updateType = params[0];

            if (updateType.equals(CONNECTION_UPDATE)) {
                String updateOutcome = params[1];
                if (updateOutcome.equals(CONNECTION_DID_SUCCEED))
                    mRemoteConnectionListener.onRemoteConnectionDidConnect();
                else
                    mRemoteConnectionListener.onRemoteConnectionDidFailToConnect(new Error(params[2]));
            } else {
                String data = params[1];

                mRemoteConnectionListener.onRemoteConnectionDidReceiveData(data);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class SendDataTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {

            String data = params[0];
            Log.d(TAG, "Sending data: " + data);

            DataOutputStream wr = null;

            try {
                wr = new DataOutputStream(mSocket.getOutputStream());

                wr.writeBytes(data);
                wr.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    public String getServerUrl() {
        return mServerUrl;
    }

    public void setServerUrl(String serverUrl) {
        mServerUrl = serverUrl;
    }

    public Integer getServerPort() {
        return mServerPort;
    }

    public void setServerPort(Integer serverPort) {
        mServerPort = serverPort;
    }

}
