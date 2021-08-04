package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.Customer.BookedHistoryAdapter;
import adapter.Waiter.WaiterBillAdapter;
import model.ProcessOrder;


public class BillFragment extends Fragment {
    private DatabaseReference reference;
    private List<ProcessOrder> list;
    private RecyclerView recyclerView;
    private WaiterBillAdapter adapter;
    private int dvHeight;
    private FirebaseUser user;
    public BillFragment(FragmentActivity fragmentActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        fragmentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dvHeight = displayMetrics.heightPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_waiter, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView  = view.findViewById(R.id.bill_waiter_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("ProcessOrder");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProcessOrder processOrder = dataSnapshot.getValue(ProcessOrder.class);
                    if (processOrder.getStatus().equals("confirmed") && processOrder.getWaiterId().equals(user.getUid())) {
                        list.add(processOrder);
                    }

                }
                adapter = new WaiterBillAdapter(list, getContext());
                recyclerView.setMinimumHeight(dvHeight);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}