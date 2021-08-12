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

import com.example.su21g3project.Customer.BillActivity;
import com.example.su21g3project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Model.Bill;
import Model.Floor;
import Model.Table;

public class ReadyBillAdapter extends RecyclerView.Adapter<ReadyBillAdapter.ViewHolder> {
    private List<Bill> bills;
    private Context context;
    private DatabaseReference reference;

    public ReadyBillAdapter(List<Bill> bills, Context context) {
        this.bills = bills;
        this.context = context;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTable, txtTotal, txtTime;
        private Button btnEdit;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtTable = itemView.findViewById(R.id.txtWBTable);
            txtTotal = itemView.findViewById(R.id.txtWBTotal);
            txtTime = itemView.findViewById(R.id.txtWBTime);
            btnEdit = itemView.findViewById(R.id.btnWBEdit);
        }

        public TextView getTxtTable() {
            return txtTable;
        }

        public TextView getTxtTotal() {
            return txtTotal;
        }

        public TextView getTxtTime() {
            return txtTime;
        }

        public Button getBtnEdit() {
            return btnEdit;
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View uView = inflater.inflate(R.layout.custom_waiting_bill, parent, false);
        ReadyBillAdapter.ViewHolder viewHolder = new ReadyBillAdapter.ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReadyBillAdapter.ViewHolder holder, int position) {
        Bill bill = bills.get(position);
        //get and display table and floor name
        reference = FirebaseDatabase.getInstance().getReference("Tables").child(bill.getTableId());
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
                                holder.getTxtTable().setText(table.getName()
                                        +context.getString(R.string.floor)+ floor.getName());
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

        holder.getTxtTable().setText(bill.getOrderId());
        //get and display total money
        holder.getTxtTotal().setText(context.getString(R.string.thanhtien) + bill.getTotalMoney().toString());
        //get and display time
        holder.getTxtTime().setText(bill.getStrTime());
        holder.getBtnEdit().setOnClickListener(v -> {
            Intent intent = new Intent(context, BillActivity.class);
            intent.putExtra("orderId",bill.getOrderId());
            intent.putExtra("tableId",bill.getTableId());
            intent.putExtra("buffetId",bill.getBuffetId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }
}
