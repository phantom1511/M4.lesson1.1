package com.dastan.m4lesson11.onBoard;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dastan.m4lesson11.MainActivity;
import com.dastan.m4lesson11.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardFragment extends Fragment {


    public BoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_board, container, false);
        //View viewSkip =  inflater.inflate(R.layout.activity_on_board, container, false);
        int pos = getArguments().getInt("pos");
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);
        Button button = view.findViewById(R.id.btnStart);
        //Button button1 = viewSkip.findViewById(R.id.skipBtn);

        switch (pos){
            case 0:
                textView.setText("Do you know who is this man?");
                textView.setTextColor(Color.parseColor("#ffffff"));
                imageView.setImageResource(R.drawable.cris1);
                button.setVisibility(View.INVISIBLE);
                view.setBackgroundColor(Color.parseColor("#15262E"));
                break;
            case 1:
                textView.setText("He is the best player in the world MF*");
                imageView.setImageResource(R.drawable.cris2);
                button.setVisibility(View.INVISIBLE);
                view.setBackgroundColor(Color.parseColor("#328D32"));
                break;
            case 2:
                textView.setText("Siii!!!");
                imageView.setImageResource(R.drawable.cris3);
                view.setBackgroundColor(Color.parseColor("#E6B534"));
                //button1.setVisibility(View.INVISIBLE);
                break;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
                SharedPreferences preferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
                preferences.edit().putBoolean("isShown", true).apply();
                Log.e("ron", "start");
            }
        });

        return view;
    }

}
