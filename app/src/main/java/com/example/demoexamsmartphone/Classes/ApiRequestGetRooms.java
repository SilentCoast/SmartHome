package com.example.demoexamsmartphone.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import javax.net.ssl.HttpsURLConnection;

public class ApiRequestGetRooms extends AsyncTask<String, String, String> {
    private final ProgressDialog progressDialog;
    Context context;

    public ApiRequestGetRooms(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Please wait");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpsURLConnection connection = null;
        try {
            //open URL conection and set properties
            URL url = new URL("http://192.168.1.7:81/Mobile/GetMobile");
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("token",token);
            //connection.setRequestProperty("uuid",uuid);

            Log.i("API", "getRoom:" + connection.getResponseCode() + " " + connection.getResponseMessage());

            //set input stream (get response)
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            Log.i("API", "doInBackground: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i("API", "rooms: " + result);
        //rooms.clear();
//        try {
//            JSONArray jsonArray = new JSONObject(result).getJSONArray("items");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                Room room = new Room();
//                room.setType(jsonObject.getString("type"));
//                room.setName(jsonObject.getString("name"));
//                room.setId(jsonObject.getInt("id"));
//                //room.setImage(getImageFromTypeOfRoom(room.getType()));
//                //rooms.add(room);
//                //adapter.notifyDataSetChanged();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
