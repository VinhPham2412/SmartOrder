package com.example.su21g3project.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;

import model.User;

public class CommunicationCustomer extends AppCompatActivity {
    private EditText txtReason;
    private ListView listviewdata;
    ArrayAdapter<String> adapter;
    private Button btnSenReason,btnCamera;
    private DatabaseReference reference;
    private String userId;
    private String role="customer";
    String [] chefNotice={"Bếp gặp sự cố","Nguyên liệu có vấn đề","Có người bị thương trong bếp",
            "Cần bổ xung thêm người làm"};
    String [] customerNotice={"Đồ ăn ra quá chậm","Đồ ăn ra không đúng với Order","Chất lượng đồ ăn có vấn đề",
            "Thái độ nhân viên không được chuẩn mực","Vệ sinh bàn còn rất bẩn"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_customer);
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnSenReason=findViewById(R.id.btnSendReason);
        txtReason=findViewById(R.id.txtReason);
        listviewdata=findViewById(R.id.listview_data);
        reference=FirebaseDatabase.getInstance().getReference("User").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user=snapshot.getValue(User.class);
                    role=user.getRole();
                    if (role.equals("customer")){
                        adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice,customerNotice);
                        listviewdata.setAdapter(adapter);
                    }
                    else {
                        adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice,chefNotice);
                        listviewdata.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSenReason.setOnClickListener(v -> {
            String communicationId = UUID.randomUUID().toString();
            if (role.equals("customer"))
                reference = FirebaseDatabase.getInstance().getReference("Communication").child("Customer");
            else
                reference = FirebaseDatabase.getInstance().getReference("Communication").child("Chef");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", communicationId);
            hashMap.put("userId", userId);
            hashMap.put("message", txtReason.getText().toString());
            hashMap.put("isSeen", false);
            reference.child(communicationId).setValue(hashMap).addOnCompleteListener(
                    task -> Toast.makeText(CommunicationCustomer.this,"Gửi ý kiến thành công",
                            Toast.LENGTH_SHORT).show());
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_communication,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.item_done){
            String itemSelected="";
          for (int i=0;i<listviewdata.getCount();i++){
              if (listviewdata.isItemChecked(i)){
                  itemSelected+=listviewdata.getItemAtPosition(i)+"\n";
              }
          }
          txtReason.setText(itemSelected);
        }
        return super.onOptionsItemSelected(item);
    }
}