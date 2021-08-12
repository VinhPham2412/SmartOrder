package Fragments.Chef;

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
import java.util.Date;
import java.util.List;

import Model.Order;
import Model.OrderDetail;
import adapter.Chef.ChefAdapter;
import adapter.Waiter.OrderProcessingAdapter;


public class MainChefFragment extends Fragment {


    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private ChefAdapter chefAdapter;
    private List<String> orderIds = new ArrayList<>();
    private List<List<OrderDetail>> result = new ArrayList<>();
    private List<OrderDetail> subResult = new ArrayList<>();
    private FirebaseUser user;

    public MainChefFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.custom_recylerview, container, false);
        recyclerView = view.findViewById(R.id.container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        user = FirebaseAuth.getInstance().getCurrentUser();
        //get orderId which belong to this waiter responsibility
        if (user != null) {
            //get details
            reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    //orderDetails from rtdb
                    result.clear();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        OrderDetail orderDetail = postSnapshot.getValue(OrderDetail.class);
                        //if accepted by waiter
                        if (orderDetail.getStatus().equals("accepted")) {
                            String orderId = orderDetail.getOrderId();
                            Date time = orderDetail.getTime();
                            boolean isFoundPlace = false;

                            //go through all exist subResult
                            for (int i = 0; i < result.size(); i++) {

                                //get list contain elements with same orderId
                                subResult = result.get(i);
                                if (isBelong(subResult, orderId, time)) {
                                    subResult.add(orderDetail);
                                    isFoundPlace = true;
                                }
                            }
                            //if not found any existed list belong to then make new list
                            if (!isFoundPlace) {
                                subResult = new ArrayList<>();
                                subResult.add(orderDetail);
                                //add new list to result
                                result.add(subResult);
                            }
                        }
                    }
                    chefAdapter = new ChefAdapter(result, getContext());
                    recyclerView.setAdapter(chefAdapter);
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
        }
        return view;
    }

    //belong is same orderId and time
    private boolean isBelong(@NotNull List<OrderDetail> list, String orderId, Date time) {
        if (!list.isEmpty() && list.get(0) != null) {
            return list.get(0).getOrderId().equals(orderId)
                    && list.get(0).getTime().equals(time);
        }
        return false;
    }
}