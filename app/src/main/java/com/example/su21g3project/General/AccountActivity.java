package com.example.su21g3project.General;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.su21g3project.Customer.BookedHistoryActivity;
import com.example.su21g3project.Customer.ProfileActivity;
import com.example.su21g3project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scwang.wave.MultiWaveHeader;

import Model.User;

public class AccountActivity extends AppCompatActivity {

    private TextView txtLogout,txtPhone,txtChange,txtName,txtBookedHistory;
    private BottomNavigationView mNavigationView;
    FirebaseUser user;
    private DatabaseReference reference;
    private MultiWaveHeader waveHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        waveHeader=findViewById(R.id.waveHeader);
        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);
        waveHeader.setStartColor(Color.GREEN);
        waveHeader.setCloseColor(Color.GRAY);
        txtName=findViewById(R.id.fragment_nameAccount);
        txtPhone=findViewById(R.id.fragment_phoneAccount);
        txtBookedHistory = findViewById(R.id.txtTableHistory);
        mNavigationView=findViewById(R.id.navigation1);
        txtLogout=findViewById(R.id.txtLogout);
        txtChange = findViewById(R.id.txtChangeInfo);
        /**
         * get current User in device
         */
        user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            /**
             * get info User
             */
            reference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        User user =snapshot.getValue(User.class);
                        txtName.setText(user.getName());
                        txtPhone.setText(user.getPhone());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            /**
             * intent to ProfileActivity when click UpdateInfo
             */
            txtChange.setOnClickListener(v -> {
                Intent intent = new Intent(AccountActivity.this, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
            /**
             * intent to BookedHistoryActivity
             */
            txtBookedHistory.setOnClickListener(v -> {
                Intent intent = new Intent(AccountActivity.this, BookedHistoryActivity.class);
                startActivity(intent);
            });
            /**
             * action when click on Text Logout
             */
            txtLogout.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Do you want logout?");
                builder.setPositiveButton("YES", (dialog, which) -> {
                    // Do nothing but close the dialog
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                    finish();
                });

                builder.setNegativeButton("NO", (dialog, which) -> {
                    // Do nothing
                    dialog.dismiss();
                });

                AlertDialog alert = builder.create();
                alert.show();
            });
        }
        mNavigationView.setSelectedItemId(R.id.navigation_account);
        /**
         * set action when choose item in NavigationView
         */
        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    Intent intent=new Intent(AccountActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_account:
                    return true;
                case R.id.navigation_address:
                    return true;
            }
            return false;
        });
    }
}