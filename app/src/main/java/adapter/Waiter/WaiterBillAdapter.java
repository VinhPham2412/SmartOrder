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

import Model.Order;
import Model.Table;

public class WaiterBillAdapter extends RecyclerView.Adapter<WaiterBillAdapter.ViewHolder> {
    private List<Order> list;
    private Context context;
    private DatabaseReference reference;

    public WaiterBillAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
        reference = FirebaseDatabase.getInstance().getReference("Orders");
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View uView = inflater.inflate(R.layout.custom_bill_waiter, parent, false);
        ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Order processOrder = list.get(position);
        reference = FirebaseDatabase.getInstance().getReference("Tables").
                child(processOrder.getTableId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Table table = snapshot.getValue(Table.class);
                    holder.getTxtTable().setText("Bàn "+table.getName());
                    holder.getTxtCustomer().setText(processOrder.getName()+"\n"+processOrder.getPhone());
                    holder.getBtnBill().setOnClickListener(v -> {
                        Intent intent = new Intent(context, BillActivity.class);
                        intent.putExtra("orderId",processOrder.getId());
                        intent.putExtra("tableId",processOrder.getTableId());
                        intent.putExtra("buffetId",processOrder.getBuffetId());
                        intent.putExtra("numPeople",processOrder.getNumberOfPeople());
                        context.startActivity(intent);
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
        return list.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTable,txtCustomer;
        private Button btnBill;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txtTable = itemView.findViewById(R.id.txtTableName);
            txtCustomer = itemView.findViewById(R.id.txtCustomerName);
            btnBill = itemView.findViewById(R.id.btnWaiterBill);
        }

        public TextView getTxtTable() {
            return txtTable;
        }

        public TextView getTxtCustomer() {
            return txtCustomer;
        }

        public Button getBtnBill() {
            return btnBill;
        }
    }
}
