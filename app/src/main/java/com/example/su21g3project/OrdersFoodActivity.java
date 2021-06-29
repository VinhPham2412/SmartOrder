package com.example.su21g3project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.MenuAdapter;
import adapter.OrdersFoodAdapter;
import adapter.OrdersFoodMoneyAdapter;
import dao.BuffetDAO;
import dao.FoodDAO;
import model.Buffet;
import model.Food;

public class OrdersFoodActivity extends AppCompatActivity {
    private RecyclerView recyclerView,recycler1;
    private OrdersFoodAdapter ordersFoodAdapter;
    private OrdersFoodMoneyAdapter ordersFoodMoneyAdapter;
    private List<Food> list;
    private List<Food> foodListMoney;
    private TextView buffetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_food);
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


    }
}