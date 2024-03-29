package com.example.su21g3project.General;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scwang.wave.MultiWaveHeader;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Model.Order;
import Model.User;

public class GetTableActivity2 extends AppCompatActivity {
    private Button btnNext;
    private EditText txtName, txtPhone, txtNote;
    private DatabaseReference reference;
    private FirebaseUser user;
    private ProgressBar bar;
    private String userId;
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
        setContentView(R.layout.activity_get_table2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.datban);
        waveHeader=findViewById(R.id.waveHeader);
        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);
        waveHeader.setStartColor(Color.BLACK);
        waveHeader.setCloseColor(Color.GRAY);

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
            String name;
            String phone;
            String note;
            user = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Orders");
            name = txtName.getText().toString();
            phone = txtPhone.getText().toString();
            note = txtNote.getText().toString();
            Date date = null;
            String strDate = getIntent().getStringExtra("date");
            DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
            try {
                date = format.parse(strDate);
                int x = 3;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int noPP = Integer.parseInt(getIntent().getStringExtra("noPP"));
            String orderId = reference.push().getKey();
            Order order = new Order(orderId,userId,name,phone,date,noPP,note,"new",null,null,false);
            reference.child(orderId).setValue(order.toMap()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(GetTableActivity2.this,
                            "Gửi yêu cầu đặt bàn thành công!",Toast.LENGTH_SHORT).show();
                    btnNext.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.GONE);
                    startActivity(new Intent(GetTableActivity2.this, MainActivity.class));
                    finish();
                }
            });

        });
    }
}