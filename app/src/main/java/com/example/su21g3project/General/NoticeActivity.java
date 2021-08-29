package com.example.su21g3project.General;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

import Model.Notice;
import adapter.NoticeAdapter;

public class NoticeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private List<Notice> noticeList;
    private NoticeAdapter noticeAdapter;
    private FirebaseUser user;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.chefNotice);
        setContentView(R.layout.custom_recylerview);
        user= FirebaseAuth.getInstance().getCurrentUser();
        String role=getIntent().getStringExtra("role");
        noticeList=new ArrayList<>();
        recyclerView=findViewById(R.id.container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reference= FirebaseDatabase.getInstance().getReference("Communications").child(role);
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