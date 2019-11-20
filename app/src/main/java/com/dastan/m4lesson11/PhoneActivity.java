package com.dastan.m4lesson11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private EditText editPhone;
    private Button btnSendCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private EditText editSmsCode;
    private Button btnSmsCode;
    private String codeSent;

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
                //signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("ron", "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSent = s;
            }
        };

    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PhoneActivity.this, "Verification succeed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Log.e("ron", "Error" + task.getException().getMessage());
                            Toast.makeText(PhoneActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onSendCode(View view) {
        String phone = editPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            editPhone.setError("Input phone number");
            editPhone.requestFocus();
            return;
        }

        if (phone.length() < 10) {
            editPhone.setError("Incorrect phone number");
            editPhone.requestFocus();
            return;
        }
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, this, callbacks);
        editPhone.setVisibility(View.GONE);
        btnSendCode.setVisibility(View.GONE);
        editSmsCode.setVisibility(View.VISIBLE);
        btnSmsCode.setVisibility(View.VISIBLE);
    }

    public void onConfirm(View view) {
        String code = editSmsCode.getText().toString().trim();
        if (code.isEmpty()){
            editSmsCode.setError("Input sms code");
            editSmsCode.requestFocus();
            return;
        }

//        if (code.length() == 6){
//            editSmsCode.setError("Incorrect sms code");
//            editSmsCode.requestFocus();
//            return;
//        }
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signIn(credential);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Incorrect code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
