package com.example.su21g3project.Customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.Order;
import adapter.Waiter.BookedHistoryAdapter;

public class BookedHistoryActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseUser user;
    private List<Order> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_recylerview);
        user = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<>();
        RecyclerView view = findViewById(R.id.container);
        view.setLayoutManager(new LinearLayoutManager(this));
        reference = FirebaseDatabase.getInstance().getReference("Orders");
        /**
         * get all ProcessOrder of currentUser
         */
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order processOrder = dataSnapshot.getValue(Order.class);
                    if (processOrder.getUserId().equals(user.getUid())) {
                        list.add(processOrder);
                    }

                }
                //show list processOrder by apdater
                BookedHistoryAdapter adapter = new BookedHistoryAdapter(list, BookedHistoryActivity.this);
                view.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}