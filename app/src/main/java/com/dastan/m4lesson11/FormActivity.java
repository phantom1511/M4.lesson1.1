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

        edit1 = (EditText) findViewById(R.id.editText1);
        edit2 = (EditText) findViewById(R.id.editText2);
    }

    public void saveInfo(View view) {
        saveBtn = (Button) findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
                String info = edit1.getText().toString() + edit2.getText().toString();
//                bundle.putString("info", info1 + info2);
//                HomeFragment homeFragment = new HomeFragment();
//                homeFragment.setArguments(bundle);
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.text_home, homeFragment);
//                transaction.commit();
                Intent intent = new Intent(FormActivity.this, MainActivity.class);
                intent.putExtra("info", info);
                startActivity(intent);
                Log.d("ron", edit1.getText().toString() + edit2.getText().toString());
            }
        });
    }
}
