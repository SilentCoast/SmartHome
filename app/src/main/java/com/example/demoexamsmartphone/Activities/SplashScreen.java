package com.example.demoexamsmartphone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

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

    //deprecated
//    private class POSTRequestRegisterApp extends AsyncTask<String, String, String> {
//        Context context;
//        public POSTRequestRegisterApp(Context context){
//            this.context = context;
//        }
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        protected String doInBackground(String... params) {
//
//            try {
//                JSONObject postdata = new JSONObject();
//                // postdata.put("uuid",sharedPreferences.getString("UUID",""));
//                postdata.put("appId",getApplicationInfo().packageName);
//                postdata.put("competitor","Competitor-2");
//
//                URL url = new URL(getResources().getString(R.string.baseURL)+"app");
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Content-Type","application/json");
//
//
//                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
//                writer.write(postdata.toString());
//                writer.flush();
//
//
//                Log.i("API","regApp: "+connection.getResponseCode()+ " " + connection.getResponseMessage());
//
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//
//                while ((line = reader.readLine()) != null) {
//                    stringBuilder.append(line).append("\n");
//
//                }
//                reader.close();
//                connection.disconnect();
//                return stringBuilder.toString();
//
//
//            }
//            catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Log.i("API", "RegApp: "+ result);
//            new POSTRequestRegisterMobile(context).execute();
//
//        }
//    }
//    private class POSTRequestRegisterMobile extends AsyncTask<String, String, String> {
//        AlertDialog.Builder dialog;
//        Context context;
//        public POSTRequestRegisterMobile(Context context){
//            this.context = context;
//            dialog = new AlertDialog.Builder(context);
//        }
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        protected String doInBackground(String... params) {
//
//            try {
//                JSONObject postdata = new JSONObject();
//                postdata.put("Uuid",sharedPreferences.getString("UUID","none"));
//                postdata.put("AppId",getApplicationInfo().packageName);
//                postdata.put("DeviceName", Build.BRAND.toUpperCase()+" "+Build.MODEL.toUpperCase());
//                Log.i("API", "regData: "+ postdata.toString());
//                URL url = new URL(getResources().getString(R.string.baseURL)+"/Mobile/RegisterMobile");
//                Log.i("API","finalURL: "+url.toString());
//                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Content-Type","application/json");
//                if(connection.getResponseCode()!=201){
//                    Log.i("API","error:"+connection.getResponseCode()+ " " + connection.getResponseMessage());
//
//                }
//                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
//                writer.write(postdata.toString());
//                writer.flush();
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//
//                while ((line = reader.readLine()) != null) {
//                    stringBuilder.append(line).append("\n");
//
//                }
//                reader.close();
//                connection.disconnect();
//                return stringBuilder.toString();
//
//            }
//            catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            try {
//                String keyDevice;
//                if(result == null) {
//                    dialog.setTitle("Error");
//                    dialog.setMessage("Server is unavailable, try again later");
//                    dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            //close app
//                            dialogInterface.cancel();
//                            System.exit(0);
//                        }
//                    });
//                    dialog.show();
//                    return;
//                }
//                Log.i("API", "result: "+ result);
//                JSONObject jsonObject = new JSONObject(result);
//                keyDevice = jsonObject.getString("keyDevice");
//                Log.i("API", "keyDevice: "+ keyDevice);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }




}