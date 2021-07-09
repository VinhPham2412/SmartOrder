package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;

import java.security.AccessControlContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dao.FoodDAO;
import model.OrderDetail;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<List<OrderDetail>> list;
    private List<OrderDetail> postlist;
    private FoodDAO foodDAO;
    private Context context;
    public OrderHistoryAdapter(List<List<OrderDetail>> list ){
        this.list = list;
        foodDAO = new FoodDAO();
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
        postlist = list.get(position);
        Date time = postlist.get(0).getTime();
        for(OrderDetail orderDetail:postlist){
            String foodName = foodDAO.getFoodNameById(orderDetail.getFoodId());
            TextView textView = holder.getTxtFood();
            textView.setText(textView.getText()+"\n"+foodName + " : " + orderDetail.getQuantity());
        }
        DateFormat format = new SimpleDateFormat("dd/MM/YYYY HH:mm");
        holder.getTxtTime().setText(format.format(time));

        if(postlist.get(0).getIsAccepted()){
            holder.getTxtStatus().setTextColor(Color.GREEN);
            holder.getTxtStatus().setText("Đã duyêt");
        }else{
            holder.getTxtStatus().setTextColor(Color.RED);
            holder.getTxtStatus().setText("Chờ xác nhận");
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
