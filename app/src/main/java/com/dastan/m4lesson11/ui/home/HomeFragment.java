package com.dastan.m4lesson11.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.dastan.m4lesson11.FormActivity;
import com.dastan.m4lesson11.MainActivity;
import com.dastan.m4lesson11.R;
import com.dastan.m4lesson11.Task;
import com.dastan.m4lesson11.TaskAdapter;
import com.dastan.m4lesson11.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> list;
    private Task task;
    private int positionRV;


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
                Toast.makeText(getContext(), "pos = " + position, Toast.LENGTH_SHORT).show();
                positionRV = position;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            task = (Task) data.getSerializableExtra("task");
            Log.e("ron", "title: " + task.getTitle());
            Log.e("ron", "desc: " + task.getDesc());
            list.add(task);
            adapter.notifyDataSetChanged();
        }else if (resultCode == Activity.RESULT_OK && requestCode == 200){
            task = (Task) data.getSerializableExtra("task");
            list.set(positionRV, task);
            adapter.notifyDataSetChanged();
        }
    }
}