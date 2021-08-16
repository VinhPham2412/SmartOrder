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

import Model.Floor;
import Model.Food;
import Model.Order;
import Model.OrderDetail;
import Model.Table;

public class CookedOrderAdapter extends RecyclerView.Adapter<CookedOrderAdapter.ViewHolder> {

    private List<List<OrderDetail>> result;
    private Context context;
    private DatabaseReference reference;
    private List<String> ids;
    private String foodName;

    public CookedOrderAdapter(List<List<OrderDetail>> result) {
        this.result = result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View uView =
                inflater.inflate(R.layout.cooked_order, parent, false);
        return new CookedOrderAdapter.ViewHolder(uView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final List<OrderDetail> details = result.get(position);
        ids = new ArrayList<>();
        //display food and quantity
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
        //btn accept
        holder.getBtnDelivery().setOnClickListener(v -> {
            //update details status to delivered ,push data to rtdb
            reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
            for (String id : ids) {
                reference.child(id).child("status").setValue("delivered").addOnCompleteListener(
                        task -> Log.println(Log.INFO, "Update to rtdb", "Set delivered ok"));
            }
        });
        //set text for time
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
                                            holder.getTxtTable().setText(table.getName() +context.getString(R.string.floor) + floor.getName());
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
        return result.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private Button btnDelivery;
        private TextView txtTable;
        private TextView txtTime;
        private TextView txtFood;
        public ViewHolder(View uView) {
            super(uView);
            btnDelivery = uView.findViewById(R.id.btnDelivery);
            txtTable = uView.findViewById(R.id.txtCookedTable);
            txtTime = uView.findViewById(R.id.txtCookedTime);
            txtFood = uView.findViewById(R.id.txtCookedFood);
        }

        public Button getBtnDelivery() {
            return btnDelivery;
        }

        public TextView getTxtTable() {
            return txtTable;
        }

        public TextView getTxtTime() {
            return txtTime;
        }

        public TextView getTxtFood() {
            return txtFood;
        }
    }
}
