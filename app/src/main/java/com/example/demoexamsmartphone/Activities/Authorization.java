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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.demoexamsmartphone.Classes.MyErrorAlertDialog;
import com.example.demoexamsmartphone.Classes.MySingleton;
import com.example.demoexamsmartphone.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Authorization extends AppCompatActivity {

    Button buttonSignIn;
    Button buttonNewResident;
    TextView textViewEmail;
    TextView textViewPassword;
    SharedPreferences sharedPreferences;
    private void StartHomePageActivity(){
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        sharedPreferences = getSharedPreferences("mPref", Context.MODE_PRIVATE);

        if(sharedPreferences.getBoolean("isAuthorized",false)){
            StartHomePageActivity();
        }

        Log.i("FFF", String.valueOf(sharedPreferences.getBoolean("isAuthorized",false)));
        textViewEmail = findViewById(R.id.textViewEmailInAuthorization);
        textViewPassword = findViewById(R.id.textViewPasswordInAuthorization);

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //autorization and go on homepage

                StringRequest AuthorizeUserRequest = new StringRequest(
                        Request.Method.POST, getResources().getString(R.string.baseURL) + "/User/AuthorizeUser",
                        new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("API", "registerUserResponse: " + response.toString());

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Integer UserId = Integer.valueOf(response);
                        editor.putBoolean("isAuthorized",true);
                        editor.putString("token", UserId.toString())
                                .apply();
                        Toast.makeText(Authorization.this, "Authorized",
                                Toast.LENGTH_LONG).show();

                        StartHomePageActivity();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("API", "REGERROR: " + error.toString());
                        MyErrorAlertDialog.ShowAlertDialog(Authorization.this,error);
                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Login",textViewEmail.getText().toString());
                        headers.put("Password",textViewPassword.getText().toString());
                        return  headers;
                    }
                };
                MySingleton.getInstance(Authorization.this).addToRequestQueue(AuthorizeUserRequest);
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
                connection.setRequestMethod("OPTIONS");
                connection.setRequestProperty("email",textViewEmail.getText().toString());
                connection.setRequestProperty("password",textViewPassword.getText().toString());
                connection.setRequestProperty("uuid",sharedPreferences.getString("UUID","none"));

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
            catch ( NullPointerException exception){
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