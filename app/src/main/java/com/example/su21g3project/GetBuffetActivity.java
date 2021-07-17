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

import adapter.Customer.BuffetAdapter;
import model.Buffet;

public class GetBuffetActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Buffet> list;
    private Button btnConfirm;
    private BuffetAdapter buffetAdapter;
    private  DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_buffet);
        String orderId = getIntent().getStringExtra("orderId");
        recyclerView = findViewById(R.id.recycleView);
        btnConfirm = findViewById(R.id.btnConfirm);
        list = new ArrayList<>();
        //get all buffet
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Buffet");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.exists()) {
                        list.add(snapshot1.getValue(Buffet.class));
                    }
                }
                buffetAdapter = new BuffetAdapter(getApplicationContext(), list);
                recyclerView.setAdapter(buffetAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(GetBuffetActivity.this));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        btnConfirm.setOnClickListener(v -> {
            if (buffetAdapter.getSelected() != null) {
                Buffet buffet = buffetAdapter.getSelected();
                //insert buffet id to order
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ProcessOrder").child(orderId).child("buffetId");
                reference2.setValue(buffet.getId());
                Intent intent = new Intent(GetBuffetActivity.this, OrdersFoodActivity.class);
                intent.putExtra("orderId", getIntent().getStringExtra("orderId"));
                intent.putExtra("buffetId",buffet.getId());
                startActivity(intent);
            } else {
                ShowToast("No buffet is choose!");
            }
        });
    }

    private void ShowToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}