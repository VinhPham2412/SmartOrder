package com.example.su21g3project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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
    private Button btnOrder;
    private FirebaseUser user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            userId = user.getUid();
        }
        setContentView(R.layout.activity_orders_food);
        btnOrder = findViewById(R.id.btnOrder);
        Intent intent=getIntent();
        Buffet buffet=(Buffet)intent.getSerializableExtra("buffet");
        list=new ArrayList<>();
        list=new FoodDAO().getFoodsOfBuffet(buffet.getId());
        recyclerView=findViewById(R.id.recycleView);
        ordersFoodAdapter=new OrdersFoodAdapter(this,list);
        recyclerView.setAdapter(ordersFoodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recycler1=findViewById(R.id.recycleViewMoney);
        foodListMoney=new FoodDAO().getFoodMoney();
        ordersFoodMoneyAdapter=new OrdersFoodMoneyAdapter(this,foodListMoney);
        recycler1.setAdapter(ordersFoodMoneyAdapter);
        recycler1.setLayoutManager(new LinearLayoutManager(this));
        buffetName=findViewById(R.id.buffetName);
        buffetName.setText(buffet.getName()+" " +String.valueOf(buffet.getPrice()));

        btnOrder.setOnClickListener(v -> {
            //get data and make new detail
            //insert into rtdb
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
            Date time = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00")).getTime();
            //get food and quantity from food in buffet
            for (OrdersFoodAdapter.ViewHolder food : ordersFoodAdapter.getAllHolder()
                 ) {
                int quantity  = Integer.parseInt(food.numberFood.getText().toString());
                if(quantity>0){
                    String id = UUID.randomUUID().toString();
                    OrderDetail detail = new OrderDetail(id,userId,food.foodId,quantity,time);
                    reference.child(id).setValue(detail.toMap()).addOnCompleteListener(task ->
                            Log.println(Log.INFO,"addOk","Add ok"));
                }
            }
            //get food and quantity from food out of buffet
            for (OrdersFoodMoneyAdapter.ViewHolder food : ordersFoodMoneyAdapter.getAllHolder()
            ) {
                int quantity  = Integer.parseInt(food.numberFoodMoney.getText().toString());
                if(quantity>0){
                    String id = UUID.randomUUID().toString();
                    OrderDetail detail = new OrderDetail(id,userId,food.foodId,quantity,time);
                    reference.child(id).setValue(detail.toMap()).addOnCompleteListener(task ->
                            Log.println(Log.INFO,"addOk","Add ok"));
                }
            }
        });


    }
}