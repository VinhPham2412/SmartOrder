package adapter.Chef;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import Model.Order;
import Model.OrderDetail;
import Model.Table;

public class ChefOrderAdapter extends RecyclerView.Adapter<ChefOrderAdapter.ViewHolder> {
    List<List<OrderDetail>> orderDetailList;
    Context mContext;
    DatabaseReference reference;
    List<String> ids;
    String foodName;
    String tableId;

    public ChefOrderAdapter(List<List<OrderDetail>> orderDetailList, Context mContext) {
        this.orderDetailList = orderDetailList;
        this.mContext = mContext;
        reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
    }

    @NonNull
    @Override
    public ChefOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View uView = inflater.inflate(R.layout.custom_order_chef, parent, false);
        ChefOrderAdapter.ViewHolder viewHolder = new ChefOrderAdapter.ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChefOrderAdapter.ViewHolder holder, int position) {
        final List<OrderDetail> details = orderDetailList.get(position);
        ids = new ArrayList<>();
        for (int i = 0; i < details.size(); i++) {
            OrderDetail od = details.get(i);
            ids.add(od.getId());
            reference = FirebaseDatabase.getInstance().getReference("Foods").child(od.getFoodId());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    //get foodName and display
                    if (snapshot.exists()) {
                        Food food = snapshot.getValue(Food.class);
                        foodName = food.getName();
                        TextView textView = holder.getTxtFood();
                        String status =od.getStatus();
                        switch (status){
                            case "cooked":
                                textView.setText(textView.getText()+"\n"+foodName + " : " + od.getQuantity()+ " - cooked");
                                holder.getBtnChefDone().setVisibility(View.VISIBLE);
                                break;
                            default:
                                textView.setText(textView.getText()+"\n"+foodName + " : " + od.getQuantity()+ " - accepted");
                                holder.getBtnChefDone().setVisibility(View.INVISIBLE);
                                break;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        //get table id
        reference = FirebaseDatabase.getInstance().getReference("Orders").child(details.get(0).getOrderId());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    tableId = order.getTableId();
                    //get table name
                    reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Table table = snapshot.getValue(Table.class);
                                holder.getTxtTableName().setText(table.getName());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        holder.getBtnChefDone().setOnClickListener(v -> {
            //update status
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.dialog_reason, null);
            builder.setView(view);
            EditText text = view.findViewById(R.id.txtRs);
            TextView title = view.findViewById(R.id.textView22);
            title.setText("Confirmation !");
            builder.setPositiveButton(R.string.done, (dialog, which) -> {
                //push data to rtdb
                reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
                for (String id : ids) {
                    reference.child(id).child("status").setValue("delivered").addOnCompleteListener(
                            task -> Log.println(Log.INFO, "Update to rtdb", "Set cooked ok"));
                }
            }).setNegativeButton(R.string.cancel, ((dialog, which) -> {
                dialog.cancel();
            })).create().show();
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTableName;
        private final TextView txtTime;
        private TextView txtFood;
        private Button btnChefDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFood = itemView.findViewById(R.id.txtDetails);
            btnChefDone = itemView.findViewById(R.id.btnChefDone);
            txtTableName = itemView.findViewById(R.id.txtChefTable);
            txtTime = itemView.findViewById(R.id.txtChefTime);
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

        public Button getBtnChefDone() {
            return btnChefDone;
        }
    }
}
