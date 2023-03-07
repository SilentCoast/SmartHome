package com.example.demoexamsmartphone.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.demoexamsmartphone.Classes.AddRoomRecyclerAdapter;
import com.example.demoexamsmartphone.Classes.MySingleton;
import com.example.demoexamsmartphone.Classes.RoomInAdd;
import com.example.demoexamsmartphone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddRoomActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AddRoomRecyclerAdapter adapter;
    ArrayList<RoomInAdd> rooms;
    View currentViewSelected;
    Integer currentPositionSelected;
    TextView textViewRoomNameInAddRoom;
    AppCompatButton btnSaveRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        textViewRoomNameInAddRoom = findViewById(R.id.textViewRoomNameInAddRoom);

        String[] types = getResources().getStringArray(R.array.strarray);
        rooms = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            RoomInAdd room = new RoomInAdd();
            room.setType(types[i]);
            rooms.add(room);
        }
        setImages(0);
        recyclerView = findViewById(R.id.recyclerViewAddRoom);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        currentViewSelected = new View(this);
        adapter = new AddRoomRecyclerAdapter(getApplicationContext(), rooms);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new AddRoomRecyclerAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(currentViewSelected !=null){
                    currentViewSelected.setScaleX(1.0f);
                    currentViewSelected.setScaleY(1.0f);
                }
                currentViewSelected = view;
                currentPositionSelected = position;
                Log.i("click", "onClick: ");
                view.setScaleX(1.2f);
                view.setScaleY(1.2f);
                setImages(position);
            }
        });

        btnSaveRoom = findViewById(R.id.btnSaveRoom);
        btnSaveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("mPref",MODE_PRIVATE);

                StringRequest AddRoomRequest = new StringRequest(Request.Method.POST,
                        getResources().getString(R.string.baseURL) + "/Rooms/AddRoom",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("API", "Room added with Id: " + response.toString());

                                Intent intent = new Intent(AddRoomActivity.this,HomePage.class);
                                AddRoomActivity.this.startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("API", "error: " + error.toString());
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddRoomActivity.this);
                                builder.setTitle("Error")
                                        .setMessage(error.getMessage())
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                            }
                        })
                        {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap headers = new HashMap();
                                headers.put("token", sharedPreferences.getString("token","none"));
                                headers.put("Type", rooms.get(currentPositionSelected).getType());
                                headers.put("Name", textViewRoomNameInAddRoom.getText().toString());
                                return  headers;
                            }
                        };
                MySingleton.getInstance(AddRoomActivity.this).addToRequestQueue(AddRoomRequest);


            }
        });

    }



    public void setImages(int position){
        for (int i = 0; i < rooms.size(); i++) {

            if(i==position){

            }
            else {
                switch (rooms.get(i).getType().toLowerCase()){

                    case "kitchen":
                        rooms.get(i).setImage(getResources().getDrawable(R.drawable.icon_kitchen));
                        break;
                    case "living room":
                        rooms.get(i).setImage(getResources().getDrawable(R.drawable.icon_living_room));
                        break;
                    case "bathroom":
                        rooms.get(i).setImage(getResources().getDrawable(R.drawable.icon_bathroom));
                        break;
                }
                rooms.get(i).setTextColor(getResources().getColor(R.color.dark_red));
            }
        }
        //adapter.notifyDataSetChanged();

    }

}