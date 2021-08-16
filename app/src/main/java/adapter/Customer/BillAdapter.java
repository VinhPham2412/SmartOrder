package adapter.Customer;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.Food;
import Model.OrderDetail;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {
    private List<OrderDetail> orderDetailList;
    private Context mContext;
    private DatabaseReference reference;
    private Float finalSum = 0f;
    private List<ViewHolder> allViews;
    ValueEventListener eventListener;
    private String role;

    public BillAdapter(List<OrderDetail> orderDetailList, Context mContext,String role) {
        this.orderDetailList = orderDetailList;
        this.mContext = mContext;
        reference = FirebaseDatabase.getInstance().getReference("Foods");
        allViews=new ArrayList<>();
        eventListener=null;
        this.role=role;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View uView = inflater.from(parent.getContext()).inflate(R.layout.custom_bill, parent, false);
        ViewHolder viewHolder = new ViewHolder(uView);
        allViews.add(viewHolder);
        return viewHolder;
    }
    public List<ViewHolder> getAllHolder() {
        return allViews;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final OrderDetail orderDetail = orderDetailList.get(position);
            if (role.equals("waiter")){
                holder.getEtFoodQuantity().setEnabled(true);
            }
            else
                holder.getEtFoodQuantity().setEnabled(false);
            holder.setOrderDetailId(orderDetail.getId());
            int quantity = orderDetail.getQuantity();
            holder.getEtFoodQuantity().setText(quantity+"");
            reference.child(orderDetail.getFoodId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Food food = snapshot.getValue(Food.class);
                        int total = (int) (food.getPrice() * quantity);
                        holder.getTxtFoodName().setText(food.getName());
                        holder.getTxtFoodPrice().setText((int)food.getPrice()+"");
                        holder.getTxtFoodTotal().setText(total+ "");
                        finalSum += total;
                        reference = FirebaseDatabase.getInstance().getReference("SubTotals");
                        reference.child(orderDetail.getOrderId()).setValue(finalSum);
                        editFood(holder.getEtFoodQuantity(),food.getPrice(),holder.getTxtFoodTotal(),orderDetail.getOrderId());
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
        private String orderDetailId;

        public String getOrderDetailId() {
            return orderDetailId;
        }

        public void setOrderDetailId(String orderDetailId) {
            this.orderDetailId = orderDetailId;
        }

        public TextView getTxtFoodName() {
            return txtFoodName;
        }

        public void setTxtFoodName(TextView txtFoodName) {
            this.txtFoodName = txtFoodName;
        }

        public TextView getTxtFoodPrice() {
            return txtFoodPrice;
        }

        public void setTxtFoodPrice(TextView txtFoodPrice) {
            this.txtFoodPrice = txtFoodPrice;
        }

        public TextView getTxtFoodTotal() {
            return txtFoodTotal;
        }

        public void setTxtFoodTotal(TextView txtFoodTotal) {
            this.txtFoodTotal = txtFoodTotal;
        }

        public EditText getEtFoodQuantity() {
            return etFoodQuantity;
        }

        public void setEtFoodQuantity(EditText etFoodQuantity) {
            this.etFoodQuantity = etFoodQuantity;
        }

        private TextView txtFoodName,txtFoodPrice,txtFoodTotal;
        private EditText etFoodQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFoodName=itemView.findViewById(R.id.txtFoodName);
            txtFoodPrice=itemView.findViewById(R.id.txtFoodPrice);
            txtFoodTotal=itemView.findViewById(R.id.txtFoodTotal);
            etFoodQuantity=(EditText)itemView.findViewById(R.id.etFoodQantity);
        }
    }
    private  void editFood(EditText quantity,float price,TextView total,String orderId){

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String a=quantity.getText().toString();
                int totalPrice=0;
                if(!a.isEmpty()){
                    int oldTotal=Integer.parseInt(total.getText().toString());
                    totalPrice= (int)(new Integer(a).intValue()* price);
                    total.setText(totalPrice+"");
                    int newTotal=Integer.parseInt(total.getText().toString());
                    reference = FirebaseDatabase.getInstance().getReference("SubTotals").child(orderId);
                    eventListener=new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                int finalSum = snapshot.getValue(int.class);
                                finalSum +=newTotal-oldTotal;
                                reference.setValue(finalSum);
                                reference.removeEventListener(eventListener);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    };
                    reference.addValueEventListener(eventListener);
            }
        }
    });
}
}
