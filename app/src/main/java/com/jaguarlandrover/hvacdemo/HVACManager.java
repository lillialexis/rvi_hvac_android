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
import android.util.Log;
import com.google.gson.internal.LinkedTreeMap;
import com.jaguarlandrover.pki.PKICertificateResponse;
import com.jaguarlandrover.pki.PKICertificateSigningRequestRequest;
import com.jaguarlandrover.pki.PKIManager;
import com.jaguarlandrover.pki.PKIServerResponse;
import com.jaguarlandrover.rvi.*;

import java.security.KeyStore;
import java.util.*;

import static android.content.Context.MODE_PRIVATE;

public class HVACManager
{
    private final static String TAG = "HVACDemo:HVACManager";

    private final static String RVI_DOMAIN             = "genivi.org";
    private final static String HVAC_BUNDLE_IDENTIFIER = "hvac";

    private final static String X509_PRINCIPAL_PATTERN = "CN=%s, O=Genivi, OU=%s";
    private final static String X509_ORG_UNIT          = "Android Unlock App";

    private final static String PROVISIONING_SERVER_BASE_URL = "http://38.129.64.40:8000";
    private final static String PROVISIONING_SERVER_CSR_URL  = "/csr_veh";

    private static ArrayList<String> SIGNED_CREDENTIALS =
            new ArrayList<>(Arrays.asList(
                    "eyJhbGciOiAiUlMyNTYiLCJ0eXAiOiAiSldUIn0=.eyJpc3MiOiAiR0VOSVZJIiwgInJpZ2h0X3RvX2ludm9rZSI6IFsiZ2VuaXZpLm9yZy92ZWhpY2xlLysvaHZhYy8rIl0sICJyaWdodF90b19yZWNlaXZlIjogWyJnZW5pdmkub3JnL2FuZHJvaWQvKy9odmFjLysiXSwgInZhbGlkaXR5IjogeyAic3RhcnQiOiAxNDUyNDcwNDAwLCAic3RvcCI6IDE0ODQwOTI4MDB9LCAiZGV2aWNlX2NlcnQiOiAiTUlJRVpEQ0NBa3dDQmdGWWpvZWRVakFOQmdrcWhraUc5dzBCQVFzRkFEQ0JrekVMTUFrR0ExVUVCaE1DVlZNeER6QU5CZ05WQkFnTUJrOXlaV2R2YmpFUk1BOEdBMVVFQnd3SVVHOXlkR3hoYm1ReER6QU5CZ05WQkFvTUJrZEZUa2xXU1RFTU1Bb0dBMVVFQ3d3RFVsWkpNUmN3RlFZRFZRUUREQTR4T1RJdU1UWTRMakUyTGpJME5URW9NQ1lHQ1NxR1NJYjNEUUVKQVJZWmNHUjRiM04wWXk1aGJtUnliMmxrUUdkdFlXbHNMbU52YlRBZUZ3MHhOakV4TWpNd01ERXpNakphRncweE56RXhNak13TURFek1qSmFNRmN4SnpBbEJnTlZCQU1NSG1GdVpISnZhV1F2VEUxSmMwUk5iVk5SVDJGcWVGZGhVbWh1UlhCMVFURVBNQTBHQTFVRUNnd0dSMlZ1YVhacE1Sc3dHUVlEVlFRTERCSkJibVJ5YjJsa0lGVnViRzlqYXlCQmNIQXdnZ0VpTUEwR0NTcUdTSWIzRFFFQkFRVUFBNElCRHdBd2dnRUtBb0lCQVFERlJFY0kyUEpGYXJGLzRwYnNvd3NrWVB5L0NFWDBZTHRFVHE0QW5XTzM3K05PTU1YTlJYUW5uV25vaGRZaERGZ3dFU0tOenVPeFU3QjNQNFZhY1Nub2I5U3dCbitRUE9PTHVDcGI1dCs1NmE2THlIWHNFT1JyRjNjSlFZczU4SzdpQ3dMZ3c5OXpjRFpzSFgzVWJaUXI2RHdLTUQvbE9Jb1JLRW94UTZqSCtMcXRHTkdIOWxrZXpBd2paYWlteVMwTXRNUit3ZG5nQktBVXJtcnEwYzFYSFpwK1hXSXpxb0hzT0ZjejQvVlJaM25nczNPenE0VzR1MkNTa25nekNEOW9DdHlCK1JLL20rU1NxbFlZSFNrMXFRaW92OWYzQ3laRkFUZDVqVEhyZHFYdmtIUVMvOEsxUEdOVkdnMGxZdlhxaTJqVjFTNXZsRVhkaXlWODhRaUJBZ01CQUFFd0RRWUpLb1pJaHZjTkFRRUxCUUFEZ2dJQkFGWVF4V0dLTmZQNFBYNUM2UDF6R3hkMTBGQWN3SmtTYWhzaGw5SytDcS9iMFhtTE9SbCt1OFlsVWcyWWwvUGl2dEtxZ3c0ZWhRZlo5OUYyZ2lNWmltejVmejNxT3IyTzJXckswSHh3V2FYSE9ldnYzZEE1cWtUUlBMZ1JpYkJGQmFCRE9PWFlhbHc4bzZUT0ZPWXR3NStkblFRNm9Qa3QwYlVyMElGb1FUeXJOMXVmUlkwTlNFdDJ0UEMrS2lNT2J0cSthUXYydC9oNWxPSm5uWUN4cklYay9GZ0c4a1VOTEdzNkdlQ3ZvYlhnRlVaUzluQ1JEQUpYeFBiWWRNbXdVeVB0NVl1VjFza25hdDhsUHd5K2RSeVRnb012SC9YQjRUQ1pFUjZZakZvb3NOTnJjYlNXMFhXZHFwSFdrRU04ZEtabHdMeHhyY1lnS2hwMVZpQ3VVTi9qSVBDQk83aEZUWTlOM1FXcUt3Z0ZteHhPMC81RWJSejVJK3lJeVNaRWZxcFBNL2FYRTNiTXBPaWFRQlpvSGMxRGNvcE9uc25sTmxDdmJLVWJENHlJSUIrWDJvcEM2VHBGTXNIcGl5QnJYV3J5YjBzTEFNT3NtdXhDalhTNGpma29YZ3hERjNYMWFYR3pWR0I4ZkZEdHU4V2FRUkRDVXNZc0I3ODBxeUdlZldXaHNnNHlkQ0pmczFkSHVDaTZTOWl1VXdzMXdna0t3azZma2VNWDFoZkJoaDJCVy9yL045ZDhLN3E4TE1JMGh1djVEMC9FRHMrR0hBNXozY1pjS1VKN3BCTFA2WS85RVdPZmVWMjB4cCsyN0k3b2E3UnJHalBaUitONjNNT3lsQ1hCZFhucmtnZXphSUVsNVJvVzlKSjB4SkRBY3l5R085c2QvdVdyQVk4VCIsICJjcmVhdGVfdGltZXN0YW1wIjogMTQ3NjEzOTg3OCwgImlkIjogImY5MTIzOGJjLTUwZDQtNDEwZC1hOTIzLWEwOGUyOTZjNGVhMyJ9.PIwnFP8Kx-u3cHA4CrHxISH0lCr_b0Neq1Om06IquZKjw9AwSKGN8AusKJXOBTAWny-61QOkQrUQ_qXIInkhIRNl6A1sqTRH4i6M_kfn3BiLS8f_xlGxVjJp_lGeQdgfapgNl2cESRc7YuJzeVEymhc3vu3zzfuuOXc539ywSVaO6nt4dhwNcRHyT-aaredoRfw7JDCf8m8SYvmQOb35kxVY3VjleUG0SaoMG-x4MwabABMZjX56CijiWtWbORw83aDAOqU78M8xmOqvFQycBZI5ZQcOlTdOosW2P0kro2vCHdvFDxsHS0HwMKkBtQqk_K2kGcme47a_nuaHiFX-WzQZBZWwi_TKlZKnCDAACxX1ZZ0aE4CTysalh4hdLDApS3tMicrRNXi1f-Nt9HUgvKfZPovN0-4o8wuUQ_2qp7hRoU-zgGeK9MGfQ5bHhnkJam-M0kgBkzgnabZoaeikdMmzur4Lbo_xYbqQMTRfgQK3mzQa73sAns0cCj3AOEXOG-Ny14In4_dQQM7F1zpI7UHGYrLU9PvvOTK49wU_MJKwhXmGwEGajJWlti3_qUzx24T7CbQD1lGy-y7QpiU3qus5pUFVQgWqPDGAB5N7dZEJ28pE_RjyUuYN9YidxRsglwxlzCc0pTNf01Td6s433sN34jGQ3q1u1BTv7f3235M="
            ));

    private static Context applicationContext = HVACApplication.getContext();

    private static HVACManager ourInstance = new HVACManager();

    private static RVIRemoteNode node;

    private final static ArrayList<String> localServiceIdentifiers =
            new ArrayList<>(Arrays.asList(
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.HAZARD.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.TEMP_LEFT.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.TEMP_RIGHT.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.SEAT_HEAT_LEFT.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.SEAT_HEAT_RIGHT.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.FAN_SPEED.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.AIRFLOW_DIRECTION.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.DEFROST_REAR.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.DEFROST_FRONT.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.DEFROST_MAX.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.AIR_CIRC.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.AC.value(),
                    HVAC_BUNDLE_IDENTIFIER + "/" + HVACServiceIdentifier.AUTO.value()//,
                    //HVACServiceIdentifier.SUBSCRIBE.value(),
                    //HVACServiceIdentifier.UNSUBSCRIBE.value()
            ));

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
                if(mListener != null) mListener.onNodeConnected();
                HVACManager.subscribeToHvacRvi();
            }

            @Override
            public void nodeDidFailToConnect(RVIRemoteNode node, Throwable reason) {
                Log.d(TAG, "RVI node failed to connect: " + ((reason == null) ? "(null)" : reason.getLocalizedMessage()));
                if(mListener != null) mListener.onNodeDisconnected();
            }

            @Override
            public void nodeDidDisconnect(RVIRemoteNode node, Throwable reason) {
                Log.d(TAG, "RVI node did disconnect: " + ((reason == null) ? "(null)" : reason.getLocalizedMessage()));
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

    static String getServerUrl() {
        return getStringFromPrefs(applicationContext.getResources().getString(R.string.server_url_prefs_string), "");
    }

    static void setServerUrl(String serverUrl) {
        putStringInPrefs(applicationContext.getString(R.string.server_url_prefs_string), serverUrl);
    }

    static Integer getServerPort() {
        return getIntFromPrefs(applicationContext.getResources().getString(R.string.server_port_prefs_string), 0);
    }

    static void setServerPort(Integer serverPort) {
        putIntInPrefs(applicationContext.getString(R.string.server_port_prefs_string), serverPort);
    }

    static String getProxyServerUrl() {
        return getStringFromPrefs(applicationContext.getResources()
                                                    .getString(R.string.proxy_server_url_prefs_string), "");
    }

    static void setProxyServerUrl(String proxyUrl) {
        putStringInPrefs(applicationContext.getString(R.string.proxy_server_url_prefs_string), proxyUrl);
    }

    static Integer getProxyServerPort() {
        return getIntFromPrefs(applicationContext.getResources().getString(R.string.proxy_server_port_prefs_string), 0);
    }

    static void setProxyServerPort(Integer proxyPort) {
        putIntInPrefs(applicationContext.getString(R.string.proxy_server_port_prefs_string), proxyPort);
    }

    static boolean getUsingProxyServer() {
        return getBoolFromPrefs(applicationContext.getResources()
                                                  .getString(R.string.using_proxy_server_prefs_string), false);
    }

    static void setUsingProxyServer(boolean usingProxyServer) {
        putBoolInPrefs(applicationContext.getString(R.string.using_proxy_server_prefs_string), usingProxyServer);
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
                for (String credentials : newCredentials)
                    SIGNED_CREDENTIALS.add(credentials);

            RVILocalNode.setCredentials(HVACApplication.getContext(), SIGNED_CREDENTIALS);

            pkiComplete = true;

            if (readyToConnect)
                start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean isRviConfigured() {
        if (getServerUrl()  == null || getServerUrl().isEmpty()) return false;
        if (getServerPort() == 0)                                return false;

        if (getUsingProxyServer()) {
            if (getProxyServerUrl()  == null || getProxyServerUrl().isEmpty()) return false;
            if (getProxyServerPort() == 0)                                     return false;
        }

        return true;
    }


    static void start() {
        readyToConnect = true;

        if (!pkiComplete) return;

        if (getUsingProxyServer()) {
            node.setServerUrl(getProxyServerUrl());
            node.setServerPort(getProxyServerPort());

        } else {
            node.setServerUrl(getServerUrl());
            node.setServerPort(getServerPort());
        }

        RVILocalNode.addLocalServices(HVACApplication.getContext(), localServiceIdentifiers);

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

        node.invokeService(HVAC_BUNDLE_IDENTIFIER + "/" + serviceIdentifier, invokeParams, 360000);
    }

    public static HVACManagerListener getListener() {
        return ourInstance.mListener;
    }

    static void setListener(HVACManagerListener listener) {
        ourInstance.mListener = listener;
    }
}
