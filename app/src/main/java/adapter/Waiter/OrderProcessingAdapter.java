package adapter.Waiter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import Model.Food;
import Model.OrderDetail;

public class OrderProcessingAdapter extends RecyclerView.Adapter<OrderProcessingAdapter.ViewHolder> {
    List<List<OrderDetail>> orderDetailList;
    Context mContext;
    DatabaseReference reference;
    List<String> ids;
    String foodName;

    public OrderProcessingAdapter(List<List<OrderDetail>> orderDetailList, Context mContext) {
        this.orderDetailList = orderDetailList;
        this.mContext = mContext;
        reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View uView = inflater.inflate(R.layout.custom_order_waiter, parent, false);
        ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final List<OrderDetail> details = orderDetailList.get(position);
        ids = new ArrayList<>();
        for (OrderDetail od : details) {
            if(od.getStatus().equals("new")){
                ids.add(od.getId());
                reference = FirebaseDatabase.getInstance().getReference("Foods").child(od.getFoodId());
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
            reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
            for (String id : ids) {
                reference.child(id).child("status").setValue("accepted").addOnCompleteListener(
                        task -> Log.println(Log.INFO, "Update to rtdb", "Set seen ok"));
            }
            reference = FirebaseDatabase.getInstance().getReference("Orders").
                    child(details.get(0).getOrderId());
            reference.child("status").setValue("accepted").addOnCompleteListener
                    (task ->  Log.println(Log.INFO, "Update to rtdb", "Set order reject ok"));
        });
        holder.getBtnReject().setOnClickListener(v -> {
            //update isSeen and isAccepted
            //input the reason
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.dialog_reason,null);
            builder.setView(view);
            EditText text = view.findViewById(R.id.txtRs);
            TextView title = view.findViewById(R.id.textView22);
            title.setText("Lý do từ chối.");
            builder.setPositiveButton(R.string.reject, (dialog, which) -> {
                //push data to rtdb
                if(text.getText().toString().trim().isEmpty()){
                    Toast.makeText(mContext,"Reason cannot empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
                for (String id : ids) {
                    reference.child(id).child("status").setValue("rejected").addOnCompleteListener(
                            task -> Log.println(Log.INFO, "Update to rtdb", "Set reject ok"));
                    reference.child(id).child("reason").setValue(text.getText().toString()).addOnCompleteListener
                            (task -> Log.println(Log.INFO, "Insert reason to rtdb", "Set detail reason ok"));
                }
            }).setNegativeButton(R.string.cancel,((dialog, which) -> {
                dialog.cancel();
            })).create().show();
        });
        holder.getTxtTime().setText(details.get(0).getTimeString());
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
