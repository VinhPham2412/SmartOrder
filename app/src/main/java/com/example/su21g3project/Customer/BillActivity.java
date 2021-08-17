package com.example.su21g3project.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.General.MainActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import Model.Bill;
import Model.Buffet;
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
    FirebaseUser user;
    private Float finalMoney=0f;
    String role="customer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        user= FirebaseAuth.getInstance().getCurrentUser();
        //get from intent
        String orderId = getIntent().getStringExtra("orderId");
        String tableId = getIntent().getStringExtra("tableId");
        String buffetId=getIntent().getStringExtra("buffetId");
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
                    /**
                     * action when click confirmBill
                     */
                    if(role.equals("customer")){
                        btnConfirmBill.setOnClickListener(v -> {
                            Date time = Calendar.getInstance(TimeZone.getTimeZone("GMT +7:00")).getTime();
                            String billId = FirebaseDatabase.getInstance().getReference("Bills").push().getKey();
                            reference=FirebaseDatabase.getInstance().getReference("Bills").child(billId);
                            AlertDialog.Builder builder = new AlertDialog.Builder(BillActivity.this);
                            builder.setTitle(getApplicationContext().getString(R.string.sendpayrequest));
                            builder.setPositiveButton(R.string.done, (dialog, which) -> {
                                //push bill to rtdb
                                Bill bill= new Bill(billId,orderId,finalMoney,time,tableId,buffetId);
                                reference.setValue(bill.toMap());
                                //update order status
                                reference = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
                                reference.child("status").setValue("requestToPay");
                                //update table status , HERE ?
                                reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId);
                                //add current bill id for reception to access later
                                reference.child("currentBillId").setValue(billId);
                                reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId)
                                        .child("isReadyToPay");
                                reference.setValue(true);
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(BillActivity.this);
                                builder1.setTitle(BillActivity.this.getString(R.string.waitforwaiter));
                                builder1.setNegativeButton(R.string.done, ((dialog1, which1) -> {
                                    dialog1.cancel();
                                    Intent intent = new Intent(BillActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                })).create().show();
                            }).setNegativeButton(R.string.cancel, ((dialog, which) -> {
                                dialog.cancel();
                            })).create().show();
                        });
                    }else{
                        btnConfirmBill.setOnClickListener(v -> {
                            reference = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
                            reference.child("status").setValue("readytopay");
                            //update table status , HERE ?
                            reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId);
                            reference.child("status").setValue(true);
                            finish();
                        });
                    }
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
                /**
                 * check if same food then add quantity together
                 */
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
                billAdapter = new BillAdapter(orderDetailList,getApplicationContext(),role);
                recyclerView.setAdapter(billAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        reference=FirebaseDatabase.getInstance().getReference("Buffets").child(buffetId);
        /**
         * Display buffet info in ProcessOrder
         */
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


    }
}