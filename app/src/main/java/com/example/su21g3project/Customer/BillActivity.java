package com.example.su21g3project.Customer;

;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.sax.TemplatesHandler;

import adapter.Customer.BillAdapter;
import model.OrderDetail;
import model.Table;

public class BillActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private TextView txtTableName;
    private RecyclerView recyclerView;
    private BillAdapter billAdapter;
    private List<OrderDetail> orderDetailList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        orderDetailList=new ArrayList<>();
        recyclerView=findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String orderId=getIntent().getStringExtra("orderId");
        txtTableName=findViewById(R.id.txtTableName);
        reference=FirebaseDatabase.getInstance().getReference("OrderDetail");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderDetailList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    OrderDetail orderDetail=dataSnapshot.getValue(OrderDetail.class);
                    if(orderDetail.getIsSeen()&& orderDetail.getIsAccepted() && !orderDetail.isInBuffet()){
                        orderDetailList.add(orderDetail);
                    }
                }
                billAdapter=new BillAdapter(orderDetailList,getApplicationContext());
                recyclerView.setAdapter(billAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void getTableName(String tableId){
        final Table[] table = new Table[1];
       reference=FirebaseDatabase.getInstance().getReference("Table").child(tableId);
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               table[0] =snapshot.getValue(Table.class);
               txtTableName.setText(table[0].getName());
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
}