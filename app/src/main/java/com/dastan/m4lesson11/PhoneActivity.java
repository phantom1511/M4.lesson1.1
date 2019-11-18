package com.dastan.m4lesson11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private EditText editPhone;
    private Button btnSendCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private EditText editSmsCode;
    private Button btnSmsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        editPhone = findViewById(R.id.editPhone);
        btnSendCode = findViewById(R.id.sendCodeBtn);
        editSmsCode = findViewById(R.id.editSmsCode);
        btnSmsCode = findViewById(R.id.confirmBtn);

        editSmsCode.setVisibility(View.INVISIBLE);
        btnSmsCode.setVisibility(View.INVISIBLE);
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("ron", "onVerificationCompleted");
                singIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("ron", "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
            }
        };

//        btnSendCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private void singIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PhoneActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                    finish();
                }else {
                    Log.e("ron", "Error" + task.getException().getMessage());
                    Toast.makeText(PhoneActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onSendCode(View view) {
        String phone = editPhone.getText().toString().trim();
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, this, callbacks);
        editPhone.setVisibility(View.GONE);
        btnSendCode.setVisibility(View.GONE);
        editSmsCode.setVisibility(View.VISIBLE);
        btnSmsCode.setVisibility(View.VISIBLE);
    }

    public void onConfirm(View view) {

    }
}
