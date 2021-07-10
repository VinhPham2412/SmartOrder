package com.example.su21g3project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import adapter.OrdersFoodAdapter;
import adapter.OrdersFoodMoneyAdapter;
import dao.FoodDAO;
import model.Buffet;
import model.Food;
import model.OrderDetail;

public class OrdersFoodActivity extends AppCompatActivity {
    private RecyclerView recyclerView,recycler1;
    private OrdersFoodAdapter ordersFoodAdapter;
    private OrdersFoodMoneyAdapter ordersFoodMoneyAdapter;
    private List<Food> list;
    private List<Food> foodListMoney;
    private TextView buffetName;
    private Button btnOrder,btnC;
    private FirebaseUser user;
    private String userId;
    private ImageButton btnNuocngot,btnRuou,btnDoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            userId = user.getUid();
        }
        setContentView(R.layout.activity_orders_food);
        btnNuocngot=findViewById(R.id.nuocngot);
        btnRuou=findViewById(R.id.ruou);
        btnDoan=findViewById(R.id.doan);


        btnOrder = findViewById(R.id.btnOrder);
        Intent intent=getIntent();
        Buffet buffet=(Buffet)intent.getSerializableExtra("buffet");
        list=new ArrayList<>();
        list=new FoodDAO().getFoodsOfBuffet(buffet.getId());
        recyclerView=findViewById(R.id.recycleView);
        ordersFoodAdapter=new OrdersFoodAdapter(this,list);
        recyclerView.setAdapter(ordersFoodAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setInitialPrefetchItemCount(4);
        recyclerView.setLayoutManager(manager);
        recycler1=findViewById(R.id.recycleViewMoney);
        foodListMoney=new FoodDAO().getFoodMoney();
        ordersFoodMoneyAdapter=new OrdersFoodMoneyAdapter(this,foodListMoney);
        recycler1.setAdapter(ordersFoodMoneyAdapter);
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setInitialPrefetchItemCount(4);
        recycler1.setLayoutManager(manager1);
        buffetName=findViewById(R.id.buffetName);
        buffetName.setText(buffet.getName()+" " +(int)(buffet.getPrice())+"K");
        btnNuocngot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodListMoney=new FoodDAO().getFoodMoneyByCategory(3);
                ordersFoodMoneyAdapter=new OrdersFoodMoneyAdapter(getApplicationContext(),foodListMoney);
                recycler1.setAdapter(ordersFoodMoneyAdapter);
            }
        });
        btnRuou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodListMoney=new FoodDAO().getFoodMoneyByCategory(4);
                ordersFoodMoneyAdapter=new OrdersFoodMoneyAdapter(getApplicationContext(),foodListMoney);
                recycler1.setAdapter(ordersFoodMoneyAdapter);
            }
        });
        btnDoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodListMoney=new FoodDAO().getFoodMoneyByCategory(5);
                ordersFoodMoneyAdapter=new OrdersFoodMoneyAdapter(getApplicationContext(),foodListMoney);
                recycler1.setAdapter(ordersFoodMoneyAdapter);
            }
        });
        btnOrder.setOnClickListener(v -> {
            //get data and make new detail
            //insert into rtdb
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
            Date time = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00")).getTime();
            // get orderId
            String orderId = getIntent().getStringExtra("orderId");
            //get food and quantity from food in buffet
            for (OrdersFoodAdapter.ViewHolder food : ordersFoodAdapter.getAllHolder()) {
                int quantity  = Integer.parseInt(food.numberFood.getText().toString());
                if(quantity>0){
                    String id = UUID.randomUUID().toString();
                    OrderDetail detail = new OrderDetail(id,orderId,userId,food.foodId,quantity,time,false,false,false);
                    reference.child(id).setValue(detail.toMap()).addOnCompleteListener(task ->
                            Log.println(Log.INFO,"addOk","Add ok"));
                }
            }
            //get food and quantity from food out of buffet
            for (OrdersFoodMoneyAdapter.ViewHolder food : ordersFoodMoneyAdapter.getAllHolder()) {
                int quantity  = Integer.parseInt(food.numberFoodMoney.getText().toString());
                if(quantity>0){
                    String id = UUID.randomUUID().toString();
                    OrderDetail detail = new OrderDetail(id,orderId,userId,food.foodId,quantity,time,false,false,true);
                    reference.child(id).setValue(detail.toMap()).addOnCompleteListener(task ->
                            Log.println(Log.INFO,"addOk","Add ok"));
                }
            }
            Toast.makeText(this,"Gọi món thành công",Toast.LENGTH_SHORT).show();
        });
        btnC=findViewById(R.id.btnCommunication);
        btnC.setOnClickListener(v -> startActivity(new Intent(OrdersFoodActivity.this,CommunicationCustomer.class)));

    }
}