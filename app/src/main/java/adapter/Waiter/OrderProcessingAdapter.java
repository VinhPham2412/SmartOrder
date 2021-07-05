package adapter.Waiter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import adapter.BuffetAdapter;
import dao.FoodDAO;
import model.Food;
import model.OrderDetail;

public class OrderProcessingAdapter extends RecyclerView.Adapter<OrderProcessingAdapter.ViewHolder> {
    List<List<OrderDetail>> orderDetailList;
    Context mContext;
    FoodDAO foodDAO;
    DatabaseReference reference;
    List<String> ids;

    public OrderProcessingAdapter(List<List<OrderDetail>> orderDetailList, Context mContext) {
        this.orderDetailList = orderDetailList;
        this.mContext = mContext;
        foodDAO=new FoodDAO();
        reference = FirebaseDatabase.getInstance().getReference("OrderDetail");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View uView =
                inflater.inflate(R.layout.custom_getbuffet, parent, false);

        ViewHolder viewHolder =new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final List<OrderDetail> details = orderDetailList.get(position);
        ids = new ArrayList<>();
        holder.btnReject.setBackgroundColor(Color.RED);
        holder.btnAccept.setBackgroundColor(Color.GREEN);
        for (OrderDetail od: details) {
            String foodName=foodDAO.getFoodNameById(od.getFoodId());
            TextView textView=new TextView(mContext);
            textView.setText(foodName+ " : "+od.getQuantity());
            holder.gridView.addView(textView);
            ids.add(od.getId());
        }
        holder.btnAccept.setOnClickListener(v -> {
            //update isSeen and isAccepted
            //push data to rtdb
            for (String id:ids
                 ) {
                reference.child(id).child("isSeen").setValue(true).addOnCompleteListener(
                        task -> Log.println(Log.INFO,"Update to rtdb","Set seen ok"));
                reference.child(id).child("isAccepted").setValue(true).addOnCompleteListener(
                        task -> Log.println(Log.INFO,"Update to rtdb","Set accepted ok"));
            }
        });
        holder.btnReject.setOnClickListener(v -> {
            //update isSeen and isAccepted
            //push data to rtdb
            for (String id:ids
            ) {
                reference.child(id).child("isSeen").setValue(true).addOnCompleteListener(
                        task -> Log.println(Log.INFO,"Update to rtdb","Set reject ok"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public GridView gridView;
        public Button btnReject,btnAccept;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gridView=itemView.findViewById(R.id.gridview);
            btnAccept=itemView.findViewById(R.id.btnAccept);
            btnReject=itemView.findViewById(R.id.btnReject);
        }
    }
}
