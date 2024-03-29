package com.example.su21g3project.General;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.su21g3project.Chef.MainChefActivity;
import com.example.su21g3project.R;
import com.example.su21g3project.Waiter.MainWaiterActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scwang.wave.MultiWaveHeader;

import java.util.concurrent.TimeUnit;

import Model.User;

import static android.content.ContentValues.TAG;

public class VerifySMSToken extends AppCompatActivity {

    private TextView txtPhone, txtResend;
    private TextView num1, num2, num3, num4, num5, num6;
    private Button btnVerify;
    private ProgressBar progressBar;
    private String phone, FName, LName;
    private FirebaseAuth mAuth;
    private String type;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private DatabaseReference databaseReference;

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, String type) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String userId = firebaseUser.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //if have account and login
                                if (snapshot.exists() && type.equals("login")) {
                                    User user = snapshot.getValue(User.class);
                                    if(user.getStatus()){
                                        String role = user.getRole();
                                        switch (role) {
                                            case "customer":
                                                Intent intent = new Intent(VerifySMSToken.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                                break;
                                            case "waiter":
                                                Intent intent1 = new Intent(VerifySMSToken.this, MainWaiterActivity.class);
                                                startActivity(intent1);
                                                finish();
                                                break;
                                            case "chef":
                                                Intent intent2 = new Intent(VerifySMSToken.this, MainChefActivity.class);
                                                startActivity(intent2);
                                                finish();
                                                break;
                                        }
                                    }else{
                                        Intent intent = new Intent(VerifySMSToken.this, LoginActivity.class);
                                        Toast.makeText(getApplicationContext(),getString(R.string.accountBan),Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                //if don't have account and register
                                if (!snapshot.exists() && type.equals("register")) {
                                    //insert user
                                    FName = getIntent().getStringExtra("FName");
                                    LName = getIntent().getStringExtra("LName");
                                    databaseReference.setValue(new User(userId, FName + LName, phone, "unknow").toMap())
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(VerifySMSToken.this,
                                                            "Register success, welcome to Smart Order", Toast.LENGTH_SHORT);
                                                    startActivity(new Intent(VerifySMSToken.this, MainActivity.class));
                                                    finish();
                                                }
                                            });
                                }
                                // if have account and register
                                if (snapshot.exists() && type.equals("register")) {
                                    Toast.makeText(VerifySMSToken.this, "This phone number already been used, please log in.", Toast.LENGTH_SHORT);
                                    startActivity(new Intent(VerifySMSToken.this, LoginActivity.class));
                                    finish();
                                }
                                //if don't have account and login
                                if (!snapshot.exists() && type.equals("login")) {
                                    Toast.makeText(VerifySMSToken.this, "Your account don't exist in our database, please register.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(VerifySMSToken.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
//
                            }
                        };
                        databaseReference.addListenerForSingleValueEvent(eventListener);
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            progressBar.setVisibility(View.GONE);
                            btnVerify.setVisibility(View.VISIBLE);
                            // The verification code entered was invalid
                            Toast.makeText(VerifySMSToken.this, "Invalid verification code", Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    btnVerify.setVisibility(View.VISIBLE);
                });
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code, String type) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential, type);
    }

    private MultiWaveHeader waveHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_s_m_s_token);
        waveHeader = findViewById(R.id.waveHeader);
        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);
        waveHeader.setStartColor(Color.BLACK);
        waveHeader.setCloseColor(Color.GRAY);
        txtPhone = findViewById(R.id.textMobile);
        txtResend = findViewById(R.id.textResendOTP);
        btnVerify = findViewById(R.id.btnVerify);
        progressBar = findViewById(R.id.progressBar3);
        mAuth = FirebaseAuth.getInstance();

        phone = getIntent().getStringExtra("phone");
        //get type of verify
        type = getIntent().getStringExtra("type");
        txtPhone.setText(phone);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential, type);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(VerifySMSToken.this, "Invalid request", Toast.LENGTH_SHORT).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(VerifySMSToken.this, "The SMS quota for the project has been exceeded"
                            , Toast.LENGTH_SHORT).show();
                }
                return;
                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        num1 = findViewById(R.id.inputCode1);
        num2 = findViewById(R.id.inputCode2);
        num3 = findViewById(R.id.inputCode3);
        num4 = findViewById(R.id.inputCode4);
        num5 = findViewById(R.id.inputCode5);
        num6 = findViewById(R.id.inputCode6);
        num1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (s.length() == 1)     //size as per your requirement
                {
                    num2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        num2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                switch (s.length()) {
                    case 0:
                        num1.requestFocus();
                        break;
                    default:
                        num3.requestFocus();
                        break;
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        num3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                switch (s.length()) {
                    case 0:
                        num2.requestFocus();
                        break;
                    default:
                        num4.requestFocus();
                        break;
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        num4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                switch (s.length()) {
                    case 0:
                        num3.requestFocus();
                        break;
                    default:
                        num5.requestFocus();
                        break;
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        num5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                switch (s.length()) {
                    case 0:
                        num4.requestFocus();
                        break;
                    default:
                        num6.requestFocus();
                        break;
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        num6.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                switch (s.length()) {
                    case 0:
                        num5.requestFocus();
                        break;
                    default:
                        verify();
                        break;
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        txtResend.setOnClickListener(v -> {
            resendVerificationCode(phone, mResendToken);
        });

        btnVerify.setOnClickListener(v -> {
            verify();
        });

    }

    private void verify() {
        if (num1.getText().toString().trim().isEmpty()
                || num2.getText().toString().trim().isEmpty()
                || num3.getText().toString().trim().isEmpty()
                || num4.getText().toString().trim().isEmpty()
                || num5.getText().toString().trim().isEmpty()
                || num6.getText().toString().trim().isEmpty()) {
            Toast.makeText(VerifySMSToken.this, getString(R.string.toasinvalidotp),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String code = num1.getText().toString();
        code += num2.getText().toString();
        code += num3.getText().toString();
        code += num4.getText().toString();
        code += num5.getText().toString();
        code += num6.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        btnVerify.setVisibility(View.GONE);
        mVerificationId = getIntent().getStringExtra("verificationId");
        verifyPhoneNumberWithCode(mVerificationId, code, type);
    }
}