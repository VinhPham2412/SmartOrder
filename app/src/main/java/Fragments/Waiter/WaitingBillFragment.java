package Fragments.Waiter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import Model.Bill;
import Model.Order;
import adapter.Waiter.ReadyBillAdapter;

public class WaitingBillFragment extends Fragment {
    private DatabaseReference reference;
    private List<String> list;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private List<Bill> bills;

    public WaitingBillFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_recylerview, container, false);
        recyclerView = view.findViewById(R.id.container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //get orderIds that belong to this waiter responsibility
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Orders");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (order.getStatus().equals("readytopay")&&
                    order.getWaiterId().equals(user.getUid())) {
                        list.add(order.getId());
                    }
                }
                reference = FirebaseDatabase.getInstance().getReference("Bills");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        bills = new ArrayList<>();
                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                            if(snapshot1.exists()){
                                Bill bill = snapshot1.getValue(Bill.class);
                                if(list.contains(bill.getOrderId())){
                                    bills.add(bill);
                                }
                            }
                        }
                        ReadyBillAdapter adapter = new ReadyBillAdapter(bills,getContext());
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }
}