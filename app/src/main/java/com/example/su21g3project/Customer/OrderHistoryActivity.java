package com.example.su21g3project.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.Order;
import Model.OrderDetail;
import adapter.General.OrderHistoryAdapter;

public class OrderHistoryActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private List<List<OrderDetail>> result = new ArrayList<>();
    private List<OrderDetail> subResult = new ArrayList<>();
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_history);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.lichsugoimon);
        String orderId=getIntent().getStringExtra("orderId");
        reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                result.clear();
                for (DataSnapshot post : snapshot.getChildren()) {
                    if (post.exists()) {
                        OrderDetail orderDetail = post.getValue(OrderDetail.class);
                            String iorderId = orderDetail.getOrderId();
                        if(orderId.equals(iorderId)){
                            Date time = orderDetail.getTime();
                            String status = orderDetail.getStatus();
                            boolean isFoundPlace = false;

                            //go through all exist subResult
                            for (int i = 0; i < result.size(); i++) {
                                //get list contain elements with same orderId
                                subResult = result.get(i);
                                if (isBelong(subResult, time,status)) {
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

                }
                OrderHistoryAdapter adapter = new OrderHistoryAdapter(result);
                RecyclerView view = findViewById(R.id.container_customer_order_history);
                LinearLayoutManager manager = new LinearLayoutManager(OrderHistoryActivity.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                view.setLayoutManager(manager);
                view.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private boolean isBelong(List<OrderDetail> list, Date time, String status) {
        if (!list.isEmpty() && list != null) {
            return list.get(0).getTime().toString().equals(time.toString())&&
                    list.get(0).getStatus().equals(status);
        }
        return false;
    }
}