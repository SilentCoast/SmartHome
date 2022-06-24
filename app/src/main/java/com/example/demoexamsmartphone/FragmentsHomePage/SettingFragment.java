package com.example.demoexamsmartphone.FragmentsHomePage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.demoexamsmartphone.Activities.Authorization;
import com.example.demoexamsmartphone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class SettingFragment extends Fragment {

    public SettingFragment(String uuid,String token){
        super(R.layout.fragment_setting);
        this.token = token;
        this.uuid = uuid;
    }
//    public SettingFragment newInstance(String uuid,String token){
//        SettingFragment settingFragment = new SettingFragment();
//        settingFragment.token = token;
//        settingFragment.uuid = uuid;
//        return  settingFragment;
//    }
    String token;
    String uuid;
    SharedPreferences sharedPreferences;
    AppCompatButton btnSignOut;
    EditText textViewEmail;
    EditText textViewName;
    EditText textViewPhone;
    EditText textViewGender;
    EditText textViewDateOfBirth;
    AppCompatButton btnSave;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save changes
                new ApiRequestSendProfileInfo().execute();
            }
        });

        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.sharedPrefName), Context.MODE_PRIVATE);

        textViewEmail = view.findViewById(R.id.textViewEmailInProfile);
        textViewName = view.findViewById(R.id.textViewNameInProfile);
        textViewDateOfBirth = view.findViewById(R.id.textViewDateOfBirthInProfile);
        textViewGender = view.findViewById(R.id.textViewGenderInProfile);
        textViewPhone = view.findViewById(R.id.textViewPhoneInProfile);


        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isAuthorized",false);
                editor.apply();
                Intent intent = new Intent(view.getContext(), Authorization.class);
                startActivity(intent);
            }
        });
        new ApiRequestGetProfileInfo().execute();
    }
        private class ApiRequestGetProfileInfo extends AsyncTask<String,String,String> {

            @Override
            protected String doInBackground(String... strings) {
                HttpsURLConnection connection = null;

                try {
                    URL url = new URL(getResources().getString(R.string.baseURL)+"/profile?token="+token+"&uuid="+uuid);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("token",token);
                    connection.setRequestProperty("uuid",uuid);

                    Log.i("API", "getprofile: "+connection.getResponseCode()+" "+connection.getResponseMessage());

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine())!=null){
                        stringBuilder.append(line).append("\n");
                    }
                    return  stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("item");
                    JSONObject data = jsonArray.getJSONObject(0);
                    textViewEmail.setText(data.getString("email"));
                    textViewDateOfBirth.setText(data.getString("date"));
                    textViewName.setText(data.getString("name"));
                    textViewPhone.setText(data.getString("phone"));
                Log.i("API", "getProfile: "+data.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private class ApiRequestSendProfileInfo extends AsyncTask<String,String,String>{

            @Override
            protected String doInBackground(String... strings) {

                HttpsURLConnection connection = null;
                try {
                URL url = new URL(getResources().getString(R.string.baseURL)+"/profile");
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("token",token);
                    connection.setRequestProperty("uuid",uuid);
                    connection.setRequestProperty("email",textViewEmail.getText().toString());
                    connection.setRequestProperty("name",textViewName.getText().toString());
                    connection.setRequestProperty("username",textViewName.getText().toString());
                    connection.setRequestProperty("dateOf",textViewDateOfBirth.getText().toString());
                    connection.setRequestProperty("phone",textViewPhone.getText().toString());

                    Log.i("API", "setprofile: "+connection.getResponseCode()+" "+connection.getResponseMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }
}