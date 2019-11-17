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
            editTitle.setText(task.getTitle());
            editDesc.setText(task.getDesc(), TextView.BufferType.EDITABLE);
        }
    }

    public void onSave(View view) {
        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();
        //Intent intent = new Intent();
        if (task != null){
            task.setTitle(title);
            task.setDesc(desc);
            App.getDatabase().taskDao().update(task);
        }else {
            task = new Task(title, desc);
            App.getDatabase().taskDao().insert(task);
        }
        //intent.putExtra("task", task);
        //intent.putExtra("desc", task);
        Log.e("save2", task.getTitle() + task.getDesc());
        //setResult(RESULT_OK, intent);
        finish();
    }
}
