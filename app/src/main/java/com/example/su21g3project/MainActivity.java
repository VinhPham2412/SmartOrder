package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.su21g3project.Customer.AccountActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.PhotoAdapter;
import model.Photo;
import model.User;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    private Button mainLogin;
    private ImageButton btnGetTable,btnMenu,btnVoucher,notify;
    private CircleIndicator circleIndicator;
    private Timer timer;
    ViewPager viewPager;
    private List<Photo> photoList;
    private ActionBar toolbar;
    private TextView txtUsername;
    private BottomNavigationView mNavigationView;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationView=findViewById(R.id.navigation);
        notify = findViewById(R.id.imageButton);
        mNavigationView.setSelectedItemId(R.id.navigation_home);
        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_account:
                    Intent intent=new Intent(MainActivity.this, AccountActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_address:
                    return true;
            }
            return false;
        });
        btnMenu=findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,MenuActivity.class));
            finish();
        });
        txtUsername = findViewById(R.id.txtUseName);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null) {
            reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        User user = snapshot.getValue(User.class);
                        txtUsername.setText("Xin chào " + user.getName());
                        mainLogin.setVisibility(View.INVISIBLE);
                        notify.setVisibility(View.VISIBLE);
                    }else{
                        FirebaseAuth.getInstance().signOut();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        txtUsername.setText("Xin chào ");
        mainLogin=findViewById(R.id.mainLogin);
        mainLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        });
        viewPager =findViewById(R.id.viewImageResstaurant);
        photoList=getPhoto();
        PhotoAdapter photoAdapter =new PhotoAdapter(this,photoList);
        viewPager.setAdapter(photoAdapter);
        circleIndicator=findViewById(R.id.circleIndicator);
        circleIndicator.setViewPager(viewPager);
        autoSlideImage();
        toolbar = getSupportActionBar();
        btnGetTable = findViewById(R.id.btnGetTable);
        btnGetTable.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,GetTableActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
    private void autoSlideImage(){
        if(timer==null){
            timer=new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    int currentItem=viewPager.getCurrentItem();
                    int totalTtem=photoList.size()-1;
                    if(currentItem<totalTtem){
                        currentItem++;
                        viewPager.setCurrentItem(currentItem);
                    }else {
                        viewPager.setCurrentItem(0);
                    }
                });
            }
        },1000,3000);
    }
    private List<Photo> getPhoto(){
        List<Photo> list=new ArrayList<>();
        list.add(new Photo(R.drawable.buffet));
        list.add(new Photo(R.drawable.bf3));
        list.add(new Photo(R.drawable.bf2));
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}