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

import java.net.*;

public class RVIProxyServerConnection implements RVIRemoteConnectionInterface
{
    private final static String TAG = "HVACDemo:RVIProxySer...";
    private RemoteConnectionListener mRemoteConnectionListener;

    public static final int SERVER_PORT = 8807;
    private String mProxyServerUrl;

    Socket mSocket;

    @Override
    public void sendRviRequest(RPCRequest request) {
        if (!isConnected() || !isEnabled()) // TODO: Call error on listener
            return;

        new AsyncRVIRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request.jsonString());
    }

    @Override
    public boolean isConnected() {
        return mSocket != null && mSocket.isConnected();//true;
    }

    @Override
    public boolean isEnabled() {
        return !(mProxyServerUrl == null || mProxyServerUrl.isEmpty());
    }

    @Override
    public void connect() {
        connectSocket();
    }

    @Override
    public void disconnect() {
        try {
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
        Log.d(TAG, "Connecting the socket...");

        String authorizeMessage = "{\"tid\":1,\"cmd\":\"au\",\"addr\":\"0.0.0.0\",\"port\":0,\"ver\":\"1.0\",\"cert\":\"\",\"sign\":\"\"}";

        ConnectAndAuthorizeTask connectAndAuthorizeTask = new ConnectAndAuthorizeTask(mProxyServerUrl, SERVER_PORT);
        connectAndAuthorizeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, authorizeMessage);
    }

    public class ConnectAndAuthorizeTask extends AsyncTask<String, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        ConnectAndAuthorizeTask(String addr, int port){
           dstAddress = addr;
           dstPort = port;
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.d(TAG, "Starting auth sequence...");

            String authorizeMessage = params[0];

            try {
                mSocket = new Socket(dstAddress, dstPort);

                Log.d(TAG, "Sending auth message: " + authorizeMessage);

                DataOutputStream wr = new DataOutputStream(mSocket.getOutputStream());
                wr.writeBytes(authorizeMessage);
                wr.flush();

                Log.d(TAG, "Auth message sent. Waiting for response...");

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

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = mSocket.getInputStream();

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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class AsyncRVIRequest extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... strs) {

            String urlParameters = strs[0];
            Log.d(TAG, "Sending url parameters: " + urlParameters);

            DataOutputStream wr = null;

            try {
                wr = new DataOutputStream(mSocket.getOutputStream());

                wr.writeBytes(urlParameters);
                wr.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }


//            HttpURLConnection connection = null;
//            URL url;
//
//            try {
//                url = new URL(mProxyServerUrl);
//                //url = new URL("http://rvi1.nginfotpdx.net:8801");//mProxyServerUrl);
//                //url = new URL("http://192.168.6.86:8811");//http://rvi1.nginfotpdx.net:8801");//mProxyServerUrl);
//                //url = new URL("http://posttestserver.com/post.php");//mProxyServerUrl);
//
//                //Create connection
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Content-Type", "application/json-rpc");
//                connection.setRequestProperty("User-Agent", "objc-JSONRpc/1.0");
//
//                connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
//                connection.setRequestProperty("Content-Language", "en-US");
//
//                connection.setUseCaches(false);
//                connection.setDoInput(true);
//                connection.setDoOutput(true);
//
//                //Send request
//                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//                wr.writeBytes(urlParameters);
//                wr.flush();
//                wr.close();
//
//                Map<String, List<String>> responseHeaders = connection.getHeaderFields();
//                String responseString = connection.getResponseMessage();
//
//                Log.d(TAG, "Response code: " + Integer.toString(connection.getResponseCode()));
//
//                //Get Response
//                InputStream is = connection.getInputStream();
//                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//                String line;
//                StringBuffer response = new StringBuffer();
//
//                while ((line = rd.readLine()) != null) {
//                    response.append(line);
//                    response.append('\r');
//                }
//
//                rd.close();
//
//                Log.d(TAG, "Got response: " + response.toString());
//
//                return response.toString();
//
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//                return null;
//            }
//            finally
//            {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //Log.d(TAG, result);
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
                serverSocket = new ServerSocket(SERVER_PORT);
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
