package com.example.demoexamsmartphone.FragmentsHomePage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.demoexamsmartphone.R;


public class StaticsFragment extends Fragment {
    public StaticsFragment(){
        super(R.layout.fragment_statics);
    }

    TextView textViewTestAPI;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewTestAPI = view.findViewById(R.id.textViewTestAPI);
    }

}