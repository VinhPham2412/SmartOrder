package adapter.Customer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.Food;

public class OrdersFoodMoneyAdapter extends RecyclerView.Adapter<OrdersFoodMoneyAdapter.ViewHolder>{
    private Context mContext;
    private List<Food> foodList;
    private List<ViewHolder> allViews;

    public OrdersFoodMoneyAdapter(Context mContext, List<Food> foodList) {
        this.mContext = mContext;
        this.foodList = foodList;
        allViews = new ArrayList<>();
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
        final Food foodMoney=foodList.get(position);
        holder.tvFoodNameMoney.setText(foodMoney.getName());
        holder.foodId = foodMoney.getId();
        holder.tvPrice.setText((int)(foodMoney.getPrice())+"K");
        holder.addMoney.setOnClickListener(v -> {
            int number=0;
            try {
                number=Integer.parseInt(holder.numberFoodMoney.getText().toString());
            }catch (NumberFormatException e){
                System.out.println("Could not parse " + e);
            }
            holder.numberFoodMoney.setText(String.valueOf(number+1));
        });
        holder.removeMoney.setOnClickListener(v -> {
            int number=0;
            try {
                number=Integer.parseInt(holder.numberFoodMoney.getText().toString());
            }catch (NumberFormatException e){
                System.out.println("Could not parse " + e);
            }
            if(number>0) {
                holder.numberFoodMoney.setText(String.valueOf(number - 1));
            }
        });
        new DownloadImageTask((ImageView) (holder.imageViewMoney))
                .execute(foodMoney.getImage());
        allViews.add(holder);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public List<ViewHolder> getAllHolder() {
        return allViews;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public String foodId;
        public ImageView imageViewMoney;
        public TextView tvFoodNameMoney,tvPrice;
        public EditText numberFoodMoney;
        public Button addMoney,removeMoney;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMoney=itemView.findViewById(R.id.foodOrderViewMoney);
            tvFoodNameMoney=itemView.findViewById(R.id.tvFoodNameMoney);
            numberFoodMoney=itemView.findViewById(R.id.txtNumberFoodMoney);
            addMoney=itemView.findViewById(R.id.btnAddMoney);
            removeMoney=itemView.findViewById(R.id.btnRemoveMoney);
            tvPrice=itemView.findViewById(R.id.tvMoney);
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
