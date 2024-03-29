package com.example.su21g3project.General;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.su21g3project.Chef.MainChefActivity;
import com.example.su21g3project.Customer.NewsActivity;
import com.example.su21g3project.R;
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


import Model.News;
import Model.Notice;
import Model.Photo;
import Model.User;
import SOService.NotificationService;
import adapter.PhotoAdapter;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    private TextView txtNotice;
    private Button mainLogin;
    private ImageButton btnGetTable, btnMenu, imageButton;
    private CircleIndicator circleIndicator;
    private Timer timer;
    private ViewPager viewPager;
    private List<Photo> photoList;
    private ActionBar toolbar;
    private TextView txtUsername, txtNew;
    private ImageView newImg;
    private BottomNavigationView mNavigationView;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private int countNotice = 0;
    private List<Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noticeList = new ArrayList<>();

        txtNotice = findViewById(R.id.txtNotice);
        btnMenu = findViewById(R.id.btnMenu);
        txtUsername = findViewById(R.id.txtUseName);
        imageButton = findViewById(R.id.imageButton);
        mainLogin = findViewById(R.id.mainLogin);
        viewPager = findViewById(R.id.viewImageResstaurant);
        circleIndicator = findViewById(R.id.circleIndicator);
        btnGetTable = findViewById(R.id.btnGetTable);
        mNavigationView = findViewById(R.id.navigation);
        newImg = findViewById(R.id.imageView);
        txtNew = findViewById(R.id.txtContenNews);
        //load new
        reference = FirebaseDatabase.getInstance().getReference("News");
        reference.orderByChild("time").limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        News news = dataSnapshot1.getValue(News.class);
                        new DownloadImageTask(newImg).execute(news.getImage());
                        newImg.setClipToOutline(true);
                        txtNew.setText(news.getTitle());
                        newImg.setOnClickListener(v -> {
                            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                            intent.putExtra("content", news.getContent());
                            intent.putExtra("title", news.getTitle());
                            intent.putExtra("image", news.getImage());
                            startActivity(intent);
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        //init nav
        mNavigationView.setSelectedItemId(R.id.navigation_home);
        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_account:
                    Intent intent;
                    if(firebaseUser==null){
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                    }else{
                        intent = new Intent(MainActivity.this, AccountActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.navigation_address:
                    return true;
            }
            return false;
        });
        btnMenu.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(getApplicationContext(), NotificationService.class);
            startForegroundService(intent);
            txtNotice.setVisibility(View.VISIBLE);
            reference = FirebaseDatabase.getInstance().getReference("Communications").child("customer");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    noticeList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Notice notice = dataSnapshot.getValue(Notice.class);
                        if (!notice.getIsSeen() && notice.getUserId().equals(firebaseUser.getUid()) && notice.getIsReply()) {
                            noticeList.add(notice);
                        }
                    }
                    countNotice = noticeList.size();
                    if (countNotice> 0) {
                        txtNotice.setText(String.valueOf(countNotice));
                    }else
                        txtNotice.setText("");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        if (user.getRole().equals("waiter")) {
                            Intent intent = new Intent(MainActivity.this, MainWaiterActivity.class);
                            intent.putExtra("role", user.getRole());
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        if (user.getRole().equals("chef")) {
                            Intent intent = new Intent(MainActivity.this, MainChefActivity.class);
                            intent.putExtra("role", user.getRole());
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        txtUsername.setText(R.string.welcome);
                        txtUsername.setText(txtUsername.getText() + " " + user.getName());
                        mainLogin.setVisibility(View.INVISIBLE);
                        imageButton.setVisibility(View.VISIBLE);
                        btnGetTable.setOnClickListener(v -> {
                            Intent intent = new Intent(MainActivity.this, GetTableActivity.class);
                            intent.putExtra("role", user.getRole());
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
                    } else {
                        FirebaseAuth.getInstance().signOut();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        imageButton.setOnClickListener(v -> {
            Intent intent1=new Intent(MainActivity.this,NoticeActivity.class);
            intent1.putExtra("role","customer");
            startActivity(intent1);
        });
        mainLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
        photoList = getPhoto();
        PhotoAdapter photoAdapter = new PhotoAdapter(this, photoList);
        viewPager.setClipToOutline(true);
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        autoSlideImage();
        toolbar = getSupportActionBar();
    }

    private void autoSlideImage() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    int currentItem = viewPager.getCurrentItem();
                    int totalTtem = photoList.size() - 1;
                    if (currentItem < totalTtem) {
                        currentItem++;
                        viewPager.setCurrentItem(currentItem);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                });
            }
        }, 1000, 3000);
    }

    private List<Photo> getPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.phan1));
        list.add(new Photo(R.drawable.phan2));
        list.add(new Photo(R.drawable.phan3));
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationView.setSelectedItemId(R.id.navigation_home);
    }
}