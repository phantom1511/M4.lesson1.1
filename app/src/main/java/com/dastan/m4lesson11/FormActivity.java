package com.dastan.m4lesson11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dastan.m4lesson11.ui.home.HomeFragment;

import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {

    private Button saveBtn;
    private EditText edit1;
    private EditText edit2;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        edit1 = findViewById(R.id.editText1);
        edit2 = findViewById(R.id.editText2);
        saveBtn = findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormActivity.this, MainActivity.class);
                intent.putExtra("info1", edit1.getText().toString());
                intent.putExtra("info2", edit2.getText().toString());
                startActivity(intent);
                //Log.d("ron", edit1.getText().toString() + edit2.getText().toString());
            }
        });
    }
}
