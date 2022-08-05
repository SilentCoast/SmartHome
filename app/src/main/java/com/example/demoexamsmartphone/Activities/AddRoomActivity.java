package com.example.demoexamsmartphone.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.demoexamsmartphone.Classes.AddRoomRecyclerAdapter;
import com.example.demoexamsmartphone.Classes.Room;
import com.example.demoexamsmartphone.Classes.RoomInAdd;
import com.example.demoexamsmartphone.FragmentsDevices.LightDevice;
import com.example.demoexamsmartphone.FragmentsDevices.ThermostatDevice;
import com.example.demoexamsmartphone.R;

import java.util.ArrayList;

public class AddRoomActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AddRoomRecyclerAdapter adapter;
    ArrayList<RoomInAdd> rooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

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

        adapter = new AddRoomRecyclerAdapter(getApplicationContext(), rooms);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new AddRoomRecyclerAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                setImages(position);
            }
        });

    }

    public void setImages(int position){
        for (int i = 0; i < rooms.size(); i++) {

            if(i==position){

            }
            else {
                switch (rooms.get(i).getType()){
                    case "Kitchen":
                        rooms.get(i).setImage(getResources().getDrawable(R.drawable.light_device_on));
                        break;
                    case "Bathroom":
                        rooms.get(i).setImage(getResources().getDrawable(R.drawable.thermostat_device_on));
                        break;
                }
                rooms.get(i).setTextColor(getResources().getColor(R.color.dark_red));
            }
        }
        //adapter.notifyDataSetChanged();

    }

}