package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    HVACManager.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;
import com.google.gson.internal.LinkedTreeMap;
import com.jaguarlandrover.pki.PKICertificateResponse;
import com.jaguarlandrover.pki.PKICertificateSigningRequestRequest;
import com.jaguarlandrover.pki.PKIManager;
import com.jaguarlandrover.pki.PKIServerResponse;
import com.jaguarlandrover.rvi.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;

import static android.content.Context.MODE_PRIVATE;

public class HVACManager// implements RVIRemoteNodeListener
{
    private final static String TAG = "HVACDemo:HVACManager";

    private final static String RVI_DOMAIN      = "genivi.org";
    private final static String RVI_BUNDLE_NAME = "hvac";

    private final static String X509_PRINCIPAL_PATTERN = "CN=%s, O=Genivi, OU=%s";
    private final static String X509_ORG_UNIT          = "Android Unlock App";

    private final static String PROVISIONING_SERVER_BASE_URL = "http://38.129.64.40:8000";
    private final static String PROVISIONING_SERVER_CSR_URL  = "/csr_veh";

    private static Context applicationContext = HVACApplication.getContext();
    //private static ServiceBundle hvacServiceBundle;

    private static HVACManager ourInstance = new HVACManager();

    private static RVIRemoteNode node;

    private final static ArrayList<String> localServiceIdentifiers =
            new ArrayList<>(Arrays.asList(
                    "hvac/" + HVACServiceIdentifier.HAZARD.value(),
                    "hvac/" + HVACServiceIdentifier.TEMP_LEFT.value(),
                    "hvac/" + HVACServiceIdentifier.TEMP_RIGHT.value(),
                    "hvac/" + HVACServiceIdentifier.SEAT_HEAT_LEFT.value(),
                    "hvac/" + HVACServiceIdentifier.SEAT_HEAT_RIGHT.value(),
                    "hvac/" + HVACServiceIdentifier.FAN_SPEED.value(),
                    "hvac/" + HVACServiceIdentifier.AIRFLOW_DIRECTION.value(),
                    "hvac/" + HVACServiceIdentifier.DEFROST_REAR.value(),
                    "hvac/" + HVACServiceIdentifier.DEFROST_FRONT.value(),
                    "hvac/" + HVACServiceIdentifier.DEFROST_MAX.value(),
                    "hvac/" + HVACServiceIdentifier.AIR_CIRC.value(),
                    "hvac/" + HVACServiceIdentifier.AC.value(),
                    "hvac/" + HVACServiceIdentifier.AUTO.value()//,
                    //HVACServiceIdentifier.SUBSCRIBE.value(),
                    //HVACServiceIdentifier.UNSUBSCRIBE.value()
            ));

    //private enum ConnectionStatus
    //{
    //    DISCONNECTED,
    //    READY_TO_CONNECT,
    //    CONNECTING,
    //    CONNECTED
    //}

    //private static ConnectionStatus connectionStatus = ConnectionStatus.DISCONNECTED;
    private static boolean readyToConnect = false;
    private static boolean pkiComplete = false;

    private HVACManagerListener mListener;

    interface HVACManagerListener
    {
        void onNodeConnected();

        void onNodeDisconnected();

        void onServiceInvoked(String serviceIdentifier, Object parameters);
    }

    private HVACManager() {
        node = new RVIRemoteNode(applicationContext);

        node.setListener(new RVIRemoteNodeListener()
        {
            @Override
            public void nodeDidConnect(RVIRemoteNode node) {
                Log.d(TAG, "RVI node has successfully connected.");
                //connectionStatus = ConnectionStatus.CONNECTED;
                if(mListener != null) mListener.onNodeConnected();
                HVACManager.subscribeToHvacRvi();
            }

            @Override
            public void nodeDidFailToConnect(RVIRemoteNode node, Throwable reason) {
                Log.d(TAG, "RVI node failed to connect: " + ((reason == null) ? "(null)" : reason.getLocalizedMessage()));
                //connectionStatus = ConnectionStatus.DISCONNECTED;
                if(mListener != null) mListener.onNodeDisconnected();
            }

            @Override
            public void nodeDidDisconnect(RVIRemoteNode node, Throwable reason) {
                Log.d(TAG, "RVI node did disconnect: " + ((reason == null) ? "(null)" : reason.getLocalizedMessage()));
                //connectionStatus = ConnectionStatus.DISCONNECTED;
                if(mListener != null) mListener.onNodeDisconnected();
            }

            @Override
            public void nodeSendServiceInvocationSucceeded(RVIRemoteNode node, String serviceIdentifier) {

            }

            @Override
            public void nodeSendServiceInvocationFailed(RVIRemoteNode node, String serviceIdentifier, Throwable reason) {

            }

            @Override
            public void nodeReceiveServiceInvocationSucceeded(RVIRemoteNode node, String serviceIdentifier, Object parameters) {
                if (mListener != null) mListener.onServiceInvoked(serviceIdentifier, ((LinkedTreeMap) parameters).get("value"));
            }

            @Override
            public void nodeReceiveServiceInvocationFailed(RVIRemoteNode node, String serviceIdentifier, Throwable reason) {

            }

            @Override
            public void nodeDidAuthorizeLocalServices(RVIRemoteNode node, Set<String> serviceIdentifiers) {

            }

            @Override
            public void nodeDidAuthorizeRemoteServices(RVIRemoteNode node, Set<String> serviceIdentifiers) {

            }
        });
    }

    private static SharedPreferences getPrefs() {
        return applicationContext.getSharedPreferences(applicationContext.getString(R.string.hvac_shared_prefs_string), MODE_PRIVATE);
    }

    private static String getStringFromPrefs(String key, String defaultValue) {
        return getPrefs().getString(key, defaultValue);
    }

    private static Integer getIntFromPrefs(String key, Integer defaultValue) {
        return getPrefs().getInt(key, defaultValue);
    }

    private static Boolean getBoolFromPrefs(String key, Boolean defaultValue) {
        return getPrefs().getBoolean(key, defaultValue);
    }

    private static void putStringInPrefs(String key, String value) {
        SharedPreferences sharedPref = getPrefs();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static void putIntInPrefs(String key, Integer value) {
        SharedPreferences sharedPref = getPrefs();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static void putBoolInPrefs(String key, Boolean value) {
        SharedPreferences sharedPref = getPrefs();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

//    public static String getVin() {
//        return getStringFromPrefs(applicationContext.getResources().getString(R.string.vehicle_vin_prefs_string), "");
//    }
//
//    public static void setVin(String vin) {
//        putStringInPrefs(applicationContext.getString(R.string.vehicle_vin_prefs_string), vin);
//
//        if (hvacServiceBundle != null)
//            hvacServiceBundle.setRemotePrefix(vin);
//    }

    static String getServerUrl() {
        return getStringFromPrefs(applicationContext.getResources().getString(R.string.server_url_prefs_string), "");
    }

    static void setServerUrl(String serverUrl) {
        putStringInPrefs(applicationContext.getString(R.string.server_url_prefs_string), serverUrl);

        //RemoteConnectionManager.setServerUrl(serverUrl);
    }

    static Integer getServerPort() {
        return getIntFromPrefs(applicationContext.getResources().getString(R.string.server_port_prefs_string), 0);
    }

    static void setServerPort(Integer serverPort) {
        putIntInPrefs(applicationContext.getString(R.string.server_port_prefs_string), serverPort);

        //RemoteConnectionManager.setServerPort(serverPort);
    }

    static String getProxyServerUrl() {
        return getStringFromPrefs(applicationContext.getResources()
                                                    .getString(R.string.proxy_server_url_prefs_string), "");
    }

    static void setProxyServerUrl(String proxyUrl) {
        putStringInPrefs(applicationContext.getString(R.string.proxy_server_url_prefs_string), proxyUrl);

        //RemoteConnectionManager.setProxyServerUrl(proxyUrl);
    }

    static Integer getProxyServerPort() {
        return getIntFromPrefs(applicationContext.getResources().getString(R.string.proxy_server_port_prefs_string), 0);
    }

    static void setProxyServerPort(Integer proxyPort) {
        putIntInPrefs(applicationContext.getString(R.string.proxy_server_port_prefs_string), proxyPort);

        //RemoteConnectionManager.setProxyServerPort(proxyPort);
    }

    static boolean getUsingProxyServer() {
        return getBoolFromPrefs(applicationContext.getResources()
                                                  .getString(R.string.using_proxy_server_prefs_string), false);
    }

    static void setUsingProxyServer(boolean usingProxyServer) {
        putBoolInPrefs(applicationContext.getString(R.string.using_proxy_server_prefs_string), usingProxyServer);

        //RemoteConnectionManager.setUsingProxyServer(usingProxyServer);
    }

    static void initializeRvi() {
        RVILocalNode.start(HVACApplication.getContext(), RVI_DOMAIN);

        if (PKIManager.hasValidDeviceCert(HVACApplication.getContext()) && PKIManager.hasValidServerCert(HVACApplication.getContext())) {
            setUpRviAndConnectToServer(PKIManager.getServerKeyStore(HVACApplication.getContext()), PKIManager.getDeviceKeyStore(HVACApplication.getContext()), null, null);
        } else {
            generateKeysAndCerts();
        }
    }

    private static void generateKeysAndCerts() {
        Log.d(TAG, "Certs not found. Generating keys and certs...");

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 1);

        PKIManager.generateKeyPairAndCertificateSigningRequest(HVACApplication.getContext(), new PKIManager.CertificateSigningRequestGeneratorListener() {
            @Override
            public void generateCertificateSigningRequestSucceeded(String certificateSigningRequest) {
                Log.d(TAG, "Certificate signing request generated. Sending to server...");

                sendCertificateSigningRequest(certificateSigningRequest);
            }

            @Override
            public void generateCertificateSigningRequestFailed(Throwable reason) {
                Log.e(TAG, reason.getLocalizedMessage());
            }

        }, start.getTime(), end.getTime(), X509_PRINCIPAL_PATTERN, RVILocalNode.getLocalNodeIdentifier(HVACApplication.getContext()), X509_ORG_UNIT);
    }

    private static void sendCertificateSigningRequest(String certificateSigningRequest) {
        PKICertificateSigningRequestRequest request = new PKICertificateSigningRequestRequest(certificateSigningRequest);

        PKIManager.sendCertificateSigningRequest(HVACApplication.getContext(), new PKIManager.ProvisioningServerListener() {
            @Override
            public void managerDidReceiveResponseFromServer(PKIServerResponse response) {
                if (response.getStatus() == PKIServerResponse.Status.VERIFICATION_NEEDED) {
                    Log.e(TAG, "Problem: verification needed...");

                } else if (response.getStatus() == PKIServerResponse.Status.CERTIFICATE_RESPONSE) {
                    Log.d(TAG, "Certificate signing request received and server sent back certs and creds.");

                    PKICertificateResponse certificateResponse = (PKICertificateResponse) response;

                    setUpRviAndConnectToServer(certificateResponse.getServerKeyStore(), certificateResponse.getDeviceKeyStore(), null, certificateResponse.getJwtCredentials());

                } else if (response.getStatus() == PKIServerResponse.Status.ERROR) {
                    Log.e(TAG, "Error from server");

                }
            }

        }, PROVISIONING_SERVER_BASE_URL, PROVISIONING_SERVER_CSR_URL, request);

    }

    private static void setUpRviAndConnectToServer(KeyStore serverCertificateKeyStore, KeyStore deviceCertificateKeyStore, String deviceCertificatePassword, ArrayList<String> newCredentials) {

        try {
            RVILocalNode.setServerKeyStore(serverCertificateKeyStore);
            RVILocalNode.setDeviceKeyStore(deviceCertificateKeyStore);
            RVILocalNode.setDeviceKeyStorePassword(deviceCertificatePassword);

            if (newCredentials != null)
                RVILocalNode.setCredentials(HVACApplication.getContext(), newCredentials);

            pkiComplete = true;

            if (readyToConnect)
                start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean isRviConfigured() {
        //if (getVin()        == null || getVin().isEmpty())       return false;
        if (getServerUrl()  == null || getServerUrl().isEmpty()) return false;
        if (getServerPort() == 0)                                return false;

        if (getUsingProxyServer()) {
            if (getProxyServerUrl()  == null || getProxyServerUrl().isEmpty()) return false;
            if (getProxyServerPort() == 0)                                     return false;
        }

        return true;
    }

    //private static KeyStore getKeyStore(String fileName, String type, String password) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException { // type = "jks"?
    //    AssetManager assetManager = applicationContext.getAssets();
    //    InputStream fis = assetManager.open(fileName);
    //
    //    KeyStore ks = KeyStore.getInstance(type);
    //    ks.load(fis, password.toCharArray());
    //    fis.close();
    //
    //    return ks;
    //}

    static void start() {
        readyToConnect = true;//connectionStatus = ConnectionStatus.READY_TO_CONNECT;

        if (!pkiComplete) return;

        if (getUsingProxyServer()) {
            node.setServerUrl(getProxyServerUrl());
            node.setServerPort(getProxyServerPort());

        } else {
            node.setServerUrl(getServerUrl());
            node.setServerPort(getServerPort());
        }

        //try {
        //    node.setKeyStores(getKeyStore("server-certs", "BKS", "password"), getKeyStore("client.p12", "PKCS12", "password"), "password");
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}

        //node.addJWTCredentials("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyaWdodF90b19pbnZva2UiOlsiZ2VuaXZpLm9yZyJdLCJpc3MiOiJqbHIuY29tIiwiZGV2aWNlX2NlcnQiOiJNSUlCOHpDQ0FWd0NBUUV3RFFZSktvWklodmNOQVFFTEJRQXdRakVMTUFrR0ExVUVCaE1DVlZNeER6QU5CZ05WQkFnTUJrOXlaV2R2YmpFUk1BOEdBMVVFQnd3SVVHOXlkR3hoYm1ReER6QU5CZ05WQkFvTUJrZEZUa2xXU1RBZUZ3MHhOVEV4TWpjeU16RTBOVEphRncweE5qRXhNall5TXpFME5USmFNRUl4Q3pBSkJnTlZCQVlUQWxWVE1ROHdEUVlEVlFRSURBWlBjbVZuYjI0eEVUQVBCZ05WQkFjTUNGQnZjblJzWVc1a01ROHdEUVlEVlFRS0RBWkhSVTVKVmtrd2daOHdEUVlKS29aSWh2Y05BUUVCQlFBRGdZMEFNSUdKQW9HQkFKdHZpTThBUklyRnF1UGMwbXlCOUJ1RjlNZGtBLzJTYXRxYlpNV2VUT1VKSEdyakJERUVNTFE3ems4QXlCbWk3UnF1WVlaczY3U3lMaHlsVkdLaDZzSkFsZWN4YkhVd2o3Y1pTUzFibUtNamU2TDYxZ0t3eEJtMk5JRlUxY1ZsMmpKbFRhVTlWWWhNNHhrNTd5ajI4bmtOeFNZV1AxdmJGWDJORFgyaUg3YjVBZ01CQUFFd0RRWUpLb1pJaHZjTkFRRUxCUUFEZ1lFQWhicVZyOUUvME03MjluYzZESStxZ3FzUlNNZm95dkEzQ21uL0VDeGwxeWJHa3V6TzdzQjhmR2pnTVE5enpjYjZxMXVQM3dHalBpb3FNeW1pWVlqVW1DVHZ6ZHZSQlorNlNEanJaZndVdVlleGlLcUk5QVA2WEthSGxBTDE0K3JLKzZITjR1SWtaY0l6UHdTTUhpaDFic1RScHlZNVozQ1VEY0RKa1l0VmJZcz0iLCJ2YWxpZGl0eSI6eyJzdGFydCI6MTQ1MjE5Mjc3Nywic3RvcCI6MTQ4MzcyODc3N30sInJpZ2h0X3RvX3JlZ2lzdGVyIjpbImdlbml2aS5vcmciXSwiY3JlYXRlX3RpbWVzdGFtcCI6MTQ1MjE5Mjc3NywiaWQiOiJpbnNlY3VyZV9jcmVkZW50aWFscyJ9.TBDUJFL1IQ039Lz7SIkcblhz62jO35STJ8OiclL_xlxEE_L_EjnELrDOGvkIh7zhhl8RMHkUJcTFQKF7P6WDJ5rUJejXJlkTRf-aVmHqEhpspRw6xD2u_2A9wmTWLJF94_wsEb7M7xWCXVrbexu_oik85zmuxRQgRE5wrTC7DDQ");

        //if (hvacServiceBundle != null)
        //    node.removeBundle(hvacServiceBundle);
        //
        //hvacServiceBundle = new ServiceBundle(applicationContext, RVI_DOMAIN, RVI_BUNDLE_NAME, localServiceIdentifiers);
        //hvacServiceBundle.setListener(ourInstance);

        //node.addBundle(hvacServiceBundle);

        RVILocalNode.addLocalServices(HVACApplication.getContext(), localServiceIdentifiers);

        //connectionStatus = ConnectionStatus.CONNECTING;
        node.connect();
    }

    static void restart() {
        node.disconnect();
        node.connect();
    }

    static void subscribeToHvacRvi() {
        invokeService(HVACServiceIdentifier.SUBSCRIBE.value(),
                "{\"node\":\"" + RVI_DOMAIN + "/" + RVILocalNode.getLocalNodeIdentifier(applicationContext) + "/\"}");
    }

    static void invokeService(String serviceIdentifier, String value) {
        HashMap<String, Object> invokeParams = new HashMap<>(2);

        invokeParams.put("sending_node", RVI_DOMAIN + "/" + RVILocalNode.getLocalNodeIdentifier(applicationContext) + "/");
        invokeParams.put("value", value);

        node.invokeService(serviceIdentifier, invokeParams, 360000);
    }

//    @Override
//    public void onServiceInvoked(ServiceBundle serviceBundle, String serviceIdentifier, Object parameters) {
//        if (mListener != null) mListener.onServiceInvoked(serviceIdentifier, ((LinkedTreeMap) parameters).get("value"));
//    }

    public static HVACManagerListener getListener() {
        return ourInstance.mListener;
    }

    static void setListener(HVACManagerListener listener) {
        ourInstance.mListener = listener;
    }
}
