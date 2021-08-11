package com.example.su21g3project.Customer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Model.User;
import adapter.General.CommunicationAdapter;

public class CommunicationActivity extends AppCompatActivity {
    private EditText txtReason;
    private RecyclerView listviewdata;
    private ImageButton btnSenReason;
    private DatabaseReference reference;
    private String userId;
    private String role = "customer";
    private List<String> chefNotice =new ArrayList<>();
    private List<String> customerNotice = new ArrayList<>();
    private CommunicationAdapter communicationAdapter;
    private List<CommunicationAdapter.ViewHolder> viewHolders;

    /**
     * create View
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_customer);
        chefNotice.add("Bếp gặp sự cố");
        chefNotice.add("Nguyên liệu có vấn đề");
        chefNotice.add("Có người bị thương trong bếp");
        chefNotice.add("Cần bổ xung thêm người làm");
        customerNotice.add("Đồ ăn ra quá chậm");
        customerNotice.add("Đồ ăn ra không đúng với Order");
        customerNotice.add("Chất lượng đồ ăn có vấn đề");
        customerNotice.add("Thái độ nhân viên không được chuẩn mực");
        customerNotice.add("Vệ sinh bàn còn rất bẩn");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnSenReason = findViewById(R.id.btnSendReason);
        txtReason = findViewById(R.id.txtReason);
        listviewdata = findViewById(R.id.listview_data);
        listviewdata.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        // check role user then display reason allow role
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    role = user.getRole();
                    if (role.equals("customer")) {
                        communicationAdapter = new CommunicationAdapter(customerNotice,getApplicationContext(),txtReason);
                    } else {
                        communicationAdapter = new CommunicationAdapter(chefNotice,getApplicationContext(),txtReason);
                    }
                    listviewdata.setAdapter(communicationAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // action when click send reason
        btnSenReason.setOnClickListener(v -> {
            if (role.equals("customer"))
                reference = FirebaseDatabase.getInstance().getReference("Communications").child("customer");
            else
                reference = FirebaseDatabase.getInstance().getReference("Communications").child("chef");
            String communicationId = reference.push().getKey();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", communicationId);
            hashMap.put("userId", userId);
            hashMap.put("message", txtReason.getText().toString());
            hashMap.put("isSeen", false);
            hashMap.put("isReply",false);
            hashMap.put("isNotify",false);
            reference.child(communicationId).setValue(hashMap).addOnCompleteListener(
                    task -> Toast.makeText(CommunicationActivity.this, "Gửi ý kiến thành công",
                            Toast.LENGTH_SHORT).show());
        });

    }
}