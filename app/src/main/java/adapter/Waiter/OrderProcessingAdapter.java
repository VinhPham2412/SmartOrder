package adapter.Waiter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import model.Food;
import model.OrderDetail;

public class OrderProcessingAdapter extends RecyclerView.Adapter<adapter.Waiter.OrderProcessingAdapter.ViewHolder> {
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
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Food").child(od.getFoodId());
            reference.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    foodName = task.getResult().getValue(Food.class).getName();
                }
            });
            TextView textView = new TextView(mContext);
            textView.setText(foodName + " : " + od.getQuantity());

            holder.gridView.addView(textView);
            ids.add(od.getId());
        }
        OrderDetail orderDetail=details.get(1);
        holder.btnAccept.setOnClickListener(v -> {
            //update isSeen and isAccepted
            //push data to rtdb
            for (String id : ids
            ) {
                reference.child(id).child("isSeen").setValue(true).addOnCompleteListener(
                        task -> Log.println(Log.INFO, "Update to rtdb", "Set seen ok"));
                reference.child(id).child("isAccepted").setValue(true).addOnCompleteListener(
                        task -> Log.println(Log.INFO, "Update to rtdb", "Set accepted ok"));
            }
        });
        holder.btnReject.setOnClickListener(v -> {
            //update isSeen and isAccepted
            //push data to rtdb
            for (String id : ids) {
                reference.child(id).child("isSeen").setValue(true).addOnCompleteListener(
                        task -> Log.println(Log.INFO, "Update to rtdb", "Set reject ok"));
            }
        });
        holder.txtTime.setText(orderDetail.getTime().toString());

    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTableName,txtTime;
        public GridLayout gridView;
        public Button btnReject, btnAccept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gridView = itemView.findViewById(R.id.gridview);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            txtTableName=itemView.findViewById(R.id.txtTable);
            txtTime=itemView.findViewById(R.id.txtTime);
        }
    }
}
