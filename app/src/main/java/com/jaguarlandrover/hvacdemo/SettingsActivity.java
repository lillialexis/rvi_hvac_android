package com.jaguarlandrover.hvacdemo;

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

        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        String vinString = sharedPref.getString(getResources().getString(R.string.vehicle_vin_prefs_string), "");
        String urlString = sharedPref.getString(getResources().getString(R.string.server_url_prefs_string), "");

        EditText vin = (EditText) findViewById(R.id.vin_edit_text);
        EditText url = (EditText) findViewById(R.id.server_url_edit_text);

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
        EditText vin = (EditText) findViewById(R.id.vin_edit_text);
        EditText url = (EditText) findViewById(R.id.server_url_edit_text);

        String vinString = vin.getText().toString();
        String urlString = url.getText().toString();

        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.vehicle_vin_prefs_string), vinString);
        editor.putString(getString(R.string.server_url_prefs_string), urlString);
        editor.commit();

        finish();
    }

    public void settingsCancelButtonClicked(View view) {
        finish();
    }

}
