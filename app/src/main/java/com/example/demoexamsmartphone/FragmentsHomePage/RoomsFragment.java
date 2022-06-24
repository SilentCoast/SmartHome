package com.example.demoexamsmartphone.FragmentsHomePage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoexamsmartphone.Activities.InsideRoom;
import com.example.demoexamsmartphone.Classes.Room;
import com.example.demoexamsmartphone.R;
import com.example.demoexamsmartphone.Classes.roomsRecyclerAdapter;

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


public class RoomsFragment extends Fragment {

    String token;
    String uuid;
    ArrayList<Room> rooms;
    ImageButton imageButtonAddRoom;
    roomsRecyclerAdapter adapter;
    public RoomsFragment(String token,String uuid){
        super(R.layout.fragment_rooms);
        this.token = token;
        this.uuid = uuid;
        rooms = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        adapter = new roomsRecyclerAdapter(getContext(),rooms);
        recyclerView.setAdapter(adapter);
        //get rooms from server
        new ApiRequestGetRooms(view.getContext()).execute();

        //click on specific room
        adapter.setClicklistener(new roomsRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.click));

                Room currentRoom = adapter.getItem(position);
                Intent intent = new Intent(getContext(), InsideRoom.class);
                intent.putExtra("name",currentRoom.getName());
                intent.putExtra("type",currentRoom.getType());
                intent.putExtra("id",currentRoom.getId());
                intent.putExtra("token",token);
                intent.putExtra("uuid",uuid);
                startActivity(intent);

            }
        });

        imageButtonAddRoom = view.findViewById(R.id.imageButtonAddRoom);
        imageButtonAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ApiRequestAddRoom(view.getContext()).execute();
            }
        });

    }

    private class ApiRequestAddRoom extends AsyncTask<String,String,String>{
        private final ProgressDialog progressDialog;

        public ApiRequestAddRoom(Context context){
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
                URL url = new URL(getResources().getString(R.string.baseURL)+"/rooms");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("name","kitchenyyy");
                connection.setRequestProperty("type","kitchen");
                connection.setRequestProperty("token",token);
                connection.setRequestProperty("uuid",uuid);

                Log.i("API","addRoom:"+connection.getResponseCode()+ " " + connection.getResponseMessage());

                //set input stream (get response)
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while   ((line = bufferedReader.readLine())!=null){
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
            Log.i("API", "add: "+s);
            Room room = new Room();
            room.setImage(getResources().getDrawable(R.drawable.icon_kitchen));
            room.setName("Kitchenyyy");
            room.setType("Kitchen");
            room.setImage(getImageFromTypeOfRoom(room.getType()));
            rooms.add(room);
            adapter.notifyDataSetChanged();
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }


    private class ApiRequestGetRooms extends AsyncTask<String,String,String>{
        private final ProgressDialog progressDialog;

        public ApiRequestGetRooms(Context context){
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
                URL url = new URL(getResources().getString(R.string.baseURL)+"/rooms");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("token",token);
                connection.setRequestProperty("uuid",uuid);

                Log.i("API","getRoom:"+connection.getResponseCode()+ " " + connection.getResponseMessage());

                //set input stream (get response)
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while   ((line = bufferedReader.readLine())!=null){
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
            Log.i("API", "rooms: "+s);

            try {
                JSONArray jsonArray = new JSONObject(s).getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Room room = new Room();
                    room.setType(jsonObject.getString("type"));
                    room.setName(jsonObject.getString("name"));
                    room.setId(jsonObject.getInt("id"));
                    room.setImage(getImageFromTypeOfRoom(room.getType()));
                    rooms.add(room);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public Drawable getImageFromTypeOfRoom(String roomType){
        switch (roomType.toLowerCase()){
            case "kitchen":
                return (getResources().getDrawable(R.drawable.icon_kitchen));
            case "living room":
                return getResources().getDrawable(R.drawable.icon_living_room);
            case "bathroom":
                return getResources().getDrawable(R.drawable.icon_bathroom);
        }
        return null;
    }
}