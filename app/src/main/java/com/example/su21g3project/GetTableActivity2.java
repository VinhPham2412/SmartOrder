package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

import model.ProcessOrder;
import model.User;

public class GetTableActivity2 extends AppCompatActivity {
    private Button btnNext;
    private EditText txtName, txtPhone, txtNote;
    private DatabaseReference reference;
    private FirebaseUser user;
    private ProgressBar bar;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_table2);

        txtName = findViewById(R.id.txtCName);
        txtPhone = findViewById(R.id.txtCPhone);
        txtNote = findViewById(R.id.txtNote);
        bar = findViewById(R.id.progressBar5);
        btnNext = findViewById(R.id.btnNext1);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            userId = user.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null){
                        User user1 = snapshot.getValue(User.class);
                        txtName.setText(user1.getName());
                        txtPhone.setText(user1.getPhone());
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        btnNext.setOnClickListener(v -> {
            btnNext.setVisibility(View.INVISIBLE);
            bar.setVisibility(View.VISIBLE);
            String orderId = UUID.randomUUID().toString();
            String name;
            String phone;
            String note;
            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("ProcessOrder");
            name = txtName.getText().toString();
            phone = txtPhone.getText().toString();
            note = txtNote.getText().toString();
            Date date = new Date();
            date.setTime(getIntent().getLongExtra("date",-1));
            int noPP = Integer.parseInt(getIntent().getStringExtra("noPP"));
            ProcessOrder order = new ProcessOrder(orderId,userId,name,phone,date,noPP,note,false);
            reference.child(orderId).setValue(order.toMap()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(GetTableActivity2.this,
                            "Your order have been received.",Toast.LENGTH_SHORT).show();
                    btnNext.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.GONE);
                    finish();
                }
            });
        });
    }
}