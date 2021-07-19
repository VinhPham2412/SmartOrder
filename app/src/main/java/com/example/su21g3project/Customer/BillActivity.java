package com.example.su21g3project.Customer;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import adapter.Customer.BillAdapter;
import model.Buffet;
import model.OrderDetail;
import model.Table;
import model.User;

;

public class BillActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private TextView txtTableName,billBuffetName,billBuffetNumPeople,billBuffetPrice,billBuffetTotal,txtTotal;
    private RecyclerView recyclerView;
    private BillAdapter billAdapter;
    private Button btnConfirmBill;
    private List<OrderDetail> orderDetailList;
    FirebaseUser user;
    private int dvWidth;
    private Float finalMoney=0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dvWidth = displayMetrics.widthPixels;
        btnConfirmBill=findViewById(R.id.btnConfirmBill);
        billBuffetName=findViewById(R.id.billBuffetName);
        billBuffetNumPeople=findViewById(R.id.billBuffetNumpeople);
        billBuffetPrice=findViewById(R.id.billBuffetPrice);
        billBuffetTotal=findViewById(R.id.billBuffetTotal);
        txtTotal = findViewById(R.id.txtTotalMoney);
        orderDetailList = new ArrayList<>();
        recyclerView = findViewById(R.id.billRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String orderId = getIntent().getStringExtra("orderId");
        String tableId = getIntent().getStringExtra("tableId");
        String buffetId=getIntent().getStringExtra("buffetId");
        int numPeople=getIntent().getIntExtra("numPeople",0);
        txtTableName = findViewById(R.id.txtTableName);

        //get table name
        reference = FirebaseDatabase.getInstance().getReference("Table").child(tableId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Table table = snapshot.getValue(Table.class);
                txtTableName.setText("Bàn "+table.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //display detail
        reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderDetailList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderDetail orderDetail = dataSnapshot.getValue(OrderDetail.class);
                    if (orderDetail.getIsAccepted() &&
                        !orderDetail.isInBuffet() &&
                         orderDetail.getOrderId().equals(orderId))
                    {
                        orderDetailList.add(orderDetail);
                    }
                }
                for(int i=0;i<orderDetailList.size()-1;i++){
                    for(int j=i+1;j<orderDetailList.size();j++){
                        if(orderDetailList.get(i).getFoodId()
                                .equals(orderDetailList.get(j).getFoodId())){
                            int oldQuantity = orderDetailList.get(i).getQuantity();
                            int alphaQuantity = orderDetailList.get(j).getQuantity();
                            orderDetailList.get(i).setQuantity(oldQuantity+alphaQuantity);
                            orderDetailList.remove(j);
                        }
                    }
                }
                billAdapter = new BillAdapter(orderDetailList,getApplicationContext());
                recyclerView.setAdapter(billAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference=FirebaseDatabase.getInstance().getReference("Buffet").child(buffetId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Buffet buffet=snapshot.getValue(Buffet.class);
                    billBuffetName.setWidth(dvWidth/4);
                    billBuffetName.setText(buffet.getName());
                    billBuffetNumPeople.setWidth(dvWidth/4);
                    billBuffetNumPeople.setText(numPeople+"");
                    billBuffetPrice.setWidth(dvWidth/4);
                    billBuffetPrice.setText((int)buffet.getPrice()+"");
                    billBuffetTotal.setWidth(dvWidth/4);
                    Float buffetSum = buffet.getPrice()*numPeople;
                    billBuffetTotal.setText(buffetSum+"");
                    reference = FirebaseDatabase.getInstance().getReference("SubTotal").child(orderId);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Long finalSum = snapshot.getValue(Long.class);
                                finalMoney=finalSum+buffetSum;
                                txtTotal.setText("Tổng : "+finalMoney);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user=snapshot.getValue(User.class);
                    if (user.getRole().equals("waiter")){
                        btnConfirmBill.setVisibility(View.VISIBLE);
                    }else
                        btnConfirmBill.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnConfirmBill.setOnClickListener(v -> {
            reference = FirebaseDatabase.getInstance().getReference("ProcessOrder").child(orderId);
            reference.child("status").setValue("done");
            reference = FirebaseDatabase.getInstance().getReference("Table").child(tableId);
            reference.child("status").setValue(true);
            Toast.makeText(getApplicationContext(),"Thanh toán thành công",Toast.LENGTH_SHORT).show();
        });
    }
}