package com.example.su21g3project.Customer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import Model.Order;
import Model.OrderDetail;
import adapter.General.OrderHistoryAdapter;

public class OrderHistoryActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseUser user;
    private List<List<OrderDetail>> result = new ArrayList<>();
    private List<OrderDetail> subResult = new ArrayList<>();
    private Button btnBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_history);
        String orderId=getIntent().getStringExtra("orderId");
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
        reference.orderByChild("orderId").equalTo(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                result.clear();
                for (DataSnapshot post : snapshot.getChildren()) {
                    if (post.exists()) {
                        OrderDetail orderDetail = post.getValue(OrderDetail.class);
                        //if same user
                        assert orderDetail != null;
                        orderDetail.getUserId().equals(user.getUid());
                            String orderId = orderDetail.getOrderId();
                            Date time = orderDetail.getTime();
                            boolean isFoundPlace = false;

                            //go through all exist subResult
                            for (int i = 0; i < result.size(); i++) {
                                //get list contain elements with same orderId
                                subResult = result.get(i);
                                if (isBelong(subResult, time)) {
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
        btnBill=findViewById(R.id.btnBill);
        btnBill.setOnClickListener(v -> {
            reference = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Order processOrder = snapshot.getValue(Order.class);
                        Intent intent = new Intent(getApplicationContext(), BillActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("tableId", processOrder.getTableId());
                        intent.putExtra("buffetId",processOrder.getBuffetId());
                        intent.putExtra("numPeople",processOrder.getNumberOfPeople());
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        });
    }



    private boolean isBelong(List<OrderDetail> list,  Date time) {
        if (!list.isEmpty() && list != null) {
            return list.get(0).getTime().toString().equals(time.toString());
        }
        return false;
    }
}