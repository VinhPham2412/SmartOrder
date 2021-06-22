package com.example.su21g3project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.User;

public class UpdateProfile extends AppCompatActivity {

    private Button btnSave;
    private FirebaseUser fbuser;
    private EditText txtName,txtPhone,txtAddress;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    private User duser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        txtName = findViewById(R.id.txtProfileName);
        txtPhone = findViewById(R.id.txtProfilePhone);
        txtAddress = findViewById(R.id.txtProfileAdress);
        progressBar = findViewById(R.id.progressBar4);
        //get user from authen to get uid
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        //get user info from database with uid
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(fbuser.getUid()).get().addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()){
                        duser = task.getResult().getValue(User.class);
                    }else{
                        Toast.makeText(UpdateProfile.this,"Error loading user data",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
        //display
        if(duser!=null){
            txtName.setText(duser.getName());
            txtPhone.setText(duser.getPhone());
            txtAddress.setText(duser.getAddress());
        }
        btnSave = findViewById(R.id.btnSaveUpdate);
        btnSave.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.INVISIBLE);
            String name = txtName.getText().toString();
            String phone = txtPhone.getText().toString();
            String address = txtAddress.getText().toString();

            User user = new User(FirebaseAuth.getInstance().getUid(),name,phone,address);
            mDatabase.child("users").child(fbuser.getUid()).setValue(user)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(UpdateProfile.this,
                                "Update success.", Toast.LENGTH_SHORT).show();
                        btnSave.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(UpdateProfile.this,AccountActivity.class);
                        startActivity(intent);
                    });
        });
    }
}