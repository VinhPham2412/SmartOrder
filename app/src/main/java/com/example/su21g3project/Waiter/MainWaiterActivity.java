package com.example.su21g3project.Waiter;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.su21g3project.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import adapter.ViewPagerAdapter;

public class MainWaiterActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private TextView txtWaiterName;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_waiter);
        txtWaiterName=findViewById(R.id.waiterName);
        user= FirebaseAuth.getInstance().getCurrentUser();
        txtWaiterName.setText(user.getDisplayName());
        tabLayout=findViewById(R.id.tabLayout);
        viewPager2=findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Trang chủ");
                    break;
                case 1:
                    tab.setText("Hóa Đơn");
                    break;
                case 2:
                    tab.setText("Gọi món");
                    break;
                case 3:
                    tab.setText("Giao tiếp");
                    break;
            }
        }).attach();

    }
}