package com.example.demoexamsmartphone.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.demoexamsmartphone.Classes.Device;
import com.example.demoexamsmartphone.Classes.devicesRecyclerAdapter;
import com.example.demoexamsmartphone.FragmentsDevices.LightDevice;
import com.example.demoexamsmartphone.FragmentsDevices.ThermostatDevice;
import com.example.demoexamsmartphone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class InsideRoom extends AppCompatActivity {


    ImageButton btnGoBack;
    TextView textViewTitle;
    String token;
    String uuid;
    int idRoom;
    ImageButton btnAddDevice;
    RecyclerView recyclerViewDevices;
    devicesRecyclerAdapter adapter;
    ArrayList<Device> devices = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_room);




        textViewTitle = findViewById(R.id.textViewTitleInsideRoom);
        textViewTitle.setText(getIntent().getStringExtra("name")+" ("+getIntent().getStringExtra("type")+")");
        token = getIntent().getStringExtra("token");
        uuid = getIntent().getStringExtra("uuid");
        idRoom = getIntent().getIntExtra("id", 0);

        recyclerViewDevices = findViewById(R.id.recyclerViewDevices);
        recyclerViewDevices.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapter = new devicesRecyclerAdapter(this,devices);
        recyclerViewDevices.setAdapter(adapter);


        adapter.setItemClickListener(new devicesRecyclerAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(),R.anim.click));

                setImagesAndChangeFragment(position);


            }
        });


        btnAddDevice = findViewById(R.id.btnAddDevice);
        btnAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ApiRequestAddDevice(view.getContext()).execute();
            }
        });




        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       //recyclerViewDevices.findViewHolderForAdapterPosition(0).itemView.performClick();
        new ApiRequestGetDevices(this).execute();
    }

    private class ApiRequestGetDevices extends AsyncTask<String,String,String>{

        private final ProgressDialog progressDialog;

        public ApiRequestGetDevices(Context context){
            progressDialog = new ProgressDialog(context);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)  {

            HttpURLConnection connection = null;
            try {
                //open URL conection and set properties
                URL url = new URL(getResources().getString(R.string.baseURL)+"/devices");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("room",String.valueOf(idRoom));
                connection.setRequestProperty("token",token);
                connection.setRequestProperty("uuid",uuid);

                Log.i("API","getdevices:"+connection.getResponseCode()+ " " + connection.getResponseMessage());

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line).append("\n");
                }
                return stringBuilder.toString();

            } catch (IOException e) {
                Log.i("API", "doInBackground: "+e.getMessage());
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
            Log.i("API", "devices: "+s);
            devices.clear();
            try {
                JSONArray jsonArrayDevices = new JSONObject(s).getJSONArray("items");
                for (int i = 0; i < jsonArrayDevices.length(); i++) {
                    JSONObject jsonObject = jsonArrayDevices.getJSONObject(i);
                    Device device = new Device();
                    if(jsonObject.getString("type").equals("LED")){
                        device.setType(jsonObject.getString("type"));
                        device.setLight_lm(jsonObject.getString("light_lm"));
                    }
                    else{
                        device.setType("Thermostat");
                        device.setTemperature(jsonObject.getString("temperature"));
                        device.setSpeed_fan(jsonObject.getString("speed_fan"));
                    }
                    try{
                        device.setWorkState(jsonObject.getBoolean("off"));
                    }catch (JSONException ignored){
                    }
                    device.setId(jsonObject.getInt("id"));
                    devices.add(device);
                    setImagesAndChangeFragment(0);

                }
                    adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private class ApiRequestAddDevice extends AsyncTask<String,String,String>{
        ProgressDialog progressDialog;
        Context context;
        public ApiRequestAddDevice(Context context){
            progressDialog = new ProgressDialog(context);
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("please wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpsURLConnection connection = null;
            try {
                URL url = new URL(getResources().getString(R.string.baseURL)+"/devices");
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("type","LED");
                connection.setRequestProperty("room",String.valueOf(idRoom));
                connection.setRequestProperty("token",token);
                connection.setRequestProperty("uuid",uuid);

                Log.i("API", "addDevice: "+connection.getResponseCode()+" "+connection.getResponseMessage());

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine())!=null){
                    stringBuilder.append(line).append("\n");
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("API", "addDeviceResult: "+s);
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            new ApiRequestGetDevices(context).execute();
        }
    }

    public void setImagesAndChangeFragment(int position){
        for (int i = 0; i < devices.size(); i++) {

            if(i==position){
                switch (devices.get(i).getType()){
                    case "LED":
                        devices.get(i).setImage(getResources().getDrawable(R.drawable.light_device_on));
                        break;
                    case "Thermostat":
                        devices.get(i).setImage(getResources().getDrawable(R.drawable.thermostat_device_on));
                        break;
                }
                devices.get(i).setTextColor(getResources().getColor(R.color.dark_red));
            }
            else {
                switch (devices.get(i).getType()){
                    case "LED":
                        devices.get(i).setImage(getResources().getDrawable(R.drawable.light_device_off));
                        break;
                    case "Thermostat":
                        devices.get(i).setImage(getResources().getDrawable(R.drawable.thermostat_device_off));
                        break;
                }
                devices.get(i).setTextColor(getResources().getColor(R.color.grey));
            }
        }
        adapter.notifyDataSetChanged();
        switch (devices.get(position).getType()){
            case "LED":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayoutInsideRoom,new LightDevice())
                        .commit();
                break;
            case "Thermostat":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayoutInsideRoom,new ThermostatDevice(token,uuid,devices.get(position)))
                        .commit();
        }
    }
}