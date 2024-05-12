package com.example.demoexamsmartphone.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.example.demoexamsmartphone.FragmentsHomePage.RoomsFragment;
import com.example.demoexamsmartphone.FragmentsHomePage.SettingFragment;
import com.example.demoexamsmartphone.FragmentsHomePage.StaticsFragment;
import com.example.demoexamsmartphone.R;

public class HomePage extends AppCompatActivity {

    AppCompatImageButton btnStatics;
    AppCompatImageButton btnHome;
    AppCompatImageButton btnSetting;
    FrameLayout frameLayout;
    String token;
    String uuid;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        frameLayout = findViewById(R.id.frameLayoutHomePage);

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedPrefName), Context.MODE_PRIVATE);
        uuid = sharedPreferences.getString("UUID","none");

        token = sharedPreferences.getString("token","none");
        RoomsFragment roomsFragment = new RoomsFragment(token,uuid);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(frameLayout.getId(),roomsFragment);
        transaction.commit();

        //logic for button Statics
        btnStatics = findViewById(R.id.btnStatics);
        btnStatics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticsFragment staticsFragment = new StaticsFragment();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(frameLayout.getId(),staticsFragment);
                fragmentTransaction.commit();

                btnHome.setBackgroundResource(R.drawable.home_icon_none);
                btnStatics.setBackgroundResource(R.drawable.icon_stats_pressed);
                btnSetting.setBackgroundResource(R.drawable.icon_setting_none);
            }
        });

        //logic for button Home
        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RoomsFragment roomsFragment = new RoomsFragment(token,uuid);

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(frameLayout.getId(),roomsFragment);
                transaction.commit();

                btnHome.setBackgroundResource(R.drawable.home_icon_pressed);
                btnStatics.setBackgroundResource(R.drawable.icon_stats_none);
                btnSetting.setBackgroundResource(R.drawable.icon_setting_none);
            }
        });

        //logic for button Setting
        btnSetting= findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SettingFragment settingFragment = new SettingFragment(uuid,token);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(frameLayout.getId(),settingFragment);
                transaction.commit();

                btnSetting.setBackgroundResource(R.drawable.icon_setting_pressed);
                btnHome.setBackgroundResource(R.drawable.home_icon_none);
                btnStatics.setBackgroundResource(R.drawable.icon_stats_none);
            }
        });
    }
}