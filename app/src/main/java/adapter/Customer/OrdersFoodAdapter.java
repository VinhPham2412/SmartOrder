package adapter.Customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.General.DownloadImageTask;
import com.example.su21g3project.R;

import java.util.ArrayList;
import java.util.List;

import Model.Food;

public class OrdersFoodAdapter extends RecyclerView.Adapter<OrdersFoodAdapter.ViewHolder> {

    private Context mContext;
    private List<Food> foodList;
    private List<ViewHolder> allViews;
    private boolean money;

    public OrdersFoodAdapter(Context mContext, List<Food> foodList, boolean money) {
        this.mContext = mContext;
        this.foodList = foodList;
        allViews = new ArrayList<>();
        this.money = money;
        setHasStableIds(true);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);


        View uView =
                inflater.inflate(R.layout.custom_orders_money, parent, false);

        ViewHolder viewHolder = new ViewHolder(uView);
        allViews.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Food food = foodList.get(position);
        holder.setFoodId(food.getId());
        holder.getTvFoodName().setText(food.getName());
        if(money){
            holder.getTvPrice().setText((int)food.getPrice()+"");
        }
        holder.getAdd().setOnClickListener(v -> {
            int number = 0;
            try {
                number = Integer.parseInt(holder.getNumberFood().getText().toString());
            } catch (NumberFormatException e) {
                System.out.println("Could not parse " + e);
            }
            if (number<5)
            holder.getNumberFood().setText(String.valueOf(number + 1));
            else
                Toast.makeText(mContext, "Xin lỗi, tối đa cho 1 món là 5 đĩa!",
                        Toast.LENGTH_SHORT).show();
        });
        holder.getRemove().setOnClickListener(v -> {
            int number = 0;
            try {

                number = Integer.parseInt(holder.getNumberFood().getText().toString());

            } catch (NumberFormatException e) {
                System.out.println("Could not parse " + e);
            }
            if (number > 0) {
                holder.getNumberFood().setText(String.valueOf(number - 1));
            }

        });
        new DownloadImageTask((ImageView) (holder.getImageView()))
                .execute(food.getImage());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private String foodId;
        private TextView tvPrice;
        private ImageView imageView;
        private TextView tvFoodName;
        private EditText numberFood;
        private ImageButton add, remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.foodOrderViewMoney);
            tvFoodName = itemView.findViewById(R.id.tvFoodNameMoney);
            numberFood = itemView.findViewById(R.id.txtNumberFoodMoney);
            add = itemView.findViewById(R.id.btnAddMoney);
            remove = itemView.findViewById(R.id.btnRemoveMoney);
            tvPrice = itemView.findViewById(R.id.tvMoney);
        }

        public TextView getTvPrice() {
            return tvPrice;
        }

        public String getFoodId() {
            return foodId;
        }

        public void setFoodId(String foodId) {
            this.foodId = foodId;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTvFoodName() {
            return tvFoodName;
        }

        public EditText getNumberFood() {
            return numberFood;
        }

        public ImageButton getAdd() {
            return add;
        }

        public ImageButton getRemove() {
            return remove;
        }
    }

    public List<ViewHolder> getAllHolder() {
        List<ViewHolder> result = new ArrayList<>();
        for (ViewHolder holder : allViews) {

                if (Integer.parseInt(holder.numberFood.getText().toString()) > 0) {
                    result.add(holder);

            }
        }
        return result;
    }


}