package com.example.demoexamsmartphone.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoexamsmartphone.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Authorization extends AppCompatActivity {

    Button buttonSignIn;
    Button buttonNewResident;
    TextView textViewEmail;
    TextView textViewPassword;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);




        sharedPreferences = getSharedPreferences("mPref", Context.MODE_PRIVATE);



        Log.i("FFF", String.valueOf(sharedPreferences.getBoolean("isAuthorized",false)));

//        if(sharedPreferences.getBoolean("isAuthorized",false)){
//
//            Intent intent = new Intent(getApplicationContext(),HomePage.class);
//            startActivity(intent);
//            return;
//        }
        textViewEmail = findViewById(R.id.textViewEmailInAuthorization);
        textViewPassword = findViewById(R.id.textViewPasswordInAuthorization);

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //autorization and go on homepage
                new ApiRequestOptionsAuthorizateUser(v.getContext()).execute();

                }
        });

        buttonNewResident = findViewById(R.id.buttonNewResidentInAuthorization);
        buttonNewResident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private class ApiRequestOptionsAuthorizateUser extends AsyncTask<String,String,String> {

        int responseCode = 201;
        AlertDialog.Builder dialog;
        public ApiRequestOptionsAuthorizateUser(Context context){

            dialog = new AlertDialog.Builder(context);
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpsURLConnection connection = null;
            try {
                URL url = new URL("https://smarthome.madskill.ru/user");
                connection = (HttpsURLConnection) url.openConnection();

                //connection.setRequestProperty("X-HTTP-Method-Override","OPTIONS");

                connection.setRequestMethod("OPTIONS");
                connection.setRequestProperty("email",textViewEmail.getText().toString());
                connection.setRequestProperty("password",textViewPassword.getText().toString());
                connection.setRequestProperty("uuid",sharedPreferences.getString("UUID","none"));
//                connection.setDoInput(true);
//                connection.setDoOutput(true);
//                connection.setChunkedStreamingMode(0);
//                connection.connect();

                    if(connection.getResponseCode()!=201){
                        //DialogWindowWhenError
                        responseCode = connection.getResponseCode();
                        return null;
                    }
                Log.i("API", "authUser: "+connection.getResponseCode()+" "+connection.getResponseMessage());

                InputStream inputStream =connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine())!=null){
                    stringBuilder.append(line).append("\n");
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
            if(responseCode!=201) {
                dialog.setTitle("Error");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                switch (responseCode) {
                    case 403:
                        dialog.setMessage("User was not found");
                        break;
                    case 405:
                        dialog.setMessage("Device hasn't been registered, contact developer to solve this problem: smarthomesupport@mail.ru");
                        break;
                }
                dialog.show();
                return;
            }
            String token = null;
            try {
                JSONObject jsonObject = new JSONObject(s);
                token = jsonObject.getString("token");
            Log.i("API", "token: "+token);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isAuthorized",true);
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            intent.putExtra("token",token);
            startActivity(intent);
            Log.d("FFF",String.valueOf(sharedPreferences.getBoolean("isAuthorized",false)));

        }
    }







}