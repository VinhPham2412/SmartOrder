package adapter.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import model.Food;
import model.OrderDetail;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private List<OrderDetail> orderDetailList;
    private Context mContext;
    private DatabaseReference reference;
    private int quantity;
    private ViewHolder viewHolder;

    public BillAdapter(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
        reference = FirebaseDatabase.getInstance().getReference("Food");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View uView =
                inflater.inflate(R.layout.custom_bill, parent, false);
        return new ViewHolder(uView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        viewHolder = holder;
        OrderDetail orderDetail = orderDetailList.get(position);
        quantity = orderDetail.getQuantity();
        reference.child(orderDetail.getFoodId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food = snapshot.getValue(Food.class);
                int total = (int)(food.getPrice() * quantity);
                viewHolder.getTxtBillRow().setText(food.getName()+" "+food.getPrice()+" "+quantity+" "+total);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtBillRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBillRow = itemView.findViewById(R.id.txtBillRow);
        }

        public TextView getTxtBillRow() {
            return txtBillRow;
        }
    }

}
