package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    SettingsActivity.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import android.nfc.tech.MifareUltralight;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


public class SettingsActivity extends ActionBarActivity
{
    EditText mVinEditText;

    TextView mUrlLabel;
    EditText mUrlEditText;
    TextView mPortLabel;
    EditText mPortEditText;

    TextView mProxyUrlLabel;
    EditText mProxyUrlEditText;
    TextView mProxyPortLabel;
    EditText mProxyPortEditText;

    Switch   mUsingProxySwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String vinString       = HVACManager.getVin();

        String urlString       = HVACManager.getServerUrl();
        String portString      = HVACManager.getServerPort().toString();

        String proxyUrlString  = HVACManager.getProxyServerUrl();
        String proxyPortString = HVACManager.getProxyServerPort().toString();

        boolean usingProxyServer = HVACManager.getUsingProxyServer();

        mVinEditText  = (EditText) findViewById(R.id.vin_edit_text);

        mUrlLabel     = (TextView) findViewById(R.id.server_url_label);
        mUrlEditText  = (EditText) findViewById(R.id.server_url_edit_text);
        mPortLabel    = (TextView) findViewById(R.id.server_port_label);
        mPortEditText = (EditText) findViewById(R.id.server_port_edit_text);

        mProxyUrlLabel     = (TextView) findViewById(R.id.proxy_server_url_label);
        mProxyUrlEditText  = (EditText) findViewById(R.id.proxy_server_url_edit_text);
        mProxyPortLabel    = (TextView) findViewById(R.id.proxy_server_port_label);
        mProxyPortEditText = (EditText) findViewById(R.id.proxy_server_port_edit_text);

        mUsingProxySwitch  = (Switch) findViewById(R.id.proxy_server_switch);

        mVinEditText.setText(vinString);
        mUrlEditText.setText(urlString);
        mPortEditText.setText(portString);

        mProxyUrlEditText.setText(proxyUrlString);
        mProxyPortEditText.setText(proxyPortString);

        mUsingProxySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            public void onCheckedChanged(CompoundButton usingProxySwitch, boolean isChecked) {
                updateUsingProxyServer(isChecked);
            }
        });

        mUsingProxySwitch.setChecked(usingProxyServer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateUsingProxyServer(boolean isChecked) {


        if (isChecked) {
            mUrlLabel.setEnabled(false);
            mUrlEditText.setEnabled(false);
            mPortLabel.setEnabled(false);
            mPortEditText.setEnabled(false);

            mProxyUrlEditText.setVisibility(View.VISIBLE);
            mProxyUrlLabel.setVisibility(View.VISIBLE);
            mProxyPortEditText.setVisibility(View.VISIBLE);
            mProxyPortLabel.setVisibility(View.VISIBLE);

        } else {
            mUrlLabel.setEnabled(true);
            mUrlEditText.setEnabled(true);
            mPortLabel.setEnabled(true);
            mPortEditText.setEnabled(true);

            mProxyUrlEditText.setVisibility(View.GONE);
            mProxyUrlLabel.setVisibility(View.GONE);
            mProxyPortEditText.setVisibility(View.GONE);
            mProxyPortLabel.setVisibility(View.GONE);
        }

        HVACManager.setUsingProxyServer(isChecked);
    }

    public void settingsSubmitButtonClicked(View view) {
        // TODO: Validate input (correct URLs, etc.)

        EditText vinEditText  = (EditText) findViewById(R.id.vin_edit_text);

        EditText urlEditText  = (EditText) findViewById(R.id.server_url_edit_text);
        EditText portEditText = (EditText) findViewById(R.id.server_port_edit_text);

        EditText proxyUrlEditText  = (EditText) findViewById(R.id.proxy_server_url_edit_text);
        EditText proxyPortEditText = (EditText) findViewById(R.id.proxy_server_port_edit_text);

        HVACManager.setVin(vinEditText.getText().toString().trim());

        HVACManager.setServerUrl(urlEditText.getText().toString().trim());
        HVACManager.setServerPort(Integer.parseInt((portEditText.getText().toString().trim())));

        HVACManager.setProxyServerUrl(proxyUrlEditText.getText().toString().trim());
        HVACManager.setProxyServerPort(Integer.parseInt(proxyPortEditText.getText().toString().trim()));

        finish();
    }

    public void settingsCancelButtonClicked(View view) {
        finish();
    }

}
