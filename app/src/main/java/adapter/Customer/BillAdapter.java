package adapter.Customer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
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
    private int dvWidth;
    private Float finalSum = 0f;

    public BillAdapter(List<OrderDetail> orderDetailList, Context mContext) {
        this.orderDetailList = orderDetailList;
        this.mContext = mContext;
        reference = FirebaseDatabase.getInstance().getReference("Food");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dvWidth = displayMetrics.widthPixels;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View uView =
                inflater.from(parent.getContext()).inflate(R.layout.custom_bill, parent, false);
        return new ViewHolder(uView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            OrderDetail orderDetail = orderDetailList.get(position);
            quantity = orderDetail.getQuantity();
            reference.child(orderDetail.getFoodId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Food food = snapshot.getValue(Food.class);
                        int total = (int) (food.getPrice() * quantity);
                        TextView foodName = new TextView(mContext);
                        foodName.setText(food.getName());
                        foodName.setWidth(dvWidth / 4);
                        TextView quantity1 = new TextView(mContext);
                        quantity1.setText(quantity + "");
                        quantity1.setWidth(dvWidth / 4);
                        TextView foodPrice = new TextView(mContext);
                        foodPrice.setText((int) food.getPrice() + "");
                        foodPrice.setWidth(dvWidth / 4);
                        TextView totalMoney = new TextView(mContext);
                        totalMoney.setText(total + "");
                        totalMoney.setTextColor(Color.RED);
                        totalMoney.setWidth(dvWidth / 4);
                        finalSum += total;
                        holder.getTxtBillRow().addView(foodName);
                        holder.getTxtBillRow().addView(quantity1);
                        holder.getTxtBillRow().addView(foodPrice);
                        holder.getTxtBillRow().addView(totalMoney);
                        reference = FirebaseDatabase.getInstance().getReference("SubTotal");
                        reference.child(orderDetail.getOrderId()).setValue(finalSum);
                    }
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
        private GridLayout txtBillRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBillRow = itemView.findViewById(R.id.txtBillRow);
        }

        public GridLayout getTxtBillRow() {
            return txtBillRow;
        }
    }

}
