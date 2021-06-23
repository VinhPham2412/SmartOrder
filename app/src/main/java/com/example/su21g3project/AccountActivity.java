package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private TextView txtLogout,txtPhone,txtChange;
    private BottomNavigationView mNavigationView;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        txtLogout=findViewById(R.id.txtLogout);
        txtChange = findViewById(R.id.txtChangeInfo);
        txtLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AccountActivity.this,LoginActivity.class));
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        txtPhone=findViewById(R.id.fragment_phoneAccount);
        if(user!=null){
            txtPhone.setText(user.getPhoneNumber());
        }
        mNavigationView=findViewById(R.id.navigation);
        mNavigationView.setSelectedItemId(R.id.navigation_account);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_account:
                        Intent intent1=new Intent(getApplicationContext(), AccountActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
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