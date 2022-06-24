package com.example.demoexamsmartphone.FragmentsKitchen;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.demoexamsmartphone.R;


public class LightInKitchen extends Fragment {

   public LightInKitchen(){
       super(R.layout.fragment_light_in_kitchen);
   }

   ConstraintLayout layToTouch;
   TextView textViewBrightless;
   View scale1;
   View scale2;
   View scale3;
    View scale4;
    View scale5;
    View scale6;
    View scale7;
    View scale8;
    View scale9;
    View scale10;
    View scale11;
    View scale12;
    View scale13;
    View scale14;
    View scale15;
    View scale16;
    int brightless;


   View[] scales;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layToTouch = view.findViewById(R.id.layToTouch);
        layToTouch.setOnTouchListener(handleTouch);

        textViewBrightless = view.findViewById(R.id.textViewBrightless);

        scale1 = view.findViewById(R.id.scale1);
        scale2 = view.findViewById(R.id.scale2);
        scale3 = view.findViewById(R.id.scale3);
        scale4 = view.findViewById(R.id.scale4);
        scale5 = view.findViewById(R.id.scale5);
        scale6 = view.findViewById(R.id.scale6);
        scale7 = view.findViewById(R.id.scale7);
        scale8 = view.findViewById(R.id.scale8);
        scale9 = view.findViewById(R.id.scale9);
        scale10 = view.findViewById(R.id.scale10);
        scale11 = view.findViewById(R.id.scale11);
        scale12 = view.findViewById(R.id.scale12);
        scale13 = view.findViewById(R.id.scale13);
        scale14 = view.findViewById(R.id.scale14);
        scale15 = view.findViewById(R.id.scale15);
        scale16 = view.findViewById(R.id.scale16);

        scales = new View[]{scale1,scale2,scale3,scale4,scale5,scale6,scale7,scale8,scale9,scale10,scale11,scale12,scale13,scale14,scale15,scale16};


    }
    private View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i("TAG", "touched down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("TAG", "moving: (" + x + ", " + y + ")");
                    //textViewBrightless.setText("(" + x + ", " + y + ")");
                    brightless =0;
                    for (int i = 0; i < scales.length; i++) {
                        if(scales[i].getY()+ scales[i].getHeight()<y){
                            scales[i].setBackgroundColor(getResources().getColor(R.color.white));}
                        else{
                            scales[i].setBackgroundColor(getResources().getColor(R.color.dark_red));}
                        ColorDrawable colorDrawable = (ColorDrawable) scales[i].getBackground();
                        if(colorDrawable.getColor() == getResources().getColor(R.color.dark_red)){
                            brightless+=6;
                            textViewBrightless.setText(String.valueOf(brightless));
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("TAG", "touched up");

                    break;
            }

            return true;
        }
    };
}