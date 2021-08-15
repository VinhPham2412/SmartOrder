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

import Model.Notice;
import Model.Order;
import adapter.NoticeAdapter;
import adapter.Waiter.BookedHistoryAdapter;

public class NoticeCustomerActivity extends AppCompatActivity {
    private RecyclerView tableRe,noticeRe;
    private DatabaseReference reference;
    private List<Order> processOrderList;
    private FirebaseUser user;
    private List<Notice> noticeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_customer);
        processOrderList=new ArrayList<>();
        noticeList=new ArrayList<>();
        user= FirebaseAuth.getInstance().getCurrentUser();
        tableRe=findViewById(R.id.tableRecyclerView);
        noticeRe=findViewById(R.id.recycleViewNotice);
        tableRe.setLayoutManager(new LinearLayoutManager(this));
        noticeRe.setLayoutManager(new LinearLayoutManager(this));
        reference = FirebaseDatabase.getInstance().getReference("Orders");
        /**
         * get all ProcessOrder of currentUser
         */
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                processOrderList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order processOrder = dataSnapshot.getValue(Order.class);
                    if (processOrder.getUserId().equals(user.getUid())
                            && processOrder.getStatus().equals("accepted")) {
                        processOrderList.add(processOrder);
                    }

                }
                //show list processOrder by apdater
                BookedHistoryAdapter adapter = new BookedHistoryAdapter(processOrderList);
                tableRe.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference=FirebaseDatabase.getInstance().getReference("Communications").child("customer");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noticeList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Notice notice=dataSnapshot.getValue(Notice.class);
                    if (!notice.getIsSeen() && notice.getUserId().equals(user.getUid())){
                        noticeList.add(notice);
                    }
                }
                NoticeAdapter noticeAdapter=new NoticeAdapter(noticeList,NoticeCustomerActivity.this,"customer");
                noticeRe.setAdapter(noticeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}