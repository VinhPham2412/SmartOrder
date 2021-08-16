package Fragments.Waiter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.General.CommunicationAdapter;

public class CommunicationFragment extends Fragment {
    private EditText txtReason;
    private RecyclerView listviewdata;
    private ImageButton btnSenReason, btnCamera;
    private DatabaseReference reference;
    private String userId;
    private CommunicationAdapter communicationAdapter;
    private List<String> stringList;
    private List<CommunicationAdapter.ViewHolder> viewHolders;
    private String itemSelected;

    public CommunicationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.custom_communication, container, false);
        stringList = new ArrayList<>();
        stringList.add("Khách yêu cầu gặp quản lí");
        stringList.add("Khách có thái độ không tốt,mất kiểm soát");
        stringList.add("Tôi có việc đột xuất cần nghỉ");
        stringList.add("Xin về sớm");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnSenReason = view.findViewById(R.id.btnSendReason);
        txtReason = view.findViewById(R.id.txtReason);
        listviewdata = view.findViewById(R.id.listview_data);
        listviewdata.setLayoutManager(new LinearLayoutManager(getContext()));
        communicationAdapter = new CommunicationAdapter(stringList, getContext());
        listviewdata.setAdapter(communicationAdapter);
        viewHolders = communicationAdapter.getViewHolders();
        btnSenReason.setOnClickListener(v -> {
            itemSelected = "";
            reference = FirebaseDatabase.getInstance().getReference("Communications").child("waiter");
            String communicationId = reference.push().getKey();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", communicationId);
            hashMap.put("userId", userId);
            for (CommunicationAdapter.ViewHolder viewHolder : viewHolders) {
                if(viewHolder.checkBox.isChecked()){
                    itemSelected+=viewHolder.checkBox.getText().toString()+"\n";
                }
            }
            hashMap.put("message", itemSelected + txtReason.getText().toString());
            hashMap.put("isSeen", false);
            hashMap.put("isReply", false);
            hashMap.put("isNotify", false);
            reference.child(communicationId).setValue(hashMap).addOnCompleteListener(
                    task -> Toast.makeText(getContext(), "Gửi ý kiến thành công",
                            Toast.LENGTH_SHORT).show());
        });

        return view;
    }
}