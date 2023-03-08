package com.example.demoexamsmartphone.FragmentsDevices;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.demoexamsmartphone.Classes.Device;
import com.example.demoexamsmartphone.Classes.MyErrorAlertDialog;
import com.example.demoexamsmartphone.Classes.MySingleton;
import com.example.demoexamsmartphone.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import me.tankery.lib.circularseekbar.CircularSeekBar;


public class ThermostatDevice extends Fragment {
    String token;
    String uuid;
    Device device;
    String baseURL;
    public ThermostatDevice(String token,String uuid,Device device){
        super(R.layout.fragment_thermostat_device);
        this.device = device;
        this.token = token;
        this.uuid = uuid;

    }
    public ThermostatDevice(){
        super(R.layout.fragment_thermostat_device);

    }

    SeekBar seekBarFan;
    TextView textViewTemperature;
    ImageButton imageButtonHeating;
    ImageButton imageButtonCool;
    int temperatureMnozhitel = 1;

    CircularSeekBar seekBarTemperature;
    SwitchCompat switchState;
    StringRequest SendDeviceDataRequest;

public void SetDefaults(){
    Log.i("gg", "temperaure = "+device.getTemperature());
    if(!TextUtils.isEmpty(device.getTemperature())){
        seekBarTemperature.setProgress(Float.parseFloat(device.getTemperature()));
        textViewTemperature.setText(device.getTemperature());
    }
    if(!TextUtils.isEmpty(device.getSpeed_fan())){
        seekBarFan.setProgress(Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(device.getSpeed_fan())))));
    }

    switchState.setChecked(device.isWorkState());

}
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewTemperature = view.findViewById(R.id.textViewTemperature);

        baseURL = getResources().getString(R.string.baseURL);
        imageButtonHeating = view.findViewById(R.id.imageButtonHeating);
        imageButtonHeating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtonCool.setImageDrawable(getResources().getDrawable(R.drawable.cool_off));
                imageButtonHeating.setImageDrawable(getResources().getDrawable(R.drawable.heating_on));
                temperatureMnozhitel = 1;
                textViewTemperature.setText(String.valueOf((int) seekBarTemperature.getProgress()));
            }
        });
        imageButtonCool = view.findViewById(R.id.imageButtonCool);
        imageButtonCool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtonCool.setImageDrawable(getResources().getDrawable(R.drawable.cool_on));
                imageButtonHeating.setImageDrawable(getResources().getDrawable(R.drawable.heating_off));
                temperatureMnozhitel = -1;
                textViewTemperature.setText(String.valueOf((int) seekBarTemperature.getProgress()*temperatureMnozhitel));

            }
        });

        seekBarFan = view.findViewById(R.id.seekBarFan);
        seekBarFan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                device.setSpeed_fan(String.valueOf(seekBar.getProgress()));
                //TODO send device data
                MySingleton.getInstance(getActivity()).addToRequestQueue(SendDeviceDataRequest);
            }
        });

        seekBarTemperature = view.findViewById(R.id.seekBarTemperature);
        if(TextUtils.isEmpty(device.getTemperature())){
            seekBarTemperature.setProgress(Float.parseFloat(device.getTemperature()));
        }

        seekBarTemperature.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(@Nullable CircularSeekBar circularSeekBar, float v, boolean b) {

                textViewTemperature.setText(String.valueOf((int)(circularSeekBar.getProgress()*temperatureMnozhitel)));
            }

            @Override
            public void onStopTrackingTouch(@Nullable CircularSeekBar circularSeekBar) {
                device.setTemperature(String.valueOf((int)(circularSeekBar.getProgress()*temperatureMnozhitel)));
                //new ApiRequestSendDeviceData().execute();
                //TODO send device data
                MySingleton.getInstance(getActivity()).addToRequestQueue(SendDeviceDataRequest);
            }

            @Override
            public void onStartTrackingTouch(@Nullable CircularSeekBar circularSeekBar) {

            }
        });

        switchState = view.findViewById(R.id.switchState);
        switchState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                device.setWorkState(switchState.isChecked());
            }
        });
        SetDefaults();
        SendDeviceDataRequest = new StringRequest(
                Request.Method.PATCH,
                baseURL + "/Devices/UpdateDevice",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("API", "SendThermoInfo: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyErrorAlertDialog.ShowAlertDialog(getActivity(),error);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Type",device.getType());
                headers.put("FanSpeed",device.getSpeed_fan());
                headers.put("IsActive",String.valueOf( device.isWorkState()));
                headers.put("Temperature",device.getTemperature());
                headers.put("Id",String.valueOf(device.getId()));
                return  headers;
            }
        };
    }



    private class ApiRequestSendDeviceData extends AsyncTask<String,String,String>{
        String TAG = "API";
        String className = getClass().getSimpleName();
        @Override
        protected String doInBackground(String... strings) {

            HttpsURLConnection connection = null;
            try {
                URL url = new URL(getResources().getString(R.string.baseURL)+"/devices/Thermostat");
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("PATCH");
                connection.setRequestProperty("name",device.getType());
                connection.setRequestProperty("speed_fan",device.getSpeed_fan());
                connection.setRequestProperty("off",String.valueOf( device.isWorkState()));
                connection.setRequestProperty("temperature",device.getTemperature());
                connection.setRequestProperty("id",String.valueOf(device.getId()));
                connection.setRequestProperty("token",token);
                connection.setRequestProperty("uuid",uuid);

                Log.i(TAG, className+" "+ connection.getResponseCode()+" "+ connection.getResponseMessage());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }

            }


            return null;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG, className+" response "+ s);
        }
    }

//    private class ApiRequestGetDeviceData extends AsyncTask<String,String,String>{
//        String TAG = "API";
//        String className = getClass().getSimpleName();
//        @Override
//        protected String doInBackground(String... strings) {
//
//            HttpsURLConnection connection = null;
//            try {
//                URL url = new URL(getResources().getString(R.string.baseURL)+"/device/"+String.valueOf(device.getId()));
//                connection = (HttpsURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.setRequestProperty("id",String.valueOf(device.getId()));
//                connection.setRequestProperty("token",token);
//                connection.setRequestProperty("uuid",uuid);
//
//                Log.i(TAG, className+" "+ connection.getResponseCode()+" "+ connection.getResponseMessage());
//
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line;
//                StringBuilder stringBuilder = new StringBuilder();
//                while ((line = bufferedReader.readLine())!=null){
//                    stringBuilder.append(line);
//                }
//                return stringBuilder.toString();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//
//            }
//
//
//            return null;
//
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.i(TAG, className+" response "+ s);
//        }
//    }
}