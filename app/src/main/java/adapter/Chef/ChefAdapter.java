package adapter.Chef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import adapter.Customer.BillAdapter;
import model.Food;
import model.OrderDetail;
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
        reference= FirebaseDatabase.getInstance().getReference("Food");
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
        final OrderDetail orderDetail=orderDetailList.get(position);
        holder.txtQuantity.setText(orderDetail.getQuantity()+"");
        reference=FirebaseDatabase.getInstance().getReference("Food").child(orderDetail.getFoodId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Food food=snapshot.getValue(Food.class);
                    holder.txtFoodName.setText(food.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.btnDone.setOnClickListener(v -> {
            reference=FirebaseDatabase.getInstance().getReference("OrderDetail")
                    .child(orderDetail.getId()).child("doing");
            reference.setValue(true);
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
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtFoodName,txtQuantity;
        public ImageButton btnDone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFoodName=itemView.findViewById(R.id.txtFoodName);
            txtQuantity=itemView.findViewById(R.id.txtQuantity);
            btnDone=itemView.findViewById(R.id.btnDone);
        }
    }

}

