package com.example.su21g3project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.su21g3project.Customer.AccountActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scwang.wave.MultiWaveHeader;

import org.jetbrains.annotations.NotNull;

import model.User;

public class UpdateProfile extends AppCompatActivity {

    private Button btnSave;
    private FirebaseUser fbuser;
    private EditText txtName,txtPhone,txtAddress;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    private User duser;
    private String id;
    private MultiWaveHeader waveHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        waveHeader=findViewById(R.id.waveHeader);
        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);
        waveHeader.setStartColor(Color.MAGENTA);
        waveHeader.setCloseColor(Color.YELLOW);

        txtName = findViewById(R.id.txtProfileName);
        txtPhone = findViewById(R.id.txtProfilePhone);
        txtAddress = findViewById(R.id.txtProfileAdress);
        progressBar = findViewById(R.id.progressBar4);
        btnSave = findViewById(R.id.btnSaveUpdate);
        //get user from authen to get uid
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.INVISIBLE);
        if(fbuser!=null){
            //get user info from database with uid
            mDatabase = FirebaseDatabase.getInstance().getReference();
            id = fbuser.getUid();
            mDatabase.child("User").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null){
                        duser = snapshot.getValue(User.class);
                        txtName.setText(duser.getName());
                        txtPhone.setText(duser.getPhone());
                        txtAddress.setText(duser.getAddress());
                        progressBar.setVisibility(View.GONE);
                        btnSave.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(UpdateProfile.this,error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            btnSave.setOnClickListener(v -> {
                progressBar.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.INVISIBLE);
                String id = FirebaseAuth.getInstance().getUid();
                String name = txtName.getText().toString();
                String phone = txtPhone.getText().toString();
                String address = txtAddress.getText().toString();

                User user = new User(name,phone,address);
                user.setId(id);
                mDatabase.child("User").child(id).setValue(user.toMap())
                        .addOnCompleteListener(task -> {
                            Toast.makeText(UpdateProfile.this,
                                    "Update success.", Toast.LENGTH_SHORT).show();
                            btnSave.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();

                            fbuser.updateProfile(profileUpdates);

                            Intent intent = new Intent(UpdateProfile.this, AccountActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
            });
        }
    }
}