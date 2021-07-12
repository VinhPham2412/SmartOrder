package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.Waiter.OrderProcessingAdapter;
import model.OrderDetail;


public class OrderFragment extends Fragment {


    private RecyclerView recyclerView;
    private List<List<OrderDetail>> list;
    private DatabaseReference reference;
    private OrderProcessingAdapter orderProcessingAdapter;
    private List<List<OrderDetail>> result = new ArrayList<>();
    private List<OrderDetail> subResult = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
        recyclerView = view.findViewById(R.id.recycleViewOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //orderDetails from rtdb
                result.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    OrderDetail orderDetail = postSnapshot.getValue(OrderDetail.class);
                    //if not seen
                    if(!orderDetail.getIsSeen()){
                        String orderId = orderDetail.getOrderId();
                        Date time = orderDetail.getTime();
                        boolean isFoundPlace = false;

                        //go through all exist subResult
                        for (int i = 0; i < result.size(); i++) {

                            //get list contain elements with same orderId
                            subResult = result.get(i);
                            if (isBelong(subResult, orderId,time)) {
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
                orderProcessingAdapter = new OrderProcessingAdapter(result, getContext());
                recyclerView.setAdapter(orderProcessingAdapter);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        return view;
    }

    //belong is same orderId and time
    private boolean isBelong(List<OrderDetail> list, String orderId, Date time) {
        if (!list.isEmpty()&&list.get(0)!=null) {
            return list.get(0).getOrderId().equals(orderId)
                    &&list.get(0).getTime().equals(time);
        }
        return false;
    }
}