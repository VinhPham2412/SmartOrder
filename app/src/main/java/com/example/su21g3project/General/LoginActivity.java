package com.example.su21g3project.General;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.su21g3project.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.scwang.wave.MultiWaveHeader;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView txtRegister;
    private TextView phone;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String phoneNumber;
    private MultiWaveHeader waveHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        waveHeader=findViewById(R.id.waveHeader);
        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);
        waveHeader.setStartColor(Color.BLACK);
        waveHeader.setCloseColor(Color.GRAY);
        phone = findViewById(R.id.txtLoginAccount);
        final ProgressBar progressBar=findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID
                Intent intent = new Intent(LoginActivity.this, VerifySMSToken.class);

                intent.putExtra("phone", phoneNumber);
                intent.putExtra("verificationId",verificationId);
                intent.putExtra("type","login");
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                progressBar.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                txtRegister.setVisibility(View.VISIBLE);
                startActivity(intent);
            }

            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                txtRegister.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                txtRegister.setVisibility(View.VISIBLE);
            }
        };
        btnLogin=findViewById(R.id.btnLogin);
        txtRegister =findViewById(R.id.txtRegister);
        btnLogin.setOnClickListener(v -> {

            if (phone.getText().toString().trim().isEmpty()){
                Toast.makeText(LoginActivity.this,"Enter phone number",Toast.LENGTH_SHORT).show();
                return;
            }
            phoneNumber = phone.getText().toString().replaceFirst("0","+84");
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            txtRegister.setVisibility(View.INVISIBLE);

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(LoginActivity.this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        });
        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

}