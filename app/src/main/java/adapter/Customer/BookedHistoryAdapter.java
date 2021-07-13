package adapter.Customer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.GetBuffetActivity;
import com.example.su21g3project.R;

import java.util.List;

import model.ProcessOrder;

public class BookedHistoryAdapter extends RecyclerView.Adapter<BookedHistoryAdapter.ViewHolder> {
    private Context context;
    private List<ProcessOrder> list;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtText;
        private final TextView txtStatus;
        private final Button btngetFood;
        public ViewHolder(View itemView) {
            super(itemView);
            txtText = itemView.findViewById(R.id.txtBookedHistory);
            txtStatus = itemView.findViewById(R.id.txtBHStatus);
            btngetFood = itemView.findViewById(R.id.btnGetFood);
        }

        public TextView getTxtText() {
            return txtText;
        }

        public TextView getTxtStatus() {
            return txtStatus;
        }

        public Button getBtngetFood() {
            return btngetFood;
        }
    }
    public BookedHistoryAdapter(List<ProcessOrder> list,Context context){
        this.list = list;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View uView =
                inflater.inflate(R.layout.booked_history, parent, false);

        return new ViewHolder(uView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProcessOrder order = list.get(position);
        holder.getTxtText().setText("Người đặt : "+order.getName()+
                "\nSố điện thoại : "+order.getPhone()+
                "\nGiờ ăn : "+order.getDate().toString()+
                "\nSố người ăn : "+order.getNumberOfPeople()+
                "\nGhi chú : "+order.getNote());
        if (order.getIsVerify() && order.isStatus()) {
            holder.getTxtStatus().setText("Đã xác nhận");
            holder.getTxtStatus().setTextColor(Color.GREEN);
        }
        else if (order.getIsVerify() && !order.isStatus()) {
            holder.getTxtStatus().setText("Đã Từ chối");
            holder.getTxtStatus().setTextColor(Color.RED);
        }else {
            holder.getTxtStatus().setText("Chưa xác nhận");
            holder.getTxtStatus().setTextColor(Color.RED);
        }
        if(!order.getIsVerify()){
            holder.getBtngetFood().setVisibility(View.INVISIBLE);
        }
        holder.getBtngetFood().setOnClickListener(v -> {
            Intent intent = new Intent(context, GetBuffetActivity.class);
            intent.putExtra("orderId",order.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
