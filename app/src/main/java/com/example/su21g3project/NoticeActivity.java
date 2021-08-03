package com.example.su21g3project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.NoticeAdapter;
import model.Notice;

public class NoticeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private List<Notice> noticeList;
    private NoticeAdapter noticeAdapter;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        user= FirebaseAuth.getInstance().getCurrentUser();
        String role=getIntent().getStringExtra("role");
        noticeList=new ArrayList<>();
        recyclerView=findViewById(R.id.noticeRecyeceView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reference= FirebaseDatabase.getInstance().getReference("Communication").child(role);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noticeList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Notice notice=dataSnapshot.getValue(Notice.class);
                    if (!notice.getIsSeen()&& notice.getUserId().equals(user.getUid())){
                        noticeList.add(notice);
                    }
                }
                noticeAdapter=new NoticeAdapter(noticeList,getApplicationContext(),role);
                recyclerView.setAdapter(noticeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}