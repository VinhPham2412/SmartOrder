package com.example.su21g3project.General;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import Model.Bill;
import Model.Buffet;
import Model.Order;
import Model.OrderDetail;
import Model.Table;
import Model.User;
import adapter.Customer.BillAdapter;

public class BillActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private TextView txtTableName,billBuffetName,billBuffetNumPeople,billBuffetPrice,billBuffetTotal,txtTotal;
    private RecyclerView recyclerView;
    private BillAdapter billAdapter;
    private Button btnConfirmBill;
    private List<OrderDetail> orderDetailList;
    private  FirebaseUser user;
    private Float finalMoney=0f;
    private String role="customer";
    private String billId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        user= FirebaseAuth.getInstance().getCurrentUser();
        //get from intent
        String orderId = getIntent().getStringExtra("orderId");
        String tableId = getIntent().getStringExtra("tableId");
        String buffetId=getIntent().getStringExtra("buffetId");
        billId = getIntent().getStringExtra("billId");
        int numPeople=getIntent().getIntExtra("numPeople",0);


        btnConfirmBill=findViewById(R.id.btnConfirmBill);
        billBuffetName=findViewById(R.id.billBuffetName);
        billBuffetNumPeople=findViewById(R.id.billBuffetNumpeople);
        billBuffetPrice=findViewById(R.id.billBuffetPrice);
        billBuffetTotal=findViewById(R.id.billBuffetTotal);
        txtTotal = findViewById(R.id.txtTotalMoney);
        orderDetailList = new ArrayList<>();
        recyclerView = findViewById(R.id.billRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        txtTableName = findViewById(R.id.txtTableName);
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user1 = snapshot.getValue(User.class);
                    role = user1.getRole();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference=FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Order order=snapshot.getValue(Order.class);
                    if ((order.getStatus().equals("done") || order.getStatus().equals("requestToPay")
                            ||order.getStatus().equals("readytopay")) && role.equals("customer")){
                        btnConfirmBill.setVisibility(View.INVISIBLE);
                    }else
                        btnConfirmBill.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /**
         * get tableName
         */
        if(tableId==null){
            txtTableName.setText("Chưa xếp bàn.");
        }else{
            reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Table table = snapshot.getValue(Table.class);
                    txtTableName.setText(table.getName());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        /**
         * Display all orderDetail in recyclerView
         */
        reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
        reference.orderByChild("status").equalTo("delivered").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderDetailList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderDetail orderDetail = dataSnapshot.getValue(OrderDetail.class);
                    if (!orderDetail.getIsInBuffet() &&
                         orderDetail.getOrderId().equals(orderId))
                    {
                        orderDetailList.add(orderDetail);
                    }
                }
                billAdapter = new BillAdapter(orderDetailList,getApplicationContext(),role);
                recyclerView.setAdapter(billAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        /**
         * action when click confirmBill
         * if role = customer then insert new bill
         * if role = waiter:
         * get updated detail from view
         * if bill exist then update
         * else insert new bill (case customer not using app)
         */
        btnConfirmBill.setOnClickListener(v -> {
            //get bill id if exist, create if not
            if (role.equals("customer")){
                //update order status
                billId = FirebaseDatabase.getInstance().getReference("Bills").push().getKey();
                reference = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
                reference.child("status").setValue("requestToPay");
                processBill(orderId,tableId,buffetId,billId);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(BillActivity.this);
                builder1.setTitle(BillActivity.this.getString(R.string.waitforwaiter));
                builder1.setNegativeButton(R.string.done, ((dialog1, which1) -> {
                    dialog1.cancel();
                    Intent intent = new Intent(BillActivity.this, MainActivity.class);
                    startActivity(intent);
                })).create().show();
            }else{
                //if billId == null mean customer not use app
                if(billId==null) {
                    billId = FirebaseDatabase.getInstance().getReference("Bills").push().getKey();
                }
                // update detail after edit
                updateDetails(orderId,tableId,billAdapter.getDetails());
                // process bill
                processBill(orderId,tableId,buffetId,billId);
                Toast.makeText(getApplicationContext(),getApplicationContext()
                        .getString(R.string.toastBill),Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        /**
         * Display buffet info and final sum
         */
        if(buffetId!=null){
            reference=FirebaseDatabase.getInstance().getReference("Buffets").child(buffetId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Buffet buffet=snapshot.getValue(Buffet.class);
                        billBuffetName.setText(buffet.getName());
                        billBuffetNumPeople.setText(numPeople+"");
                        billBuffetPrice.setText((int)buffet.getPrice()+"");
                        Float buffetSum = buffet.getPrice()*numPeople;
                        billBuffetTotal.setText(buffetSum+"");
                        finalMoney=buffetSum;
                        txtTotal.setText("Tổng : "+finalMoney+"K");
                        reference = FirebaseDatabase.getInstance().getReference("SubTotals").child(orderId);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Long finalSum = snapshot.getValue(Long.class);
                                    finalMoney=finalSum+buffetSum;
                                    txtTotal.setText("Tổng : "+finalMoney+"K");
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
        }else{
            billBuffetName.setText("Không có gì ở đây.");
        }
    }

    private void processBill(String orderId, String tableId, String buffetId, String billId) {
        Date time = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00")).getTime();
        reference=FirebaseDatabase.getInstance().getReference("Bills").child(billId);
        Bill bill= new Bill(billId,orderId,finalMoney,time,tableId,buffetId);
        reference.setValue(bill.toMap());
        //update table status
        reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId);
        //add current bill id for reception to access later
        reference.child("currentBillId").setValue(billId);
    }

    private void updateDetails(String orderId,String tableId,List<OrderDetail> orderDetails){
        for (OrderDetail orderDetail:orderDetails){
            reference=FirebaseDatabase.getInstance().getReference("OrderDetails").child(orderDetail.getId())
                    .child("quantity");
            reference.setValue(orderDetail.getQuantity());
        }
        reference = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        reference.child("status").setValue("readytopay");
        //update table status
        reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId);
        reference.child("status").setValue(true);
        reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId)
                .child("isReadyToPay");
        reference.setValue(true);
    }
}