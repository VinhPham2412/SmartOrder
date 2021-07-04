package Waiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.su21g3project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adapter.Waiter.OrderProcessingAdapter;
import model.OrderDetail;

public class ApproveOrderActivity extends AppCompatActivity {
    private RecyclerView view;
    private List<List<OrderDetail>> list;
    private DatabaseReference reference;
    List<List<OrderDetail>> result;
    Map<String, Object> allDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_order);
        //get list of list orderdetail by table in a order
        reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
        list = getAllOrderDetails();
        RecyclerView.Adapter adapter = new OrderProcessingAdapter(list,ApproveOrderActivity.this);
        view.setAdapter(adapter);
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