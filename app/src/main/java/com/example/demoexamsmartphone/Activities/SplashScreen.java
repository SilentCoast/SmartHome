package com.example.demoexamsmartphone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.demoexamsmartphone.Activities.Authorization;
import com.example.demoexamsmartphone.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class SplashScreen extends AppCompatActivity {
ImageView imageView;
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        imageView = findViewById(R.id.imageView);

        //animation
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                imageView.setRotation(imageView.getRotation()+1);
            }
        },100,5);
        //get uuid
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPrefName),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UUID", String.valueOf(UUID.randomUUID()).toUpperCase()).apply();
        Log.i("API", "UUID: "+sharedPreferences.getString("UUID",""));

        //register app and open new Activity
        new POSTRequestRegisterApp().execute();

    }



    private class POSTRequestRegisterMobile extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            try {
                JSONObject postdata = new JSONObject();
                postdata.put("uuid",sharedPreferences.getString("UUID","none"));
                postdata.put("appId",getApplicationInfo().packageName);
                postdata.put("device", Build.BRAND.toUpperCase()+" "+Build.MODEL.toUpperCase());
                Log.i("API", "regData: "+ postdata.toString());
                URL url = new URL("https://smarthome.madskill.ru/mobile");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");

                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                writer.write(postdata.toString());
                writer.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");

                }
                reader.close();
                connection.disconnect();
                return stringBuilder.toString();

            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                String keyDevice;
                JSONObject jsonObject = new JSONObject(result);
                keyDevice = jsonObject.getString("keyDevice");
                Log.i("API", "keyDevice: "+ keyDevice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getApplicationContext(), Authorization.class);
            startActivity(intent);
        }
    }


    private class POSTRequestRegisterApp extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            try {
                JSONObject postdata = new JSONObject();
                // postdata.put("uuid",sharedPreferences.getString("UUID",""));
                postdata.put("appId",getApplicationInfo().packageName);
                postdata.put("competitor","Competitor-2");

                URL url = new URL("https://smarthome.madskill.ru/app");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");


                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                writer.write(postdata.toString());
                writer.flush();


                    Log.i("API","regApp: "+connection.getResponseCode()+ " " + connection.getResponseMessage());


                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");

                }
                reader.close();
                connection.disconnect();
                return stringBuilder.toString();


            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("API", "RegApp: "+ result);
            new POSTRequestRegisterMobile().execute();

        }
    }

}