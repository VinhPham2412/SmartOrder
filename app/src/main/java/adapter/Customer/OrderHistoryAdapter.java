package adapter.Customer;

import android.content.Context;
import android.graphics.Color;
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

import org.jetbrains.annotations.NotNull;

import java.util.List;

import model.Food;
import model.OrderDetail;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<List<OrderDetail>> list;
    private List<OrderDetail> postlist;
    private Context context;
    private String foodName;
    public OrderHistoryAdapter(List<List<OrderDetail>> list ){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View uView =
                inflater.inflate(R.layout.order_history, parent, false);

        return new OrderHistoryAdapter.ViewHolder(uView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //show data
        if(!list.isEmpty()){
            postlist = list.get(position);
            for(OrderDetail orderDetail:postlist){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Food").child(orderDetail.getFoodId());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Food food = snapshot.getValue(Food.class);
                            foodName = food.getName();
                            TextView textView = holder.getTxtFood();
                            textView.setText(textView.getText()+"\n"+foodName + " : " + orderDetail.getQuantity());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
            holder.getTxtTime().setText(postlist.get(0).getTimeString());

            if(postlist.get(0).getIsAccepted()){
                holder.getTxtStatus().setTextColor(Color.GREEN);
                holder.getTxtStatus().setText("Đã duyêt");
            }else{
                holder.getTxtStatus().setTextColor(Color.RED);
                holder.getTxtStatus().setText("Chờ xác nhận");
            }
        }else{
            TextView textView = holder.getTxtFood();
            textView.setText("Nothing here, turn back.");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTime;
        private TextView txtFood;
        private TextView txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtHistoryTime);
            txtFood = itemView.findViewById(R.id.txtHistoryFood);
            txtStatus = itemView.findViewById(R.id.txtHistoryStatus);
            txtFood.setText("");
        }

        public TextView getTxtTime() {
            return txtTime;
        }

        public TextView getTxtFood() {
            return txtFood;
        }

        public TextView getTxtStatus() {
            return txtStatus;
        }
    }
}