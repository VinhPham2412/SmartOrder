package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.User;

public class AccountActivity extends AppCompatActivity {

    private TextView txtLogout,txtPhone,txtChange,txtName;
    private BottomNavigationView mNavigationView;
    FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        txtName=findViewById(R.id.fragment_nameAccount);
        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user =snapshot.getValue(User.class);
                txtName.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        txtLogout=findViewById(R.id.txtLogout);
        txtChange = findViewById(R.id.txtChangeInfo);
        txtLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(AccountActivity.this,MainActivity.class);
            SharedPreferences pref=getSharedPreferences("main", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=pref.edit();
            editor.putString("1","logout");
            editor.commit();
            startActivity(intent);
            finish();
        });
        txtPhone=findViewById(R.id.fragment_phoneAccount);
        if(user!=null){
            txtPhone.setText(user.getPhoneNumber());
        }
        mNavigationView=findViewById(R.id.navigation1);
        mNavigationView.setSelectedItemId(R.id.navigation_account);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });
        txtChange.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this,UpdateProfile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}