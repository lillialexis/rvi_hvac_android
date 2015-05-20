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

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class SettingsActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String vinString = HVACManager.getVin();
        String urlString = HVACManager.getProxyUrl();

        EditText vin = (EditText) findViewById(R.id.vin_edit_text);
        EditText url = (EditText) findViewById(R.id.proxy_server_url_edit_text);

        vin.setText(vinString);
        url.setText(urlString);
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

    public void settingsSubmitButtonClicked(View view) {
        // TODO: Validate input (correct URLs, etc.)

        EditText vin = (EditText) findViewById(R.id.vin_edit_text);
        EditText url = (EditText) findViewById(R.id.proxy_server_url_edit_text);

        HVACManager.setVin(vin.getText().toString().trim());
        HVACManager.setProxyUrl(url.getText().toString().trim());

        finish();
    }

    public void settingsCancelButtonClicked(View view) {
        finish();
    }

}
