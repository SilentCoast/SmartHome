package com.example.demoexamsmartphone.Activities;

import static androidx.appcompat.app.AlertDialog.Builder;
import static androidx.appcompat.app.AlertDialog.OnClickListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.demoexamsmartphone.Classes.MySingleton;
import com.example.demoexamsmartphone.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Registration extends AppCompatActivity {
    Button buttonNewResident;
    Button buttonEnterYourHome;
    TextView textViewEmail;
    TextView textViewName;
    TextView textViewPassword;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder alertDialog;
    String baseUrl;
    boolean unsuccesRegistration = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        baseUrl = getResources().getString(R.string.baseURL);

        alertDialog = new Builder(this);
        alertDialog.setTitle("Warning!");
        alertDialog.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


        textViewEmail = findViewById(R.id.textViewEmailInRegistration);
        textViewName = findViewById(R.id.textViewNameInRegistration);
        textViewPassword = findViewById(R.id.textViewPasswordInRegistration);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPrefName), Context.MODE_PRIVATE);


        //registerNew Resident go back to authorization
        buttonNewResident = findViewById(R.id.buttonNewResidentInRegistration);
        buttonNewResident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("API", "reg click new resident");
                StringRequest RegisterUserRequest = new StringRequest(
                        Request.Method.POST, baseUrl + "/User/RegisterUser", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("API", "registerUserResponse: " + response.toString());

                            onBackPressed();
                            Toast.makeText(Registration.this, "User registered. Continue with authorization",
                                    Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("API", "REGERROR: " + error.toString());
                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Name",textViewName.getText().toString());
                        headers.put("Login",textViewEmail.getText().toString());
                        headers.put("Password",textViewPassword.getText().toString());
                        return  headers;
                    }
                };

                MySingleton.getInstance(Registration.this).addToRequestQueue(RegisterUserRequest);

            }
        });

        //go back to authorization
        buttonEnterYourHome = findViewById(R.id.buttonSignInRegistration);
        buttonEnterYourHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                // startActivity(intent);
            }
        });

    }
    private class PostRegisterUser extends AsyncTask<String,String,String>  {
        private final ProgressDialog progressDialog;

        public PostRegisterUser(Context context){
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

            HttpURLConnection urlConnection = null;
            try {
                //
                //open URL conection and set properties
                URL url = new URL("https://smarthome.madskill.ru/user");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("email",textViewEmail.getText().toString());
                urlConnection.setRequestProperty("name",textViewName.getText().toString());
                urlConnection.setRequestProperty("password",textViewPassword.getText().toString());
                urlConnection.setRequestProperty("uuid",sharedPreferences.getString("UUID",""));
                if(urlConnection.getResponseCode()!=201){
                    Log.i("API","error:"+urlConnection.getResponseCode()+ " " + urlConnection.getResponseMessage());
                }
                //set input stream (get response)
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while   ((line = bufferedReader.readLine())!=null){
                    stringBuilder.append(line).append("\n");
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                Log.i("API", "doInBackgroundRegisterUser: "+e.getMessage());
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

            unsuccesRegistration = jsonObject.getBoolean("secure");
                if(!unsuccesRegistration){
                    onBackPressed();
                }
                Log.i("API", "unsucessReg: "+unsuccesRegistration);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

}