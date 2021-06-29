package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;

import java.util.List;

import model.Food;

public class OrdersFoodAdapter extends RecyclerView.Adapter<OrdersFoodAdapter.ViewHolder> {

    private Context mContext;
    private List<Food> foodList;

    public OrdersFoodAdapter(Context mContext, List<Food> foodList) {
        this.mContext = mContext;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View uView =
                inflater.inflate(R.layout.custom_orders, parent, false);

        ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Food food=foodList.get(position);
//        holder.imageView.setImageResource(R.drawable.bf2);
        holder.tvFoodName.setText(food.getName());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number=0;
                try {
                    number=Integer.parseInt(holder.numberFood.getText().toString());
                }catch (NumberFormatException e){
                    System.out.println("Could not parse " + e);
                }
                holder.numberFood.setText(String.valueOf(number+1));
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number=0;
                try {
                    number=Integer.parseInt(holder.numberFood.getText().toString());
                }catch (NumberFormatException e){
                    System.out.println("Could not parse " + e);
                }
                holder.numberFood.setText(String.valueOf(number-1));
            }
        });


    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
       public ImageView imageView;
       public TextView tvFoodName;
       public EditText numberFood;
       public Button add,remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           imageView=itemView.findViewById(R.id.foodOrderView);
           tvFoodName=itemView.findViewById(R.id.tvFoodName);
           numberFood=itemView.findViewById(R.id.txtNumberFood);
           add=itemView.findViewById(R.id.btnAdd);
           remove=itemView.findViewById(R.id.btnRemove);

        }
    }


}