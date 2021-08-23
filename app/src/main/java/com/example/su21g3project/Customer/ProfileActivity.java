package com.example.su21g3project.Customer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.su21g3project.General.AccountActivity;
import com.example.su21g3project.General.LoginActivity;
import com.example.su21g3project.R;
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

import Model.User;

public class ProfileActivity extends AppCompatActivity {

    private Button btnSave;
    private ImageButton btnLogout;
    private FirebaseUser fbuser;
    private EditText txtName, txtPhone, txtAddress;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    private User duser;
    private String id;
    private MultiWaveHeader waveHeader;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.account);
        waveHeader = findViewById(R.id.waveHeader);
        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);
        waveHeader.setStartColor(Color.BLACK);
        waveHeader.setCloseColor(Color.GRAY);

        txtName = findViewById(R.id.txtProfileName);
        txtPhone = findViewById(R.id.txtProfilePhone);
        txtAddress = findViewById(R.id.txtProfileAdress);
        progressBar = findViewById(R.id.progressBar4);
        btnSave = findViewById(R.id.btnSaveUpdate);
        btnLogout = findViewById(R.id.btnLogout);
        //get user from authen to get uid
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.INVISIBLE);
        if (fbuser != null) {
            //get user info from database with uid
            mDatabase = FirebaseDatabase.getInstance().getReference();
            id = fbuser.getUid();
            mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        duser = snapshot.getValue(User.class);
                        txtName.setText(duser.getName());
                        txtPhone.setText(duser.getPhone());
                        txtAddress.setText(duser.getAddress());
                        progressBar.setVisibility(View.GONE);
                        btnSave.setVisibility(View.VISIBLE);
                        if (duser.getRole().equals("waiter")) {
                            btnLogout.setVisibility(View.VISIBLE);
                            btnLogout.setOnClickListener(v -> {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                                finish();
                            });
                        } else {
                            btnLogout.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, error.getMessage(),
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

                User user = new User(id, name, phone, address);
                mDatabase.child("Users").child(id).setValue(user.toMap())
                        .addOnCompleteListener(task -> {
                            Toast.makeText(ProfileActivity.this,
                                    "Update success.", Toast.LENGTH_SHORT).show();
                            btnSave.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();

                            fbuser.updateProfile(profileUpdates);

                            Intent intent = new Intent(ProfileActivity.this, AccountActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
            });
        }
    }
}