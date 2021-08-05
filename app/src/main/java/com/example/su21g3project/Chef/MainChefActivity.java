package com.example.su21g3project.Chef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.su21g3project.Customer.AccountActivity;
import com.example.su21g3project.Customer.CommunicationCustomer;
import com.example.su21g3project.LoginActivity;
import com.example.su21g3project.NoticeActivity;
import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import adapter.Chef.ChefAdapter;
import model.OrderDetail;
import model.User;

public class MainChefActivity extends AppCompatActivity {
    DatabaseReference reference;
    private RecyclerView recyclerView;
    private List<OrderDetail> list;
    private ChefAdapter chefAdapter;
    private TextView txtChefName;
    FirebaseUser user;
    private String role="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chef);
        user= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.chefRecyclerView);
        txtChefName=findViewById(R.id.txtChefName);

        reference=FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("OrderDetail");
        /**
         * get all OrderDetail with accepted and have't done
         */
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    OrderDetail orderDetail=dataSnapshot.getValue(OrderDetail.class);
                    if (orderDetail.getIsAccepted() && !orderDetail.isDoing()){
                        list.add(orderDetail);
                    }
                }
                chefAdapter=new ChefAdapter(list,getApplicationContext());
                recyclerView.setAdapter(chefAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                startActivity(new Intent(MainChefActivity.this, CommunicationCustomer.class));
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