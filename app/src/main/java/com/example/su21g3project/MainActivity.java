package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.su21g3project.Customer.AccountActivity;
import com.example.su21g3project.Customer.BookedHistory;
import com.example.su21g3project.Waiter.MainWaiterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scwang.wave.MultiWaveHeader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.PhotoAdapter;
import model.Photo;
import model.ProcessOrder;
import model.User;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    private TextView txtNotice;
    private Button mainLogin;
    private ImageButton btnGetTable,btnMenu,imageButton,notify;
    private CircleIndicator circleIndicator;
    private Timer timer;
    private ViewPager viewPager;
    private List<Photo> photoList;
    private ActionBar toolbar;
    private TextView txtUsername;
    private BottomNavigationView mNavigationView;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private List<ProcessOrder> processOrderList;
    private MultiWaveHeader waveHeader,waveFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waveFooter=findViewById(R.id.waveFooter);
        waveHeader=findViewById(R.id.waveHeader);

//        waveHeader.setVelocity(1);
//        waveHeader.setProgress(1);
//        waveHeader.isRunning();
//        waveHeader.setGradientAngle(45);
//        waveHeader.setWaveHeight(40);
//        waveHeader.setStartColor(Color.BLACK);
//        waveHeader.setCloseColor(Color.WHITE);

        waveFooter.setVelocity(1);
        waveFooter.setProgress(1);
        waveFooter.isRunning();
        waveFooter.setGradientAngle(45);
        waveFooter.setWaveHeight(40);
        waveFooter.setStartColor(Color.WHITE);
        waveFooter.setCloseColor(Color.BLACK);

        txtNotice=findViewById(R.id.txtNotice);
        btnMenu=findViewById(R.id.btnMenu);
        txtUsername = findViewById(R.id.txtUseName);
        imageButton=findViewById(R.id.imageButton);
        mainLogin=findViewById(R.id.mainLogin);
        viewPager =findViewById(R.id.viewImageResstaurant);
        circleIndicator=findViewById(R.id.circleIndicator);
        btnGetTable = findViewById(R.id.btnGetTable);
        mNavigationView=findViewById(R.id.navigation);
        //init nav
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
        btnMenu.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,MenuActivity.class));
            finish();
        });

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null) {
            mainLogin.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.VISIBLE);
           reference=FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
           reference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()){
                       User user=snapshot.getValue(User.class);
                       txtUsername.setText(txtUsername.getText()+" " +user.getName());
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
           processOrderList =new ArrayList<>();
            reference=FirebaseDatabase.getInstance().getReference("ProcessOrder");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    processOrderList.clear();
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        ProcessOrder processOrder=dataSnapshot.getValue(ProcessOrder.class);
                        if((processOrder.getStatus().equals("confirmed") && processOrder.getUserId().equals(firebaseUser.getUid())) ||
                                (processOrder.getStatus().equals("rejected") && processOrder.getUserId().equals(firebaseUser.getUid()))){
                            processOrderList.add(processOrder);
                        }
                    }
                    if(processOrderList.size()>0){
                        txtNotice.setVisibility(View.VISIBLE);
                        txtNotice.setText(String.valueOf(processOrderList.size()));
                        SharedPreferences pref=getSharedPreferences("main", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putInt("processOrderList",processOrderList.size());
                        editor.commit();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        imageButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BookedHistory.class)));
        btnGetTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GetTableActivity.class));
                finish();
            }
        });
        mainLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        });
        photoList=getPhoto();
        PhotoAdapter photoAdapter =new PhotoAdapter(this,photoList);
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        autoSlideImage();
        toolbar = getSupportActionBar();
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
        list.add(new Photo(R.drawable.phan1));
        list.add(new Photo(R.drawable.phan3));
        list.add(new Photo(R.drawable.phan2));
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