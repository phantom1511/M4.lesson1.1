package com.dastan.m4lesson11;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.dastan.m4lesson11.onBoard.OnBoardActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.dastan.m4lesson11.ui.home.HomeFragment.setNotSorted;
import static com.dastan.m4lesson11.ui.home.HomeFragment.setSorted;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static List<Task> list;
    private static TaskAdapter adapter;
    private Task task;
    private boolean sort;
    private TextView nameText, emailText;
    private String name, email;
    private ImageView pImage1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean isShown = preferences.getBoolean("isShown", false);
        name = preferences.getString("getName", "Dastan Tulokulov");
        email = preferences.getString("getEmail", "dastan.tulokulov@gmail.com");

        if (!isShown) {
            startActivity(new Intent(this, OnBoardActivity.class));
            finish();
            return;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, PhoneActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

        String editText1 = getIntent().getStringExtra("info1");
        String editText2 = getIntent().getStringExtra("info2");
        Log.d("ron", editText1 + " " + editText2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivityForResult(intent, 100);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
        View header = navigationView.getHeaderView(0);


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ProfileActivity.class), 101);
            }
        });
        nameText = header.findViewById(R.id.textName);
        emailText = header.findViewById(R.id.textEmail);
        pImage1 = header.findViewById(R.id.pImage);


        nameText.setText(name);
        emailText.setText(email);
        setImageProf();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {
            SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
            preferences.edit().putBoolean("isShown", false).apply();
            finish();
        } else if (item.getItemId() == R.id.action_sort) {
            if (sort == false) {
                setSorted();
                sort = true;
                Log.e("ron", "sorted");
            } else {
                setNotSorted();
                sort = false;
                Log.e("ron", "not sorted");
            }
        }
        if (item.getItemId() == R.id.action_sign_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you want to sign out?");
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                }
            });
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "It been Canceled", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 101 && data != null) {
            name = data.getStringExtra("getName");
            email = data.getStringExtra("getEmail");

            Log.e("TAG", "onActivityResult: " + name);
            Log.e("TAG", "onActivityResult: " + email);
            nameText.setText(name);
            emailText.setText(email);
            setImageProf();
            if (name != null && email != null) {
            }
        }
    }

    private void setImageProf(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getUid();
        storageReference.child("images/*" + userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MainActivity.this).load(uri).circleCrop().into(pImage1);
            }
        });
    }


    //    public static void sortMethod() {
//        Collections.sort(list, new Comparator<Task>() {
//            @Override
//            public int compare(Task o1, Task o2) {
//                return o1.getTitle().compareTo(o2.getTitle());
//            }
//        });
//        adapter.notifyDataSetChanged();
//    }
}
