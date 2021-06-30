package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.NotificationAdapter;
import model.Notification;

public class NotificationActivity extends AppCompatActivity {
    private List<Notification> notificationList;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView=findViewById(R.id.recycleViewNotice);
        notificationList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Notifications");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    Notification notification=dataSnapshot.getValue(Notification.class);
                    if(notification.getReceiverId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && !notification.isSeen()){
                        notificationList.add(notification);
                    }
                    notificationAdapter=new NotificationAdapter(NotificationActivity.this,notificationList);
                    recyclerView.setAdapter(notificationAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}