package com.example.demoexamsmartphone.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.demoexamsmartphone.Classes.MyErrorAlertDialog;
import com.example.demoexamsmartphone.Classes.MySingleton;
import com.example.demoexamsmartphone.R;

import java.util.HashMap;
import java.util.Map;
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

        //set uuid
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPrefName),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UUID", String.valueOf(UUID.randomUUID()).toUpperCase())
                .apply();
        Log.i("API", "UUID: "+sharedPreferences.getString("UUID",""));



        StringRequest regMobileRequest = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.baseURL) + "/Mobile/RegMobile", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("API", "REGDATAMobile: " + response.toString());
                Intent intent = new Intent(getApplicationContext(), Authorization.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("API", "REGERROR: " + error.toString());
                MyErrorAlertDialog.ShowAlertDialog(SplashScreen.this,error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Uuid",sharedPreferences.getString("UUID","none"));
                headers.put("AppId",getApplicationInfo().packageName);
                headers.put("DeviceName", Build.BRAND.toUpperCase()+" "+Build.MODEL.toUpperCase());
                return  headers;
            }
        };


        MySingleton.getInstance(this).addToRequestQueue(regMobileRequest);


    }
}