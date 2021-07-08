package com.example.su21g3project.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
import java.util.List;

import adapter.BookedHistoryAdapter;
import model.ProcessOrder;

public class BookedHistory extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseUser user;
    List<ProcessOrder> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booked_history);
        user = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("ProcessOrder");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot post: snapshot.getChildren()){
                    if(post.exists()){
                        ProcessOrder processOrder = post.getValue(ProcessOrder.class);
                        if(processOrder.getUserId().equals(user.getUid())){
                            list.add(processOrder);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        BookedHistoryAdapter adapter = new BookedHistoryAdapter(list);
        RecyclerView view = findViewById(R.id.container_customer_booked_history);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setLayoutManager(manager);
        view.setAdapter(adapter);
    }
}