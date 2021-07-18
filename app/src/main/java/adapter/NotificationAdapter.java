package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.GetBuffetActivity;
import com.example.su21g3project.R;

import java.util.List;

import model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context mContext;
    private List<Notification> list;

    public NotificationAdapter(Context mContext, List<Notification> notifications) {
        this.mContext = mContext;
        this.list = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View uView =
                inflater.inflate(R.layout.custom_orders_money, parent, false);

        ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Notification notification=list.get(position);
        holder.txtNotice.setText(notification.getMessage());
        holder.itemView.setOnClickListener(v -> {
            int index=holder.getAdapterPosition();
            Notification notification1=list.get(index);
            if (notification1.getType()==1){
                Intent intent1=new Intent(mContext, GetBuffetActivity.class);
                mContext.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNotice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNotice=itemView.findViewById(R.id.txtNotice);

        }
    }
}
