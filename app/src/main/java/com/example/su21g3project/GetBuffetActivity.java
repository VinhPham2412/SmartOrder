package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import adapter.BuffetAdapter;
import adapter.OrdersFoodMoneyAdapter;
import model.Buffet;
import model.Food;

public class GetBuffetActivity extends AppCompatActivity {
    private RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();
    private RecyclerView recyclerView;
    private List<Buffet> list;
    private List<Food> foodList;
    private Button btnConfirm;
    private BuffetAdapter buffetAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_buffet);
        recyclerView=findViewById(R.id.recycleView);
        btnConfirm=findViewById(R.id.btnConfirm);
        list=new ArrayList<>();
        //get all buffet
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Buffet");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.add(snapshot.getValue(Buffet.class));
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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
    }

    private void ShowToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}