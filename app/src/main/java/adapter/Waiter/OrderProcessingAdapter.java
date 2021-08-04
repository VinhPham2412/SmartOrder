package adapter.Waiter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import model.Food;
import model.OrderDetail;

public class OrderProcessingAdapter extends RecyclerView.Adapter<OrderProcessingAdapter.ViewHolder> {
    List<List<OrderDetail>> orderDetailList;
    Context mContext;
    DatabaseReference reference;
    List<String> ids;
    String foodName;

    public OrderProcessingAdapter(List<List<OrderDetail>> orderDetailList, Context mContext) {
        this.orderDetailList = orderDetailList;
        this.mContext = mContext;
        reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View uView = inflater.inflate(R.layout.custom_order_waiter, parent, false);
        ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final List<OrderDetail> details = orderDetailList.get(position);
        ids = new ArrayList<>();
        for (OrderDetail od : details) {
            if(!od.getIsSeen()){
                ids.add(od.getId());
                reference = FirebaseDatabase.getInstance().getReference("Food").child(od.getFoodId());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        //get foodName and display
                        if(snapshot.exists()){
                            Food food = snapshot.getValue(Food.class);
                            foodName = food.getName();
                            TextView textView = holder.getTxtFood();
                            textView.setText(textView.getText()+"\n"+foodName+" : "+od.getQuantity());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        }
        holder.getBtnAccept().setOnClickListener(v -> {
            //update isSeen and isAccepted
            //push data to rtdb
            reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
            for (String id : ids) {
                reference.child(id).child("isSeen").setValue(true).addOnCompleteListener(
                        task -> Log.println(Log.INFO, "Update to rtdb", "Set seen ok"));
                reference.child(id).child("isAccepted").setValue(true).addOnCompleteListener(
                        task -> Log.println(Log.INFO, "Update to rtdb", "Set accepted ok"));
                reference.child(id).child("doing").setValue(false);
            }
        });
        holder.getBtnReject().setOnClickListener(v -> {
            //update isSeen and isAccepted
            //push data to rtdb
            reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
            for (String id : ids) {
                reference.child(id).child("isSeen").setValue(true).addOnCompleteListener(
                        task -> Log.println(Log.INFO, "Update to rtdb", "Set reject ok"));
            }
        });
        holder.getTxtTime().setText(details.get(0).getStrTime());
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTableName;
        private final TextView txtTime;
        private TextView txtFood;
        private Button btnReject, btnAccept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFood = itemView.findViewById(R.id.txtWaiterFood);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            txtTableName = itemView.findViewById(R.id.txtTable);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtFood.setText("");
        }

        public TextView getTxtTableName() {
            return txtTableName;
        }

        public TextView getTxtTime() {
            return txtTime;
        }

        public TextView getTxtFood() {
            return txtFood;
        }

        public Button getBtnReject() {
            return btnReject;
        }

        public Button getBtnAccept() {
            return btnAccept;
        }
    }
}
