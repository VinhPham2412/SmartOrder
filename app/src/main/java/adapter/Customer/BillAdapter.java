package adapter.Customer;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private List<OrderDetail> detailsView;
    private List<OrderDetail> detailsOriginal;
    private Context mContext;
    private DatabaseReference reference;
    private Float finalSum = 0f;
    private List<ViewHolder> allViews;
    private ValueEventListener eventListener;
    private String role;

    public BillAdapter(List<OrderDetail> orderDetailList, Context mContext, String role) {
        detailsOriginal = orderDetailList;
        /**
         * view different for each role
         */
        if (role.equals("customer")) {
            List<OrderDetail> detailForCustomer = orderDetailList;
            for (int i = 0; i < detailForCustomer.size() - 1; i++) {
                for (int j = i + 1; j < detailForCustomer.size(); j++) {
                    if (detailForCustomer.get(i).getFoodId()
                            .equals(detailForCustomer.get(j).getFoodId())) {
                        int oldQuantity = detailForCustomer.get(i).getQuantity();
                        int alphaQuantity = detailForCustomer.get(j).getQuantity();
                        detailForCustomer.get(i).setQuantity(oldQuantity + alphaQuantity);
                        detailForCustomer.remove(j);
                    }
                }
            }
            detailsView = detailForCustomer;
        } else {
            detailsView = detailsOriginal;
        }
        this.mContext = mContext;
        reference = FirebaseDatabase.getInstance().getReference("Foods");
        allViews = new ArrayList<>();
        eventListener = null;
        this.role = role;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderDetail orderDetail = detailsView.get(position);
        holder.getEtFoodQuantity().setEnabled(role.equals("waiter") ? true : false);
        int quantity = orderDetail.getQuantity();
        holder.getEtFoodQuantity().setText(quantity + "");
        reference.child(orderDetail.getFoodId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Food food = snapshot.getValue(Food.class);
                    int total = (int) (food.getPrice() * quantity);
                    holder.getTxtFoodName().setText(food.getName());
                    holder.getTxtFoodPrice().setText((int) food.getPrice() + "");
                    holder.getTxtFoodTotal().setText(total + "");
                    finalSum += total;
                    reference = FirebaseDatabase.getInstance().getReference("SubTotals");
                    reference.child(orderDetail.getOrderId()).setValue(finalSum);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.getEtFoodQuantity().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int totalPrice = 0;
                if (!s.toString().isEmpty()) {
                    if (s.length() >= 3) {
                        Toast.makeText(mContext, "Số lượng sửa quá lớn, vui  lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                        holder.getEtFoodQuantity().setText(orderDetail.getQuantity() + "");
                    } else {
                        orderDetail.setQuantity(Integer.valueOf(s.toString()).intValue());
                        int oldTotal = Integer.parseInt(holder.getTxtFoodTotal().getText().toString());
                        totalPrice = new Integer(s.toString()) * Integer.parseInt(holder.txtFoodPrice.getText().toString());
                        holder.getTxtFoodTotal().setText(totalPrice + "");
                        int newTotal = Integer.parseInt(holder.getTxtFoodTotal().getText().toString());
                        reference = FirebaseDatabase.getInstance().getReference("SubTotals").child(orderDetail.getOrderId());
                        eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    int finalSum = snapshot.getValue(int.class);
                                    finalSum += newTotal - oldTotal;
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
            }
        });


    }

    @Override
    public int getItemCount() {
        return detailsView.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

        private TextView txtFoodName, txtFoodPrice, txtFoodTotal;
        private EditText etFoodQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtFoodPrice = itemView.findViewById(R.id.txtFoodPrice);
            txtFoodTotal = itemView.findViewById(R.id.txtFoodTotal);
            etFoodQuantity = itemView.findViewById(R.id.etFoodQantity);
        }
    }

    public List<OrderDetail> getDetails() {
        return role.equals("customer")?detailsOriginal: detailsView;
    }
}
