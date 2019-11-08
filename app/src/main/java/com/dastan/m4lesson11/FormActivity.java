package com.dastan.m4lesson11;

import androidx.annotation.Nullable;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FormActivity extends AppCompatActivity {

    //private Button saveBtn;
    private EditText editTitle;
    private EditText editDesc;
    private List<Task> list;
    private TaskAdapter adapter;
    private Task task;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);


        editTitle = findViewById(R.id.editText1);
        editDesc = findViewById(R.id.editText2);
        task = (Task) getIntent().getSerializableExtra("homeFragment");

        if (task != null) {
            editTitle.setText(task.getTitle(), TextView.BufferType.EDITABLE);
            editDesc.setText(task.getDesc(), TextView.BufferType.EDITABLE);
        }

//        saveBtn = findViewById(R.id.btnSave);
//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FormActivity.this, MainActivity.class);
//                intent.putExtra("info1", edit1.getText().toString());
//                intent.putExtra("info2", edit2.getText().toString());
//                startActivity(intent);
//                //Log.d("ron", edit1.getText().toString() + edit2.getText().toString());
//            }
//        });
    }

    public void onSave(View view) {
        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();
        Intent intent = new Intent();
        Task task = new Task(title, desc);
        intent.putExtra("task", task);
        intent.putExtra("desc", task);
        Log.e("save2", task.getTitle() + task.getDesc());
        setResult(RESULT_OK, intent);
        finish();
    }
}
