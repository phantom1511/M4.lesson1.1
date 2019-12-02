package com.dastan.m4lesson11;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText editName, editEmail;
    private ImageView imageProf;
    private final int Pick_image = 1;
    private SharedPreferences sharedPref;
    private ImageView pImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        pImage = findViewById(R.id.pImage);
        imageProf = findViewById(R.id.imageProfile);
        imageProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, Pick_image);
            }
        });
        loadData();
    }

    private void loadData() {
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String name = task.getResult().getString("name");
                            String email = task.getResult().getString("email");
                            editName.setText(name);
                            editEmail.setText(email);
                        }
                    }
                });
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        storage.child("images/*" + userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileActivity.this).load(uri).into(imageProf);
            }
        });
    }

    private void loadData2() {
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null) {
                            String name = documentSnapshot.getString("name");
                            String email = documentSnapshot.getString("email");
                            editName.setText(name);
                            editEmail.setText(email);
                        }
                    }
                });
    }


    public void onNameSave(View view) {
        // Create a new user with a first and last name
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        sharedPref = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("getName", name);
        editor.putString("getEmail", email);
        editor.apply();
        Intent intent = new Intent();
        intent.putExtra("getName", name).putExtra("getEmail", email);
        setResult(RESULT_OK, intent);
        finish();
//        SharedPreferences sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
//        sharedPreferences.edit().putString("getName", name).apply();
//        sharedPreferences.edit().putString("getEmail", email).apply();
//        intent.putExtra("getName", name);
//        intent.putExtra("getEmail", email);
//        startActivity(intent);

//        sharedPreferences.edit().putString("sentName", name).apply();
//        sharedPreferences.edit().putString("sentEmail", email).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Pick_image && data != null) {
            Uri uri = data.getData();
            imageProf.setImageURI(uri);
            upload(uri);
        }
//        switch (requestCode) {
//            case Pick_image:
//                if (resultCode == RESULT_OK) {
//                    try {
//                        final Uri imageUri = data.getData();
//                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        imageProf.setImageBitmap(selectedImage);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//        }
    }

    private void upload(Uri uri) {
        final ProgressBar imgProgressBar = findViewById(R.id.imgProgressBar);
        imgProgressBar.setVisibility(View.VISIBLE);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();
        StorageReference reference = storage.getReference().child("images/*" + userId);
        UploadTask uploadTask = reference.putFile(uri);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toaster.show("Succeed");
                    imgProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}


