package com.example.demoexamsmartphone.FragmentsDevices;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.demoexamsmartphone.Classes.Device;
import com.example.demoexamsmartphone.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import me.tankery.lib.circularseekbar.CircularSeekBar;


public class ThermostatDevice extends Fragment {
    String token;
    String uuid;
    Device device;
    public ThermostatDevice(String token,String uuid,Device device){
        super(R.layout.fragment_thermostat_device);
        this.device = device;
        this.token = token;
        this.uuid = uuid;

    }

    SeekBar seekBarFan;
    TextView textViewTemperature;
    ImageButton imageButtonHeating;
    ImageButton imageButtonCool;
    int temperatureMnozhitel = 1;

    CircularSeekBar seekBarTemperature;
    SwitchCompat switchState;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //new ApiRequestGetDeviceData().execute();
        textViewTemperature = view.findViewById(R.id.textViewTemperature);

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
                new ApiRequestSendDeviceData().execute();
            }
        });

        seekBarTemperature = view.findViewById(R.id.seekBarTemperature);
        seekBarTemperature.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(@Nullable CircularSeekBar circularSeekBar, float v, boolean b) {

                textViewTemperature.setText(String.valueOf((int)(circularSeekBar.getProgress()*temperatureMnozhitel)));
            }

            @Override
            public void onStopTrackingTouch(@Nullable CircularSeekBar circularSeekBar) {
                device.setTemperature(String.valueOf((int)(circularSeekBar.getProgress()*temperatureMnozhitel)));
                new ApiRequestSendDeviceData().execute();
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

        seekBarTemperature.setProgress(Float.parseFloat(device.getTemperature()));
        textViewTemperature.setText(device.getTemperature());
        seekBarFan.setProgress(Integer.parseInt(device.getSpeed_fan()));
        switchState.setChecked(device.isWorkState());
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