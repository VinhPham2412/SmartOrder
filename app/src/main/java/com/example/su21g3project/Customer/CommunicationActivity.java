package com.example.su21g3project.Customer;

import android.os.Bundle;
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
    private String messageContents;


    /**
     * create View
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_communication);
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
        // check role user then display reason of that role
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    role = user.getRole();
                    if (role.equals("customer")) {
                        communicationAdapter = new CommunicationAdapter(customerNotice,getApplicationContext());
                    } else {
                        communicationAdapter = new CommunicationAdapter(chefNotice,getApplicationContext());
                    }
                    listviewdata.setAdapter(communicationAdapter);
                    // action when click send reason
                    btnSenReason.setOnClickListener(v -> {
                        viewHolders = communicationAdapter.getViewHolders();
                        messageContents = "";
                        for (CommunicationAdapter.ViewHolder viewh : viewHolders) {
                            if (viewh.checkBox.isChecked()) {
                                messageContents += viewh.checkBox.getText() + "\n";
                            }
                        }
                        if (txtReason.getText() != null) {
                            messageContents += txtReason.getText().toString();
                        }
                        if (messageContents.isEmpty()){
                            Toast.makeText(CommunicationActivity.this, "Không có nội dung xin mời nhập lại",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {


                            if (role.equals("customer"))
                                reference = FirebaseDatabase.getInstance().getReference("Communications").child("customer");
                            else
                                reference = FirebaseDatabase.getInstance().getReference("Communications").child("chef");
                            String communicationId = reference.push().getKey();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", communicationId);
                            hashMap.put("userId", userId);
                            hashMap.put("message", messageContents);
                            hashMap.put("isSeen", false);
                            hashMap.put("isReply", false);
                            hashMap.put("isNotify", false);
                            reference.child(communicationId).setValue(hashMap).addOnCompleteListener(
                                    task -> Toast.makeText(CommunicationActivity.this, "Gửi ý kiến thành công",
                                            Toast.LENGTH_SHORT).show());
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