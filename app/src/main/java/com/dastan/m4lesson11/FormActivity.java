package com.dastan.m4lesson11;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.dastan.m4lesson11.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void loadData() {
        String taskId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("tasks")
                .document(taskId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String title = task.getResult().getString("title");
                            String desc = task.getResult().getString("description");
                            editTitle.setText(title);
                            editDesc.setText(desc);
                        }
                    }
                });
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
        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", desc);
        String taskId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .add(task)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(FormActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FormActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //intent.putExtra("task", task);
        //intent.putExtra("desc", task);
        //setResult(RESULT_OK, intent);
        //Log.e("save2", task.getTitle() + task.getDesc());
        finish();
    }
}
