package Fragments.Chef;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import adapter.General.CommunicationAdapter;

public class CommunicationFragment extends Fragment {
    private EditText txtReason;
    private RecyclerView listviewdata;
    ArrayAdapter<String> adapter;
    private ImageButton btnSenReason,btnCamera;
    private DatabaseReference reference;
    private String userId;
    private CommunicationAdapter communicationAdapter;
    List<String> stringList;


private int dvHeight;
    public CommunicationFragment(FragmentActivity fragmentActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        fragmentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dvHeight = displayMetrics.heightPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_communication, container, false);
        setHasOptionsMenu(true);
        stringList=new ArrayList<>();
        stringList.add("Khách yêu cầu gặp quản lí");
        stringList.add("Khách có thái độ không tốt,mất kiểm soát");
        stringList.add("Tôi có việc đột xuất cần nghỉ");
        stringList.add("Xin về sớm");
        userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnSenReason=view.findViewById(R.id.btnSendReason);
        txtReason=view.findViewById(R.id.txtReason);
        listviewdata=view.findViewById(R.id.listview_data);
        listviewdata.setLayoutManager(new LinearLayoutManager(getContext()));
        communicationAdapter=new CommunicationAdapter(stringList,getContext());
        listviewdata.setAdapter(communicationAdapter);
        btnSenReason.setOnClickListener(v -> {
            String communicationId = UUID.randomUUID().toString();
            reference = FirebaseDatabase.getInstance().getReference("Communications").child("waiter");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", communicationId);
            hashMap.put("userId", userId);
            hashMap.put("message", txtReason.getText().toString());
            hashMap.put("isSeen", false);
            hashMap.put("isReply",false);
            hashMap.put("isNotify",false);
            reference.child(communicationId).setValue(hashMap).addOnCompleteListener(
                    task -> Toast.makeText(getContext(),"Gửi ý kiến thành công",
                            Toast.LENGTH_SHORT).show());
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_communication,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_done) {
            String itemSelected = "";
            List<CommunicationAdapter.ViewHolder> viewHolders=communicationAdapter.getViewHolders();
            for (CommunicationAdapter.ViewHolder v:viewHolders){
                if (v.checkBox.isChecked()){
                    itemSelected+=v.txtReason.getText()+"\n";
                }
            }
            txtReason.setText(itemSelected);
        }
        return super.onOptionsItemSelected(item);
    }

}