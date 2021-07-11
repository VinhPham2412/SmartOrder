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
    private Button btnNotHaveAccount,mainLogin;
    private ImageButton btnGetTable,btnMenu,btnVoucher;
    private CircleIndicator circleIndicator;
    private Timer timer;
    ViewPager viewPager;
    private List<Photo> photoList;
    private ActionBar toolbar;
    private TextView txtUsername;
    private FirebaseUser user;
    private ImageButton imageButton;
    private BottomNavigationView mNavigationView;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationView=findViewById(R.id.navigation);
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
                    User user = snapshot.getValue(User.class);
                    txtUsername.setText("Xin chào " + user.getName());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
            txtUsername.setText("Xin chào ");
        mainLogin=findViewById(R.id.mainLogin);
        mainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });
        viewPager =findViewById(R.id.viewImageResstaurant);
        photoList=getPhoto();
        PhotoAdapter photoAdapter =new PhotoAdapter(this,photoList);
        viewPager.setAdapter(photoAdapter);
        circleIndicator=findViewById(R.id.circleIndicator);
        circleIndicator.setViewPager(viewPager);
        autoSlideImage();
        toolbar = getSupportActionBar();
        btnNotHaveAccount=findViewById(R.id.btnNotHaveAccount);
        imageButton=findViewById(R.id.imageButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
         if(user==null){
            btnNotHaveAccount.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            mainLogin.setVisibility(View.INVISIBLE);
            btnNotHaveAccount.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this,ResisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });

        }else{
            btnNotHaveAccount.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.VISIBLE);
            mainLogin.setVisibility(View.INVISIBLE);

            toolbar.setTitle("Trang chủ");
        }
        btnGetTable = findViewById(R.id.btnGetTable);
        btnGetTable.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,GetTableActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        btnVoucher=findViewById(R.id.btnVoucher);
        btnVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GetBuffetActivity.class));
            }
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
        SharedPreferences preferences=getSharedPreferences("main", Context.MODE_PRIVATE);
        String visible=preferences.getString("1","login");
        if (visible.equals("logout")){
            mainLogin.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            btnNotHaveAccount.setVisibility(View.INVISIBLE);
        }
        else {
            return;
        }
    }
}