package adapter.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    public BillAdapter(List<OrderDetail> orderDetailList, Context mContext) {
        this.orderDetailList = orderDetailList;
        this.mContext = mContext;
        reference= FirebaseDatabase.getInstance().getReference("Food");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View uView =
                inflater.inflate(R.layout.custom_bill, parent, false);

        ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderDetail orderDetail=orderDetailList.get(position);
        Food food=getFood(orderDetail.getFoodId());
        holder.txtFoodName.setText(food.getName());
        holder.txtFoodPrice.setText(String.valueOf(food.getPrice()));
        holder.txtFoodQuantity.setText(orderDetail.getQuantity());
        holder.txtTotalMoney.setText(String.valueOf((int)food.getPrice()*orderDetail.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtFoodName,txtFoodQuantity,txtFoodPrice,txtTotalMoney;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFoodName=itemView.findViewById(R.id.txtFoodName);
            txtFoodQuantity=itemView.findViewById(R.id.txtFoodQuantity);
            txtFoodPrice=itemView.findViewById(R.id.txtFoodPrice);
            txtTotalMoney=itemView.findViewById(R.id.txtTotalMoney);

        }
    }
    private Food getFood(String foodId){
        final Food[] food = new Food[1];
        reference.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                food[0] =snapshot.getValue(Food.class);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       return  food[0];
    }
}
