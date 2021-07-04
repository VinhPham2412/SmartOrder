package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.su21g3project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Waiter.ApproveOrderActivity;
import adapter.Waiter.OrderProcessingAdapter;
import model.OrderDetail;


public class OrderFragment extends Fragment {


    private RecyclerView recyclerView;
    private List<List<OrderDetail>> list;
    private DatabaseReference reference;
    List<List<OrderDetail>> result;
    Map<String, Object> allDetails;
    private OrderProcessingAdapter orderProcessingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_order, container, false);
        recyclerView=view.findViewById(R.id.recycleViewOrder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
        list = getAllOrderDetails();
        orderProcessingAdapter=new OrderProcessingAdapter(list,getContext());
        recyclerView.setAdapter(orderProcessingAdapter);
        return view;
    }

    private List<List<OrderDetail>> getAllOrderDetails() {
        result = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //list of orderdetails
                allDetails = (Map<String, Object>) snapshot.getValue();

                //key of orderDetails
                Set<String> keys = allDetails.keySet();
                String currentId;

                for (String key: keys
                ) {
                    OrderDetail orderDetail = (OrderDetail) allDetails.get(key);
                    boolean isAdded = false;
                    //put same orderId together
                    String orderId = orderDetail.getOrderId();
                    List<OrderDetail> list1 = new ArrayList<>();

                    //if had this orderID then add to list
                    for(int i=0 ; i< result.size();i++){
                        list1 = result.get(i);
                        if(isExist(list1,orderId)){
                            list1.add(orderDetail);
                            isAdded = true;
                        }
                    }
                    if(!isAdded){
                        list1 = new ArrayList<>();
                        list1.add(orderDetail);
                        result.add(list1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return result;
    }
    private boolean isExist(List<OrderDetail> list,String orderId){
        for(OrderDetail d:list){
            if(d.getOrderId().equals(orderId)){
                return true;
            }
        }
        return false;
    }
}