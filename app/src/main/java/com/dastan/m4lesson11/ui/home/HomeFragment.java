package com.dastan.m4lesson11.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dastan.m4lesson11.App;
import com.dastan.m4lesson11.FormActivity;
import com.dastan.m4lesson11.MainActivity;
import com.dastan.m4lesson11.R;
import com.dastan.m4lesson11.Task;
import com.dastan.m4lesson11.TaskAdapter;
import com.dastan.m4lesson11.interfaces.OnItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static List<Task> list;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private static TaskAdapter adapter;
    private Task task;
    private int positionRV;
    private static List<Task> sorted;
    private static List<Task> notSorted;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        initList();
        return root;
    }

    private void initList() {
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new TaskAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getContext(), FormActivity.class);
                intent.putExtra("homeFragment", list.get(position));
                startActivityForResult(intent, 200);
                //Toast.makeText(getContext(), "pos = " + position, Toast.LENGTH_SHORT).show();
                positionRV = position;
            }

            @Override
            public void onLongClick(final int position) {
                //Toast.makeText(getContext(), "long click pos = " + position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure you want to delete?");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getDatabase().taskDao().delete(list.get(position));
                        Map<String, Object> task = new HashMap<>();
                        String taskId = FirebaseAuth.getInstance().getUid();
                        FirebaseFirestore.getInstance()
                                .collection("tasks")
                                .document(taskId)
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getContext(), "Succeed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                });
                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"It been Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        App.getDatabase().taskDao().getAll().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                notSorted = tasks;
                list.clear();
                list.addAll(tasks);
                adapter.notifyDataSetChanged();
            }
        });

        App.getDatabase().taskDao().sortedMethod().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                sorted = tasks;
            }
        });
    }

    public static void setSorted(){
        list.clear();
        list.addAll(sorted);
        adapter.notifyDataSetChanged();
    }

    public static void setNotSorted(){
        list.clear();
        list.addAll(notSorted);
        adapter.notifyDataSetChanged();
    }


//    public static void sortMethod(){
//        Collections.sort(list, new Comparator<Task>() {
//            @Override
//            public int compare(Task o1, Task o2) {
//                return o1.getTitle().compareTo(o2.getTitle());
//            }
//        });
//        adapter.notifyDataSetChanged();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK && requestCode == 100){
//            task = (Task) data.getSerializableExtra("task");
//            Log.e("ron", "title: " + task.getTitle());
//            Log.e("ron", "desc: " + task.getDesc());
//            list.add(task);
//            adapter.notifyDataSetChanged();
//        }else if (resultCode == Activity.RESULT_OK && requestCode == 200){
//            task = (Task) data.getSerializableExtra("task");
//            list.set(positionRV, task);
//            adapter.notifyDataSetChanged();
//        }
//    }
}