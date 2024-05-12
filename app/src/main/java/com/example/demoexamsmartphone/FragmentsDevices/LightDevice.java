package com.example.demoexamsmartphone.FragmentsDevices;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;


public class LightDevice extends Fragment {
    String token;
    Device device;
    String baseURL;
   public LightDevice(String token, Device device){
       super(R.layout.fragment_light_device);
       this.device = device;
       this.token = token;
   }

   ConstraintLayout layToTouch;
   TextView textViewBrightness;

SeekBar seekBarLight;
    StringRequest SendDeviceDataRequest;

   View[] scales;
   public void SetValues(){
       seekBarLight.setProgress(device.getLight_lm());
       textViewBrightness.setText(device.getLight_lm().toString());
   }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseURL = getResources().getString(R.string.baseURL);
        layToTouch = view.findViewById(R.id.layToTouch);
//        layToTouch.setOnTouchListener(handleTouch);

        textViewBrightness = view.findViewById(R.id.textViewBrightless);




        seekBarLight = view.findViewById(R.id.seekBarLight);
        seekBarLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                device.setLight_lm(seekBar.getProgress());
                textViewBrightness.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO send data
                MySingleton.getInstance(getActivity()).addToRequestQueue(SendDeviceDataRequest);
            }
        });


        SetValues();
        SendDeviceDataRequest = new StringRequest(
                Request.Method.PATCH,
                baseURL + "/Devices/UpdateDevice",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("API", "SendLEDInfo: "+response);
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
                headers.put("LightBrightness",device.getLight_lm().toString());
                headers.put("Id",String.valueOf(device.getId()));
                return  headers;
            }
        };
    }
}