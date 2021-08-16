package adapter.Customer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import Model.Floor;
import Model.Order;
import Model.Table;

public class CBookedHistoryAdapter extends RecyclerView.Adapter<CBookedHistoryAdapter.ViewHolder> {
    private Context context;
    private List<Order> list;
    private DatabaseReference reference;
    private String tableId;

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTableAndFloor;
        private TextView txtInfo;
        private TextView txtStatus;
        private Button btnOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTableAndFloor = itemView.findViewById(R.id.txtCTable);
            txtInfo = itemView.findViewById(R.id.txtBookedInfo);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnOrder = itemView.findViewById(R.id.btnCOrder);
        }

        public TextView getTxtTableAndFloor() {
            return txtTableAndFloor;
        }

        public TextView getTxtInfo() {
            return txtInfo;
        }

        public TextView getTxtStatus() {
            return txtStatus;
        }

        public Button getBtnOrder() {
            return btnOrder;
        }
    }

    public CBookedHistoryAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View uView =
                inflater.inflate(R.layout.cbooked_history, parent, false);

        return new ViewHolder(uView);
    }

    @Override
    public void onBindViewHolder(CBookedHistoryAdapter.ViewHolder holder, int position) {
        Order order = list.get(position);
        //get and display tableName+floor
        tableId = order.getTableId();
        //display table info
        if (tableId == null) {
            holder.getTxtTableAndFloor().setText("Chưa xếp bàn");
        } else {
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
        //display booked info
        holder.getTxtInfo().setText(context.getString(R.string.tennguoidat) + ": " + order.getName() + "\n"
                + context.getString(R.string.sodienthoai) + " " + order.getPhone() + "\n"
                + context.getString(R.string.gioan) + ": " + order.getStrDate() + "\n"
                + context.getString(R.string.sl_nopeople) + ": " + order.getNumberOfPeople() + "\n"
                + context.getString(R.string.ghichu) + ": " + order.getNote() + "\n");
        //display status info
        String status = order.getStatus();
        switch (status) {
            case "new":
                holder.getTxtStatus().setText(context.getString(R.string.waitingaccept));
                holder.getTxtStatus().setTextColor(Color.BLUE);
                holder.getBtnOrder().setEnabled(false);
                break;
            case "accepted":
                holder.getTxtStatus().setText(context.getString(R.string.accepted));
                holder.getTxtStatus().setTextColor(Color.GREEN);
                holder.getBtnOrder().setEnabled(true);
                break;
            case "rejected":
                holder.getTxtStatus().setText(context.getString(R.string.rejected));
                holder.getTxtStatus().setTextColor(Color.RED);
                holder.getBtnOrder().setEnabled(false);
                break;
            case "done":
                holder.getTxtStatus().setText(context.getString(R.string.daphucvu));
                holder.getTxtStatus().setTextColor(Color.BLACK);
                holder.getBtnOrder().setEnabled(false);
                break;
            default:
                holder.getTxtStatus().setText(context.getString(R.string.requesttopay));
                holder.getTxtStatus().setTextColor(Color.BLACK);
                holder.getBtnOrder().setEnabled(false);
                break;
        }
        //btn order work
        holder.getBtnOrder().setOnClickListener(v -> {
            Intent intent;
            String buffetId = order.getBuffetId();
            if (buffetId == null) {
                intent = new Intent(context, GetBuffetActivity.class);
                intent.putExtra("orderId", order.getId());
            } else {
                intent = new Intent(context, OrdersFoodActivity.class);
                intent.putExtra("orderId", order.getId());
                intent.putExtra("buffetId", order.getBuffetId());
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

