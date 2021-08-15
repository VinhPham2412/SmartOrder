package adapter.Chef;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import Model.Floor;
import Model.Food;
import Model.Order;
import Model.OrderDetail;
import Model.Table;

public class ChefAdapter extends RecyclerView.Adapter<ChefAdapter.ViewHolder> {
    private List<List<OrderDetail>> orderDetailList;
    private Context mContext;
    private DatabaseReference reference;
    private List<String> ids;
    private String foodName;

    public ChefAdapter(List<List<OrderDetail>> detailList, Context mContext) {
        this.orderDetailList = detailList;
        this.mContext = mContext;
        reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTableName;
        private final TextView txtTime;
        private TextView txtFood;
        private Button btnAccept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFood = itemView.findViewById(R.id.txtDetails);
            btnAccept = itemView.findViewById(R.id.btnChefDone);
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

        public Button getBtnAccept() {
            return btnAccept;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View uView = inflater.inflate(R.layout.custom_order_chef, parent, false);
        ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final List<OrderDetail> details = orderDetailList.get(position);
        ids = new ArrayList<>();
        for (OrderDetail od : details) {
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
                        textView.setText(textView.getText() + "\n" + foodName + " : " + od.getQuantity());
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
        holder.getBtnAccept().setOnClickListener(v -> {
            /**
             * update order details status to delivered
             */
            reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
            for (String id : ids) {
                reference.child(id).child("status").setValue("cooked").addOnCompleteListener(
                        task -> Log.println(Log.INFO, "Update to rtdb", "Set delivered ok"));
            }
        });
        holder.getTxtTime().setText(details.get(0).getTimeString());
        //set text for table and floor
        String orderId = details.get(0).getOrderId();
        reference = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    String tableId = order.getTableId();
                    reference = FirebaseDatabase.getInstance().getReference("Tables").child(tableId);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //got table name
                                Table table = snapshot.getValue(Table.class);
                                reference = FirebaseDatabase.getInstance().getReference("Floors").child(table.getFloorId());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            //got floor name
                                            Floor floor = snapshot.getValue(Floor.class);
                                            holder.getTxtTableName().setText(table.getName() + ", " + floor.getName());
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
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }
}
