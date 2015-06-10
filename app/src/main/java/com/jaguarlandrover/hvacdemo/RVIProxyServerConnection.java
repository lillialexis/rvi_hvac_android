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

import android.os.AsyncTask;

import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.io.*;
import java.net.*;

import java.util.List;
import java.util.Map;

public class RVIProxyServerConnection implements RVIRemoteConnection
{
    private final static String TAG = "HVACDemo:RVIProxyServerConnection";

    private ServerSocket serverSocket;

    private Handler updateConversationHandler;
    private Thread serverThread = null;

    public static final int SERVERPORT = 8807;
    private String mProxyServerUrl;


    @Override
    public void sendRviRequest(RPCRequest request) {
        if (!isConfigured())
            return;

        new AsyncRVIRequest().execute(request.jsonString());
    }

    private class AsyncRVIRequest extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strs) {

            String urlParameters = strs[0];
            Log.d(TAG, "Sending url parameters: " + urlParameters);

            HttpURLConnection connection = null;
            URL url;

            try
            {
                url = new URL(mProxyServerUrl);
                //url = new URL("http://rvi1.nginfotpdx.net:8801");//mProxyServerUrl);
                //url = new URL("http://192.168.6.86:8811");//http://rvi1.nginfotpdx.net:8801");//mProxyServerUrl);
                //url = new URL("http://posttestserver.com/post.php");//mProxyServerUrl);

                //Create connection
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json-rpc");
                connection.setRequestProperty("User-Agent", "objc-JSONRpc/1.0");

                connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                Map<String, List<String>> responseHeaders = connection.getHeaderFields();
                String responseString = connection.getResponseMessage();

                Log.d(TAG, "Response code: " + Integer.toString(connection.getResponseCode()));

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();

                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }

                rd.close();

                Log.d(TAG, "Got response: " + response.toString());

                return response.toString();

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
            finally
            {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

           @Override
           protected void onPostExecute(String result) {
               //Log.d(TAG, result);
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

    @Override
    public void connect() {
        connectSocket();
    }

    @Override
    public void disconnect() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectSocket() {
        Log.d(TAG, "Connecting the socket...");
        MyClientTask myClientTask = new MyClientTask(mProxyServerUrl, SERVERPORT);
        myClientTask.execute();
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port){
           dstAddress = addr;
           dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Starting auth sequence...");

            Socket socket = null;

            try {
                //socket = new Socket("192.168.4.189", 50000);
                socket = new Socket(dstAddress, dstPort);

//                ByteArrayOutputStream foo = new ByteArrayOutputStream(1024);
//                final byte[] authorizeMessageBytes = new byte[] {0x7b, 0x22, 0x74, 0x69, 0x64, 0x22, 0x3a, 0x31, 0x2c, 0x22, 0x63, 0x6d, 0x64, 0x22, 0x3a, 0x22, 0x61, 0x75, 0x22, 0x2c, 0x22, 0x61, 0x64, 0x64, 0x72, 0x22, 0x3a, 0x22, 0x33, 0x38, 0x2e, 0x31, 0x32, 0x39, 0x2e, 0x36, 0x34, 0x2e, 0x33, 0x31, 0x22, 0x2c, 0x22, 0x70, 0x6f, 0x72, 0x74, 0x22, 0x3a, 0x38, 0x38, 0x30, 0x37, 0x2c, 0x22, 0x76, 0x65, 0x72, 0x22, 0x3a, 0x22, 0x31, 0x2e, 0x30, 0x22, 0x2c, 0x22, 0x63, 0x65, 0x72, 0x74, 0x22, 0x3a, 0x22, 0x22, 0x2c, 0x22, 0x73, 0x69, 0x67, 0x6e, 0x22, 0x3a, 0x22, 0x22, 0x7d};
//                //final byte[] authorizeMessageBytes = new byte[] {0x00, 0x00, 0x00, 0x56, 0x7b, 0x22, 0x74, 0x69, 0x64, 0x22, 0x3a, 0x31, 0x2c, 0x22, 0x63, 0x6d, 0x64, 0x22, 0x3a, 0x22, 0x61, 0x75, 0x22, 0x2c, 0x22, 0x61, 0x64, 0x64, 0x72, 0x22, 0x3a, 0x22, 0x33, 0x38, 0x2e, 0x31, 0x32, 0x39, 0x2e, 0x36, 0x34, 0x2e, 0x33, 0x31, 0x22, 0x2c, 0x22, 0x70, 0x6f, 0x72, 0x74, 0x22, 0x3a, 0x38, 0x38, 0x30, 0x37, 0x2c, 0x22, 0x76, 0x65, 0x72, 0x22, 0x3a, 0x22, 0x31, 0x2e, 0x30, 0x22, 0x2c, 0x22, 0x63, 0x65, 0x72, 0x74, 0x22, 0x3a, 0x22, 0x22, 0x2c, 0x22, 0x73, 0x69, 0x67, 0x6e, 0x22, 0x3a, 0x22, 0x22, 0x7d};
//                String authorizeMessage;
//                int br;

//                foo.write(authorizeMessageBytes, 0, authorizeMessageBytes.length);
                String authorizeMessage = "{\"tid\":1,\"cmd\":\"au\",\"addr\":\"0.0.0.0\",\"port\":0,\"ver\":\"1.0\",\"cert\":\"\",\"sign\":\"\"}";//foo.toString("UTF-8");

                Log.d(TAG, "Sending auth message: " + authorizeMessage);

                DataOutputStream wr = new DataOutputStream(socket.getOutputStream());
                wr.writeBytes(authorizeMessage);
                wr.flush();

                Log.d(TAG, "Auth message sent. Waiting for response...");

//                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//                out.println(str);


//                //Get Response
//                InputStream is = socket.getInputStream();
//                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//                String line;
//                StringBuffer response = new StringBuffer();
//
//                while ((line = rd.readLine()) != null) {
//                while ((line = rd.readLine()) != -1) {
//                    response.append(line);
//                    response.append('\r');
//
//                    Log.d(TAG, "Got response: " + response.toString());
//
//                }
//
//                rd.close();

//                Log.d(TAG, "Got response: " + response.toString());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                /*
                 * notice:
                 * inputStream.read() will block if no data return
                 */
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");

                    Log.d(TAG, "Response so far: " + byteArrayOutputStream.toString("UTF-8"));
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
//                if (socket != null) {
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//
//                        e.printStackTrace();
//                    }
//                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            //textResponse.setText(response);
            super.onPostExecute(result);
        }
    }

    /*private void connectSocket() {
        updateConversationHandler = new Handler();
        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();
    }

    class ServerThread implements Runnable {
        public void run() {
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(SERVERPORT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    socket = serverSocket.accept();

                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String read = input.readLine();

                    updateConversationHandler.post(new updateUIThread(read));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class updateUIThread implements Runnable {
        private String msg;

        public updateUIThread(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
            Log.d(TAG, "Client says: " + msg);
            //text.setText(text.getText().toString() + "Client Says: " + msg + "\n");
        }
    }*/

    public String getProxyServerUrl() {
        return mProxyServerUrl;
    }

    public void setProxyServerUrl(String mProxyServerUrl) {
        this.mProxyServerUrl = mProxyServerUrl;
    }
}
