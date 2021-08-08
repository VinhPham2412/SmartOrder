package com.example.su21g3project.General;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.Customer.OrdersFoodActivity;
import com.example.su21g3project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import Model.Buffet;
import adapter.Customer.BuffetAdapter;

/**
 * Activity for choosing buffet
 * Get buffet from rtdb and innit BuffetAdapter using for a RecyclerView
 */
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
        recyclerView = findViewById(R.id.billRecycleView);
        btnConfirm = findViewById(R.id.btnConfirm);
        list = new ArrayList<>();
        //get all buffet from rtdb
        reference = FirebaseDatabase.getInstance().getReference("Buffets");
        reference.addValueEventListener(new ValueEventListener() {
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
                reference = FirebaseDatabase.getInstance().getReference("Orders")
                        .child(orderId).child("buffetId");
                reference.setValue(buffet.getId());
                Intent intent = new Intent(GetBuffetActivity.this, OrdersFoodActivity.class);
                intent.putExtra("orderId", getIntent().getStringExtra("orderId"));
                intent.putExtra("buffetId",buffet.getId());
                startActivity(intent);
                finish();
            } else {
                ShowToast("No buffet is choose!");
            }
        });
    }

    private void ShowToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}