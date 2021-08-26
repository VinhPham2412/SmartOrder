package adapter.Waiter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.General.BillActivity;
import com.example.su21g3project.Customer.OrdersFoodActivity;
import com.example.su21g3project.General.GetBuffetActivity;
import com.example.su21g3project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Model.Buffet;
import Model.Floor;
import Model.Order;
import Model.Table;

public class BookedHistoryAdapter extends RecyclerView.Adapter<BookedHistoryAdapter.ViewHolder> {
    private Context context;
    private List<Order> list;
    private DatabaseReference reference;
    private String tableId;
    private String buffetId;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTableAndFloor;
        private TextView txtBuffet;
        private Button btnGetFood;
        private Button btnBill;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTableAndFloor = itemView.findViewById(R.id.txtTableAndFloor);
            txtBuffet = itemView.findViewById(R.id.txtBuffet);
            btnGetFood = itemView.findViewById(R.id.btnGetFood);
            btnBill = itemView.findViewById(R.id.btnBill);
        }

        public Button getBtnBill() {
            return btnBill;
        }

        public TextView getTxtTableAndFloor() {
            return txtTableAndFloor;
        }

        public TextView getTxtBuffet() {
            return txtBuffet;
        }

        public Button getBtnGetFood() {
            return btnGetFood;
        }
    }

    public BookedHistoryAdapter(List<Order> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View uView =
                inflater.inflate(R.layout.booked_history, parent, false);

        return new ViewHolder(uView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = list.get(position);
        //get and display tableName+floor
        tableId = order.getTableId();
        buffetId = order.getBuffetId();
        if (tableId == null) {
            holder.getTxtTableAndFloor().setText("Chưa xếp bàn");
        }else{
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
                                    holder.getTxtTableAndFloor().setText(table.getName() + ", " + floor.getName());
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
        if(buffetId==null){
            holder.getTxtBuffet().setText("Chưa chọn buffet");
        }else{
            reference = FirebaseDatabase.getInstance().getReference("Buffets").child(buffetId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Buffet buffet = snapshot.getValue(Buffet.class);
                        holder.getTxtBuffet().setText(buffet.getName());
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
        }
        //btn order work
        holder.getBtnGetFood().setOnClickListener(v -> {
            Intent intent;
            if (order.getBuffetId() == null) {
                intent = new Intent(context, GetBuffetActivity.class);
                intent.putExtra("orderId", order.getId());
            } else {
                intent = new Intent(context, OrdersFoodActivity.class);
                intent.putExtra("orderId", order.getId());
                intent.putExtra("buffetId", order.getBuffetId());
            }
            context.startActivity(intent);
        });
        //btnBill
        holder.getBtnBill().setOnClickListener(v -> {
            Intent intent = new Intent(context, BillActivity.class);
            intent.putExtra("orderId",order.getId());
            intent.putExtra("tableId",order.getTableId());
            intent.putExtra("buffetId",order.getBuffetId());
            intent.putExtra("numPeople",order.getNumberOfPeople());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
