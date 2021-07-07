package com.example.su21g3project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.BuffetAdapter;
import adapter.OrdersFoodMoneyAdapter;
import dao.BuffetDAO;
import dao.FoodDAO;
import model.Buffet;
import model.Food;

public class GetBuffetActivity extends AppCompatActivity {
    private RecyclerView recyclerView,recyclerViewMoney;
    private List<Buffet> list;
    private List<Food> foodList;
    private Button btnConfirm;
    private BuffetAdapter buffetAdapter;
    private OrdersFoodMoneyAdapter ordersFoodMoneyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_buffet);
        recyclerView=findViewById(R.id.recycleView);
        recyclerViewMoney=findViewById(R.id.recycleViewMoney);
        btnConfirm=findViewById(R.id.btnConfirm);
        list=new ArrayList<>();
        list=new BuffetDAO().getAllBuffets();
        buffetAdapter=new BuffetAdapter(this,list);
        recyclerView.setAdapter(buffetAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnConfirm.setOnClickListener(v -> {
            if (buffetAdapter.getSelected()!=null){
                Buffet buffet=buffetAdapter.getSelected();
                Intent intent =new Intent(GetBuffetActivity.this,OrdersFoodActivity.class);
                intent.putExtra("buffet",buffet);
                intent.putExtra("orderId",getIntent().getStringExtra("orderId"));
                startActivity(intent);
            }
            else{
                ShowToast("No buffet is choose!");
            }
        });
        foodList=new ArrayList<>();
        foodList=new FoodDAO().getFoodMoney();
        ordersFoodMoneyAdapter=new OrdersFoodMoneyAdapter(this,foodList);
        recyclerViewMoney.setAdapter(ordersFoodMoneyAdapter);
        recyclerViewMoney.setLayoutManager(new LinearLayoutManager(this));


    }

    private void ShowToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}