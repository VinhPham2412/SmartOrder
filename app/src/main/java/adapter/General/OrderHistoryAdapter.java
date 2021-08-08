package adapter.General;

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

import Model.Food;
import Model.OrderDetail;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<List<OrderDetail>> list;
    private List<OrderDetail> details;
    private Context context;
    private String foodName;
    private String orderId;
    private  DatabaseReference reference;
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
        //show data of each food
        if(!list.isEmpty()){
            details = list.get(position);
            for(OrderDetail orderDetail: details){
                orderId = orderDetail.getOrderId();
                reference = FirebaseDatabase.getInstance().getReference("Foods").child(orderDetail.getFoodId());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Food food = snapshot.getValue(Food.class);
                            foodName = food.getName();
                            TextView textView = holder.getTxtFood();
                            String status =orderDetail.getStatus();
                            switch (status){
                                case "accepted":
                                    textView.setText(textView.getText()+"\n"+foodName + " : " + orderDetail.getQuantity()+ " - accepted");
                                    break;
                                case "new":
                                    textView.setText(textView.getText()+"\n"+foodName + " : " + orderDetail.getQuantity()+ " - waiting accept");
                                    break;
                                case "cooked":
                                    textView.setText(textView.getText()+"\n"+foodName + " : " + orderDetail.getQuantity()+ " - cooked");
                                    break;
                                case "delivered":
                                    textView.setText(textView.getText()+"\n"+foodName + " : " + orderDetail.getQuantity()+ " - delivered");
                                    break;
                                case "rejected":
                                    textView.setText(textView.getText()+"\n"+foodName + " : " + orderDetail.getQuantity()+ " - rejected");
                                    holder.getTxtStatus().setText(context.getString(R.string.rejected)+"\n"+orderDetail.getReason());
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
            holder.getTxtTime().setText(details.get(0).getTimeString());
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
