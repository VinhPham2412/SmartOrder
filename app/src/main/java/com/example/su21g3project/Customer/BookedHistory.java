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

import adapter.Customer.BookedHistoryAdapter;
import model.ProcessOrder;

public class BookedHistory extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseUser user;
    private List<ProcessOrder> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booked_history);
        user = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<>();
        RecyclerView view = findViewById(R.id.container_customer_booked_history);
        view.setLayoutManager(new LinearLayoutManager(this));
//        SharedPreferences pref = getSharedPreferences("main", Context.MODE_PRIVATE);
//        int listSize = pref.getInt("processOrderList", 0);
//        if (listSize > 0) {
//            reference = FirebaseDatabase.getInstance().getReference("ProcessOrder");
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    list.clear();
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        ProcessOrder processOrder = dataSnapshot.getValue(ProcessOrder.class);
//                        if ((processOrder.getStatus().equals("confirmed") && processOrder.getUserId().equals(user.getUid())) ||
//                                (processOrder.getStatus().equals("rejected") && processOrder.getUserId().equals(user.getUid()))) {
//                            list.add(processOrder);
//                        }
//
//                    }
//                    BookedHistoryAdapter adapter = new BookedHistoryAdapter(list, BookedHistory.this);
//                    view.setAdapter(adapter);
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        } else {
//
        reference = FirebaseDatabase.getInstance().getReference("ProcessOrder");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProcessOrder processOrder = dataSnapshot.getValue(ProcessOrder.class);
                    if (processOrder.getUserId().equals(user.getUid())) {
                        list.add(processOrder);
                    }

                }
                BookedHistoryAdapter adapter = new BookedHistoryAdapter(list, BookedHistory.this);
                view.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}