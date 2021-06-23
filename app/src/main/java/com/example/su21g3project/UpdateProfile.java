package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import Model.User;

public class UpdateProfile extends AppCompatActivity {

    private Button btnSave;
    private FirebaseUser fbuser;
    private EditText txtName,txtPhone,txtAddress;
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    private User duser;
    private String id;

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
        id = fbuser.getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.INVISIBLE);
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
        btnSave = findViewById(R.id.btnSaveUpdate);
        btnSave.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.INVISIBLE);
            String id = FirebaseAuth.getInstance().getUid();
            String name = txtName.getText().toString();
            String phone = txtPhone.getText().toString();
            String address = txtAddress.getText().toString();

            User user = new User(name,phone,address);
            user.setId(id);
            mDatabase.child("Users").child(id).setValue(user.toMap())
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