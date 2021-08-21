package com.example.su21g3project.General;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class RegisterActivity extends AppCompatActivity {
    private TextView txtFName;
    private TextView txtLName;
    private TextView txtPhone;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String lName,fName,phone;
    private ProgressBar progressBar;
    private ImageView imageView;
    private MultiWaveHeader waveHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        waveHeader=findViewById(R.id.waveHeader);
        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);
        waveHeader.setStartColor(Color.BLACK);
        waveHeader.setCloseColor(Color.GRAY);

        Button btnRegister = findViewById(R.id.btnRegister);
        txtFName = findViewById(R.id.txtfName);
        txtLName = findViewById(R.id.txtlName);
        txtPhone = findViewById(R.id.txtPhone);
        progressBar = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID
                Intent intent = new Intent(RegisterActivity.this, VerifySMSToken.class);

                intent.putExtra("phone", phone);
                intent.putExtra("FName", fName);
                intent.putExtra("LName", lName);
                intent.putExtra("type","register");
                intent.putExtra("verificationId",verificationId);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                btnRegister.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                btnRegister.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this,getString(R.string.toastinvalidphone),Toast.LENGTH_SHORT)
                        .show();
            }
        };

        btnRegister.setOnClickListener(v -> {
                fName = txtFName.getText().toString().trim();
                lName = txtLName.getText().toString().trim();
                phone = txtPhone.getText().toString().trim();
                if (checkInput(fName) && checkInput(lName)) {
                    if(phone.length()!=10){
                        Toast.makeText(getApplicationContext(), getString(R.string.toastinvalidphone)
                                , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    btnRegister.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    //send code
                    phone = txtPhone.getText().toString().replaceFirst("0", "+84");
                    //send OTP
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phone)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(RegisterActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                } else{
                    Toast.makeText(getApplicationContext(), getString(R.string.toasrequired)
                            , Toast.LENGTH_SHORT).show();
                }

        });
    }
    private boolean checkInput(String string){
        if(string.isEmpty()||string==null)
            return false;
        return true;
    }
}