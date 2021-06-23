package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Adapter.PhotoAdapter;
import Model.Photo;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    private Button btnNotHaveAccount;
    private ImageButton btnGetTable;
    private CircleIndicator circleIndicator;
    private Timer timer;
    ViewPager viewPager,view_pager;
    private List<Photo> photoList;
    private ActionBar toolbar;
    private TextView txtUsername;
    private FirebaseUser user;
    private ImageButton imageButton;
    private BottomNavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        viewPager =findViewById(R.id.viewImageResstaurant);
        photoList=getPhoto();
        PhotoAdapter photoAdapter =new PhotoAdapter(this,photoList);
        viewPager.setAdapter(photoAdapter);
        circleIndicator=findViewById(R.id.circleIndicator);
        circleIndicator.setViewPager(viewPager);
        autoSlideImage();
        toolbar = getSupportActionBar();
        txtUsername = findViewById(R.id.txtUseName);
        btnNotHaveAccount=findViewById(R.id.btnNotHaveAccount);
        imageButton=findViewById(R.id.imageButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            btnNotHaveAccount.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            btnNotHaveAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,ResisterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

        }else{
            btnNotHaveAccount.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.VISIBLE);
            String welcome = "Welcome ";
            welcome+= user.getDisplayName();
            txtUsername.setText(welcome);

            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

            toolbar.setTitle("Trang chủ");
        }
        view_pager=findViewById(R.id.view_pager);
        mNavigationView=findViewById(R.id.navigation);
        mNavigationView.setSelectedItemId(R.id.navigation_home);
        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_account:
                    Intent intent1=new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_address:
                    return true;
            }
            return false;
        });
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
}