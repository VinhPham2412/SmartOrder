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

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Model.Bill;

public class ReadyBillAdapter extends RecyclerView.Adapter<ReadyBillAdapter.ViewHolder>{
    private List<Bill> bills;
    private Context context;
    public ReadyBillAdapter(List<Bill> bills,Context context) {
        this.bills = bills;
        this.context =context;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTable,txtTotal,txtTime;
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
        Bill bill =bills.get(position);
        //get and display table and floor name
        holder.getTxtTable().setText(bill.getOrderId());
        //get and display total money
        holder.getTxtTotal().setText(bill.getTotalMoney().toString());
        //get and display time
        holder.getTxtTime().setText(bill.getStrTime());
        holder.getBtnEdit().setOnClickListener(v -> {
            Intent intent = new Intent(context, BillActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }
}
