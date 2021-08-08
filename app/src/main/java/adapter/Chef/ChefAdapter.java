package adapter.Chef;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import Model.Food;
import Model.OrderDetail;
/**
 * Adapter for
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ChefAdapter extends RecyclerView.Adapter<ChefAdapter.ViewHolder> {
    private List<OrderDetail> orderDetailList;
    private Context mContext;
    private DatabaseReference reference;

    public ChefAdapter(List<OrderDetail> orderDetailList, Context mContext) {
        this.orderDetailList = orderDetailList;
        this.mContext = mContext;
        reference= FirebaseDatabase.getInstance().getReference("Foods");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View uView = inflater.from(parent.getContext()).inflate(R.layout.custom__main_chef, parent, false);
        ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get current orderDetail and display info
        final OrderDetail orderDetail=orderDetailList.get(position);
        holder.getTxtQuantity().setText(orderDetail.getQuantity()+"");
        reference=FirebaseDatabase.getInstance().getReference("Foods").child(orderDetail.getFoodId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Food food=snapshot.getValue(Food.class);
                    holder.getTxtFoodName().setText(food.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.getBtnDone().setOnClickListener(v -> {
            reference=FirebaseDatabase.getInstance().getReference("OrderDetails")
                    .child(orderDetail.getId()).child("status");
            reference.setValue("cooked").addOnCompleteListener(task ->
                    Log.println(Log.INFO, "cooking", "Cooked"));
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }
    /**
     * View holder for ChefAdapter.
     * Define components for each adapter view
     */
    protected class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtFoodName,txtQuantity;
        private ImageButton btnDone;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFoodName=itemView.findViewById(R.id.txtFoodName);
            txtQuantity=itemView.findViewById(R.id.txtQuantity);
            btnDone=itemView.findViewById(R.id.btnDone);
        }

        public TextView getTxtFoodName() {
            return txtFoodName;
        }

        public TextView getTxtQuantity() {
            return txtQuantity;
        }

        public ImageButton getBtnDone() {
            return btnDone;
        }
    }

}

