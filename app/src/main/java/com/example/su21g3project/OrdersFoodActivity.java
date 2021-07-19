package com.example.su21g3project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.Customer.CommunicationCustomer;
import com.example.su21g3project.Customer.OrderHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import adapter.Customer.OrdersFoodAdapter;
import model.Buffet;
import model.Food;
import model.OrderDetail;
import model.User;

public class OrdersFoodActivity extends AppCompatActivity {
    private RecyclerView recyclerView, recycler1;
    private OrdersFoodAdapter inBuffetAdapter, outBuffetAdapter;
    private List<Food> list;
    private List<Food> foodListMoney;
    private TextView buffetName;
    private Button btnOrder, btnC, btnHistory;
    private FirebaseUser user;
    private String userId;
    private ImageButton btnNuocngot, btnRuou, btnDoan;
    private String role;
    private DatabaseReference reference;

    private boolean isIn(List<Food> list, String id) {
        for (Food f : list) {
            if (f.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    void getFoodsByCategory(String type) {
        List<Food> result = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Food");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.exists()) {
                        Food food = snapshot1.getValue(Food.class);
                        if (food.getType().equals(type) &&
                                !isIn(list, food.getId())) {
                            result.add(food);
                        }
                    }
                }
                outBuffetAdapter = new OrdersFoodAdapter(getApplicationContext(), result, true);
                recycler1.setAdapter(outBuffetAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_food);
        String orderId = getIntent().getStringExtra("orderId");
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.billRecycleView);
        btnHistory = findViewById(R.id.btnHistory);
        btnNuocngot = findViewById(R.id.nuocngot);
        btnRuou = findViewById(R.id.ruou);
        btnDoan = findViewById(R.id.doan);
        btnOrder = findViewById(R.id.btnOrder);
        btnC = findViewById(R.id.btnCommunication);
        role = "customer";

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(OrdersFoodActivity.this, OrderHistory.class);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
        });

        if (user != null) {
            btnOrder.setVisibility(View.INVISIBLE);
            userId = user.getUid();
            reference = FirebaseDatabase.getInstance().getReference("User");
            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    role = snapshot.getValue(User.class).getRole();
                    btnOrder.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        String buffetId = getIntent().getStringExtra("buffetId");
        list = new ArrayList<>();
        //get food in buffet
        reference = FirebaseDatabase.getInstance().getReference("Buffet");
        reference.child(buffetId).child("foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.exists()) {
                        list.add(snapshot1.getValue(Food.class));
                    }
                }
                inBuffetAdapter = new OrdersFoodAdapter(getApplicationContext(), list,false);
                recyclerView.setAdapter(inBuffetAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Buffet").child(buffetId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Buffet buffet = snapshot.getValue(Buffet.class);
                buffetName = findViewById(R.id.buffetName);
                buffetName.setText(buffet.getName() + " " + (int) (buffet.getPrice()) + "K");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        recycler1 = findViewById(R.id.recycleViewMoney);
        //get food out buffet
        reference = FirebaseDatabase.getInstance().getReference("Food");
        foodListMoney = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                foodListMoney.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Food food = snapshot1.getValue(Food.class);
                    if (!isIn(list, food.getId())) {
                        foodListMoney.add(food);
                    }
                }
                outBuffetAdapter = new OrdersFoodAdapter(getApplicationContext(), foodListMoney,true);
                recycler1.setAdapter(outBuffetAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        recycler1.setLayoutManager(manager1);
        btnNuocngot.setOnClickListener(v -> {
            getFoodsByCategory("soft drink");

        });
        btnRuou.setOnClickListener(v -> {
            getFoodsByCategory("wine");

        });
        btnDoan.setOnClickListener(v -> {
            getFoodsByCategory("food");

        });
        btnOrder.setOnClickListener(v -> {
            //get data and make new detail
            //insert into rtdb
            reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
            Date time = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00")).getTime();
            //get food and quantity from food in buffet
            List<OrdersFoodAdapter.ViewHolder> allInHolder = inBuffetAdapter.getAllHolder();
            List<OrdersFoodAdapter.ViewHolder> allOutHolder = outBuffetAdapter.getAllHolder();
            if(allInHolder.size()>0){
                for (OrdersFoodAdapter.ViewHolder food : allInHolder) {
                    int quantity = Integer.parseInt(food.getNumberFood().getText().toString());
                    String id = UUID.randomUUID().toString();
                    OrderDetail detail;
                    detail = new OrderDetail(id, orderId, userId, food.getFoodId(), quantity, time, role.equals("waiter") ? true : false, role.equals("waiter") ? true : false, true);
                    reference.child(id).setValue(detail.toMap()).addOnCompleteListener(task ->
                            Log.println(Log.INFO, "addOk", "Add ok"));

                }
            }
            //get food and quantity from food out of buffet
            if(allOutHolder.size()>0){
                for (OrdersFoodAdapter.ViewHolder food : allOutHolder) {
                    int quantity = Integer.parseInt(food.getNumberFood().getText().toString());
                    String id = UUID.randomUUID().toString();
                    OrderDetail detail;
                    detail = new OrderDetail(id, orderId, userId, food.getFoodId(), quantity, time, role.equals("waiter") ? true : false, role.equals("waiter") ? true : false, false);
                    reference.child(id).setValue(detail.toMap()).addOnCompleteListener(task ->
                            Log.println(Log.INFO, "addOk", "Add ok"));
                }
            }
            int count = allInHolder.size()+ allOutHolder.size();
            Toast.makeText(this, "Gọi thành công "+count+" món", Toast.LENGTH_SHORT).show();
        });
        btnC.setOnClickListener(v -> startActivity(new Intent(OrdersFoodActivity.this, CommunicationCustomer.class)));
    }
}