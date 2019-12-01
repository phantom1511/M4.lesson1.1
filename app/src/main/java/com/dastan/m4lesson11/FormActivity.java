package com.dastan.m4lesson11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormActivity extends AppCompatActivity {

    //private Button saveBtn;
    private EditText editTitle;
    private EditText editDesc;
    private List<Task> list;
    private TaskAdapter adapter;
    private Task mTask;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);


        editTitle = findViewById(R.id.editText1);
        editDesc = findViewById(R.id.editText2);
        mTask = (Task) getIntent().getSerializableExtra("homeFragment");

        if (mTask != null) {
            editTitle.setText(mTask.getTitle());
            editDesc.setText(mTask.getDesc(), TextView.BufferType.EDITABLE);
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
        if (mTask != null){
            mTask.setTitle(title);
            mTask.setDesc(desc);
            updateTaskInFirestore();
//            Map<String, Object> task = new HashMap<>();
//            task.put("title", title);
//            task.put("description", desc);
//            String taskId = FirebaseAuth.getInstance().getUid();
//            FirebaseFirestore.getInstance()
//                    .collection("tasks")
//                    .document(taskId)
//                    .set(task)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toast.makeText(FormActivity.this ,"Succeed", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(FormActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
            ProgressBar formProgressBar = findViewById(R.id.progressBar);
            formProgressBar.setVisibility(View.VISIBLE);
        }else {
            mTask = new Task(title, desc);
            saveToFirestore();
//            Map<String, Object> mTask = new HashMap<>();
//            mTask.put("title", title);
//            mTask.put("description", desc);
//            String taskId = FirebaseAuth.getInstance().getUid();
//            FirebaseFirestore.getInstance()
//                    .collection("tasks")
//                    .add(mTask)
//                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> mTask) {
//                            if (mTask.isSuccessful()){
//                                Toast.makeText(FormActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(FormActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

            ProgressBar formProgressBar = findViewById(R.id.progressBar);
            formProgressBar.setVisibility(View.VISIBLE);
        }

        //intent.putExtra("mTask", mTask);
        //intent.putExtra("desc", mTask);
        //setResult(RESULT_OK, intent);
        //Log.e("save2", mTask.getTitle() + mTask.getDesc());
    }

    private void saveToFirestore(){
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .add(mTask)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            mTask.setId(task.getResult().getId());
                            App.getDatabase().taskDao().insert(mTask);
                            Toaster.show("Succeed");
                            finish();
                        } else {
                            Toaster.show("Error" + task.getException().getMessage());
                        }
                    }
                });
    }

    private void updateTaskInFirestore(){
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .document(mTask.getId())
                .set(mTask)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()){
                            App.getDatabase().taskDao().update(mTask);
                            Toaster.show("Succeed");
                            finish();
                        } else {
                            Toaster.show("Error of update" + task.getException().getMessage());
                        }
                    }
                });
    }
}
