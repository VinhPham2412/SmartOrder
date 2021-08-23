package com.example.su21g3project.Waiter;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.su21g3project.Customer.ProfileActivity;
import com.example.su21g3project.General.AccountActivity;
import com.example.su21g3project.General.MainActivity;
import com.example.su21g3project.General.NoticeActivity;
import com.example.su21g3project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.User;
import adapter.ViewPagerAdapter;

public class MainWaiterActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private FirebaseUser user;
    private String role="";
    private DatabaseReference reference;
    private BottomNavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_waiter);
        user= FirebaseAuth.getInstance().getCurrentUser();
        tabLayout=findViewById(R.id.tabLayout);
        mNavigationView =findViewById(R.id.navigation2);
        //init nav
        mNavigationView.setSelectedItemId(R.id.waiterHome);
        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.waiterNotify:
                    //swich to notify
                    Intent intent = new Intent(MainWaiterActivity.this, NoticeActivity.class);
                    intent.putExtra("role",role);
                    startActivity(intent);
                    return true;
                case R.id.waiterHome:
                    //switch to home
                    viewPager2.setCurrentItem(0);
                    return true;
                case R.id.waiterAccount:
                    //switch to account
                    Intent intent1 = new Intent(MainWaiterActivity.this, ProfileActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    return true;
            }
            return false;
        });
        reference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user=snapshot.getValue(User.class);
                    role=user.getRole();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewPager2=findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(MainWaiterActivity.this);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setOffscreenPageLimit(5);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText(R.string.home);
                    break;
                case 1:
                    tab.setText(R.string.hoadon);
                    break;
                case 2:
                    tab.setText(R.string.cooked);
                    break;
                case 3:
                    tab.setText(R.string.communication);
                    break;
                default:
                    tab.setText(R.string.goimon);
                    break;
            }
        }).attach();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mNavigationView.setSelectedItemId(R.id.waiterHome);
    }
}