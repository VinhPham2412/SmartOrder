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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.Customer.CommunicationActivity;
import com.example.su21g3project.General.LoginActivity;
import com.example.su21g3project.General.NoticeActivity;
import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.OrderDetail;
import Model.User;
import adapter.Chef.ChefAdapter;

public class MainChefActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private TextView txtChefName;
    FirebaseUser user;
    private String role="";
    private RecyclerView recyclerView;
    private List<List<OrderDetail>> result = new ArrayList<>();
    private List<OrderDetail> subResult = new ArrayList<>();
    private ChefAdapter chefAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chef);
        recyclerView=findViewById(R.id.chefRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        user= FirebaseAuth.getInstance().getCurrentUser();
        txtChefName=findViewById(R.id.chefName);
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

        reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
        reference.orderByChild("orderId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //orderDetails from rtdb
                result.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    OrderDetail orderDetail = postSnapshot.getValue(OrderDetail.class);
                    if(orderDetail.getStatus().equals("accepted")){
                        String orderId = orderDetail.getOrderId();
                        Date time = orderDetail.getTime();
                        boolean isFoundPlace = false;
                        //bellow code is separate result by time and orderId
                        //go through all exist subResult
                        for (int i = 0; i < result.size(); i++) {

                            //get list contain elements with same orderId
                            subResult = result.get(i);
                            if (isBelong(subResult, orderId, time)) {
                                subResult.add(orderDetail);
                                isFoundPlace = true;
                            }
                        }
                        //if not found any existed list belong to then make new list
                        if (!isFoundPlace) {
                            subResult = new ArrayList<>();
                            subResult.add(orderDetail);
                            //add new list to result
                            result.add(subResult);
                        }
                    }

                }
                chefAdapter = new ChefAdapter(result, MainChefActivity.this);
                recyclerView.setAdapter(chefAdapter);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

    }
    //belong is same orderId and time
    private boolean isBelong(List<OrderDetail> list, String orderId, Date time) {
        if (!list.isEmpty() && list.get(0) != null) {
            return list.get(0).getOrderId().equals(orderId)
                    && list.get(0).getTime().equals(time);
        }
        return false;
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