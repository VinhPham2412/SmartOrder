package com.example.su21g3project.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.su21g3project.MainActivity;
import com.example.su21g3project.R;
import com.example.su21g3project.UpdateProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.User;

public class AccountActivity extends AppCompatActivity {

    private TextView txtLogout,txtPhone,txtChange,txtName,txtOrderHistory,txtBookedHistory;
    private BottomNavigationView mNavigationView;
    FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        txtName=findViewById(R.id.fragment_nameAccount);
        txtPhone=findViewById(R.id.fragment_phoneAccount);
        txtBookedHistory = findViewById(R.id.txtTableHistory);
        txtOrderHistory =findViewById(R.id.txtLanguage);
        /**
         * get current User in device
         */
        user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            /**
             * get info User
             */
            reference= FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
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
        }
        txtLogout=findViewById(R.id.txtLogout);
        txtChange = findViewById(R.id.txtChangeInfo);
        /**
         * action when click on Text Logout
         */
        txtLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(AccountActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        mNavigationView=findViewById(R.id.navigation1);
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
        /**
         * intent to UpdateProfile when click UpdateInfo
         */
        txtChange.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, UpdateProfile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        /**
         * intent to BookedHistory
         */
        txtBookedHistory.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, BookedHistory.class);
            startActivity(intent);
        });

        txtOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, ChangeLanguageActivity.class);
            startActivity(intent);
        });
    }
}