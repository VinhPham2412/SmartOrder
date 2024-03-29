package com.example.su21g3project.Customer;

import android.content.Intent;
import android.graphics.Paint;
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

import com.example.su21g3project.General.BillActivity;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import Model.Buffet;
import Model.Food;
import Model.Order;
import Model.OrderDetail;
import Model.User;
import adapter.Customer.OrdersFoodAdapter;

/**
 * Class OrderFoodActivity
 * this class using for select food and order food
 * there are two actor using this class
 * waiter and customer
 */
public class OrdersFoodActivity extends AppCompatActivity {
    private RecyclerView recyclerView, recycler1;
    private OrdersFoodAdapter inBuffetAdapter, outBuffetAdapter;
    private List<Food> list;
    private List<Food> foodListMoney;
    private TextView buffetName,txtHistoryOrder;
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
        reference = FirebaseDatabase.getInstance().getReference("Foods");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.exists()) {
                        Food food = snapshot1.getValue(Food.class);
                        if (food.getType().equals(type) && food.isStatus() &&
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
        txtHistoryOrder=findViewById(R.id.txtHistoryOrder);
        txtHistoryOrder.setPaintFlags(txtHistoryOrder.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String orderId = getIntent().getStringExtra("orderId");
        txtHistoryOrder.setOnClickListener(v -> {
            Intent intent = new Intent(OrdersFoodActivity.this, OrderHistoryActivity.class);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.billRecycleView);
        btnHistory = findViewById(R.id.btnHistory);
        btnNuocngot = findViewById(R.id.nuocngot);
        btnRuou = findViewById(R.id.ruou);
        btnDoan = findViewById(R.id.doan);
        btnOrder = findViewById(R.id.btnOrder);
        btnC = findViewById(R.id.btnCommunication);
        buffetName = findViewById(R.id.buffetName);
        role = "customer";

        btnHistory.setOnClickListener(v -> {
            reference = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Order order = snapshot.getValue(Order.class);
                        Intent intent = new Intent(getApplicationContext(), BillActivity.class);
                        intent.putExtra("orderId", order.getId());
                        intent.putExtra("tableId", order.getTableId());
                        intent.putExtra("buffetId",order.getBuffetId());
                        intent.putExtra("numPeople",order.getNumberOfPeople());
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        });

        if (user != null) {
            btnOrder.setVisibility(View.INVISIBLE);
            userId = user.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    role = snapshot.getValue(User.class).getRole();
                    btnOrder.setVisibility(View.VISIBLE);
                    if (role.equals("waiter")){
                        btnC.setVisibility(View.INVISIBLE);
                        btnHistory.setVisibility(View.INVISIBLE);
                    }else {
                        btnC.setVisibility(View.VISIBLE);
                        btnHistory.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String buffetId = getIntent().getStringExtra("buffetId");
        if(buffetId==null){
            finish();
            Toast.makeText(getParent().getBaseContext(),"Có lỗi xảy ra, vui lòng thử lại",Toast.LENGTH_SHORT).show();
        }
        list = new ArrayList<>();
        //get food in buffet
        reference = FirebaseDatabase.getInstance().getReference("Buffets");
        reference.child(buffetId).child("foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Food food=snapshot1.getValue(Food.class);
                        if (food.isStatus()){
                            list.add(food);
                        }
                    }
                    inBuffetAdapter = new OrdersFoodAdapter(getApplicationContext(), list,false);
                    recyclerView.setAdapter(inBuffetAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Buffets").child(buffetId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Buffet buffet = snapshot.getValue(Buffet.class);
                    buffetName.setText(buffet.getName() + " " + (int) (buffet.getPrice()) + "K");
                }else{
                    buffetName.setText("Lỗi khi tải thông tin buffet");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        recycler1 = findViewById(R.id.recycleViewMoney);
        //get food out buffet
        reference = FirebaseDatabase.getInstance().getReference("Foods");
        foodListMoney = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                foodListMoney.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Food food = snapshot1.getValue(Food.class);
                    if (!isIn(list, food.getId()) && food.isStatus()) {
                        foodListMoney.add(food);
                    }
                }
                outBuffetAdapter = new OrdersFoodAdapter(getApplicationContext(), foodListMoney,true);
                LinearLayoutManager manager1 = new LinearLayoutManager(OrdersFoodActivity.this);
                recycler1.setLayoutManager(manager1);
                recycler1.setAdapter(outBuffetAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

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
            //get food and quantity from food in buffet
            List<OrdersFoodAdapter.ViewHolder> allInHolder = inBuffetAdapter.getAllHolder();
            List<OrdersFoodAdapter.ViewHolder> allOutHolder = outBuffetAdapter.getAllHolder();
            int count = allInHolder.size()+ allOutHolder.size();
            if(count==0){
                Toast.makeText(getApplicationContext(),getString(R.string.toastinvalidorder),
                        Toast.LENGTH_SHORT).show();
            }else{
                //insert into rtdb
                reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
                Date time = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00")).getTime();
                if(allInHolder.size()>0){
                    for (OrdersFoodAdapter.ViewHolder food : allInHolder) {
                        int quantity = Integer.parseInt(food.getNumberFood().getText().toString());
                        String id = reference.push().getKey();
                        OrderDetail detail = new OrderDetail(id, orderId, userId, food.getFoodId(),
                                quantity, time, true, role.equals("waiter") ? "accepted" : "new");
                        reference.child(id).setValue(detail.toMap()).addOnCompleteListener(task ->
                                Log.println(Log.INFO, "addOk", "Add ok"));
                        food.getNumberFood().setText("0");
                    }
                }
                //get food and quantity from food out of buffet
                if(allOutHolder.size()>0){
                    for (OrdersFoodAdapter.ViewHolder food : allOutHolder) {
                        int quantity = Integer.parseInt(food.getNumberFood().getText().toString());
                        String id = reference.push().getKey();
                        OrderDetail detail = new OrderDetail(id, orderId, userId, food.getFoodId(),
                                quantity, time, false, role.equals("waiter") ? "accepted" : "new");
                        reference.child(id).setValue(detail.toMap()).addOnCompleteListener(task ->
                                Log.println(Log.INFO, "addOk", "Add ok"));
                        food.getNumberFood().setText("0");
                    }
                }
                Toast.makeText(this, "Gọi thành công "+count+" món", Toast.LENGTH_SHORT).show();
            }
        });
        btnC.setOnClickListener(v -> startActivity(new Intent(OrdersFoodActivity.this,
                CommunicationActivity.class)));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}