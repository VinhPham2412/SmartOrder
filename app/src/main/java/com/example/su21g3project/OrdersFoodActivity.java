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

import adapter.OrdersFoodAdapter;
import adapter.OrdersFoodMoneyAdapter;
import model.Buffet;
import model.Food;
import model.OrderDetail;

public class OrdersFoodActivity extends AppCompatActivity {
    private RecyclerView recyclerView, recycler1;
    private OrdersFoodAdapter ordersFoodAdapter;
    private OrdersFoodMoneyAdapter ordersFoodMoneyAdapter;
    private List<Food> list;
    private List<Food> foodListMoney;
    private TextView buffetName;
    private Button btnOrder, btnC;
    private FirebaseUser user;
    private String userId;
    private ImageButton btnNuocngot, btnRuou, btnDoan;

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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Menu");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.exists()) {
                        Food food = snapshot1.getValue(Food.class);
                        if (food.getType().equals(type)) {
                            result.add(food);
                        }
                    }
                }
                ordersFoodMoneyAdapter = new OrdersFoodMoneyAdapter(getApplicationContext(), result);
                recycler1.setAdapter(ordersFoodMoneyAdapter);
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
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.recycleView);

        if (user != null) {
            userId = user.getUid();
        }

        btnNuocngot = findViewById(R.id.nuocngot);
        btnRuou = findViewById(R.id.ruou);
        btnDoan = findViewById(R.id.doan);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnOrder = findViewById(R.id.btnOrder);
        Intent intent = getIntent();
        Buffet buffet = (Buffet) intent.getSerializableExtra("buffet");
        list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Buffet");
        reference.child(buffet.getId()).child("foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if (snapshot1.exists()) {
                        list.add(snapshot1.getValue(Food.class));
                    }
                }
                ordersFoodAdapter = new OrdersFoodAdapter(getApplicationContext(), list);
                recyclerView.setAdapter(ordersFoodAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        recycler1 = findViewById(R.id.recycleViewMoney);
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Menu");
        foodListMoney = new ArrayList<>();
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Food food = snapshot1.getValue(Food.class);
                    if (!isIn(list, food.getId())) {
                        foodListMoney.add(food);
                    }
                }
                ordersFoodMoneyAdapter = new OrdersFoodMoneyAdapter(getApplicationContext(), foodListMoney);
                recycler1.setAdapter(ordersFoodMoneyAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setInitialPrefetchItemCount(4);
        recycler1.setLayoutManager(manager1);
        buffetName = findViewById(R.id.buffetName);
        buffetName.setText(buffet.getName() + " " + (int) (buffet.getPrice()) + "K");
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
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("OrderDetail");
            Date time = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00")).getTime();
            // get orderId
            String orderId = getIntent().getStringExtra("orderId");
            //get food and quantity from food in buffet
            for (OrdersFoodAdapter.ViewHolder food : ordersFoodAdapter.getAllHolder()) {
                int quantity = Integer.parseInt(food.numberFood.getText().toString());
                if (quantity > 0) {
                    String id = UUID.randomUUID().toString();
                    OrderDetail detail = new OrderDetail(id, orderId, userId, food.foodId, quantity, time, false, false, false);
                    reference2.child(id).setValue(detail.toMap()).addOnCompleteListener(task ->
                            Log.println(Log.INFO, "addOk", "Add ok"));
                }
            }
            //get food and quantity from food out of buffet
            for (OrdersFoodMoneyAdapter.ViewHolder food : ordersFoodMoneyAdapter.getAllHolder()) {
                int quantity = Integer.parseInt(food.numberFoodMoney.getText().toString());
                if (quantity > 0) {
                    String id = UUID.randomUUID().toString();
                    OrderDetail detail = new OrderDetail(id, orderId, userId, food.foodId, quantity, time, false, false, true);
                    reference2.child(id).setValue(detail.toMap()).addOnCompleteListener(task ->
                            Log.println(Log.INFO, "addOk", "Add ok"));
                }
            }
            Toast.makeText(this, "Gọi món thành công", Toast.LENGTH_SHORT).show();
        });
        btnC = findViewById(R.id.btnCommunication);
        btnC.setOnClickListener(v -> startActivity(new Intent(OrdersFoodActivity.this, CommunicationCustomer.class)));

    }
}