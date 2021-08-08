package com.example.su21g3project.Chef;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.su21g3project.Customer.CommunicationActivity;
import com.example.su21g3project.General.LoginActivity;
import com.example.su21g3project.General.NoticeActivity;
import com.example.su21g3project.R;
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
import adapter.Chef.ChefViewPagerAdapter;

public class MainChefActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private TabLayout tabLayout;
    private TextView txtChefName;
    FirebaseUser user;
    private String role="";
    private ViewPager2 viewPager2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chef);
        user= FirebaseAuth.getInstance().getCurrentUser();
        txtChefName=findViewById(R.id.chefName);
        tabLayout=findViewById(R.id.tabLayout);
        reference=FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        /**
         * get info of Chef user
         */
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user=snapshot.getValue(User.class);
                    txtChefName.setText(user.getName());
                    role=user.getRole();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        viewPager2=findViewById(R.id.viewPager);
        ChefViewPagerAdapter viewPagerAdapter=new ChefViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText(R.string.trangchu);
                    break;
                case 1:
                    tab.setText(R.string.order);
                    break;
            }
        }).attach();

    }

    /**
     * add options Menu in activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chef_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * set action for click on Item in menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.Notice:
               Intent intent= new Intent(MainChefActivity.this, NoticeActivity.class);
               intent.putExtra("role",role);
               startActivity(intent);
                break;
            case R.id.Communication:
                startActivity(new Intent(MainChefActivity.this, CommunicationActivity.class));
                break;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainChefActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Do you want logout?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainChefActivity.this, LoginActivity.class));
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}