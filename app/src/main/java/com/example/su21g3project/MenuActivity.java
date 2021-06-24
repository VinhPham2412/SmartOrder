package com.example.su21g3project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import adapter.MenuAdapter;
import dao.BuffetDAO;
import model.Buffet;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<Buffet> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        list=new ArrayList<>();
        list=new BuffetDAO().getAllBuffets();
        recyclerView=findViewById(R.id.recycleView);
        menuAdapter=new MenuAdapter(list,this);
        recyclerView.setAdapter(menuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}