package com.example.su21g3project.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.example.su21g3project.Waiter.MainWaiterActivity;
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
    User currentUser=null;
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
                                reference=FirebaseDatabase.getInstance().getReference("Bills").child(orderId);
                                HashMap hashMap=new HashMap<String,Object>();
                                hashMap.put("orderId",orderId);
                                hashMap.put("totalMoney",finalMoney);
                                reference.setValue(hashMap);
                                for (OrderDetail orderDetail:orderDetailList){
                                    reference=FirebaseDatabase.getInstance().getReference("Bills").child(orderId).
                                            child("details").child(orderDetail.getId());
                                    HashMap hashMap1=new HashMap<String,Object>();
                                    hashMap1.put("id",orderDetail.getId());
                                    hashMap1.put("foodId",orderDetail.getFoodId());
                                    hashMap1.put("quantity",orderDetail.getQuantity());
                                    reference.setValue(hashMap1);
                                }


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
        /**
         * action when click confirmBill
         */
        btnConfirmBill.setOnClickListener(v -> {
            List<BillAdapter.ViewHolder> viewHolderList=billAdapter.getAllHolder();
            for (BillAdapter.ViewHolder viewHolder:viewHolderList){
                reference=FirebaseDatabase.getInstance().getReference("OrderDetails").child(viewHolder.getOrderDetailId()).child("quantity");
                reference.setValue(Integer.parseInt(viewHolder.getEtFoodQuantity().getText().toString()));
            }
            reference = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
            reference.child("status").setValue("readytopay");
            reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId);
            reference.child("status").setValue(true);
            reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId).child("isReadyToPay");
            reference.setValue(true);
            Toast.makeText(getApplicationContext(),"Thanh toán thành công",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainWaiterActivity.class));
        });
    }
}