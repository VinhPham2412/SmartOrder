package com.example.su21g3project.Waiter;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.su21g3project.LoginActivity;
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
    private ImageButton btnSingOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_waiter);
        btnSingOut=findViewById(R.id.imageButtonSingOut);
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
                    tab.setText(R.string.trangchu);
                    break;
                case 1:
                    tab.setText(R.string.bill);
                    break;
                case 2:
                    tab.setText(R.string.goimon);
                    break;
                case 3:
                    tab.setText(R.string.communication);
                    break;
            }
        }).attach();
        btnSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainWaiterActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainWaiterActivity.this,LoginActivity.class));
                        finish();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }
}