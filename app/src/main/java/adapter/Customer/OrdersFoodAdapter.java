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

public class OrdersFoodAdapter extends RecyclerView.Adapter<OrdersFoodAdapter.ViewHolder> {

    private Context mContext;
    private List<Food> foodList;
    private List<ViewHolder> allViews;

    public OrdersFoodAdapter(Context mContext, List<Food> foodList) {
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
                inflater.inflate(R.layout.custom_orders, parent, false);

        ViewHolder viewHolder = new ViewHolder(uView);
        allViews.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Food food = foodList.get(position);
        holder.foodId = food.getId();
        holder.tvFoodName.setText(food.getName());
        holder.add.setOnClickListener(v -> {
            int number = 0;
            try {
                number = Integer.parseInt(holder.numberFood.getText().toString());
            } catch (NumberFormatException e) {
                System.out.println("Could not parse " + e);
            }
            holder.numberFood.setText(String.valueOf(number + 1));
        });
        holder.remove.setOnClickListener(v -> {
            int number = 0;
            try {

                number = Integer.parseInt(holder.numberFood.getText().toString());

            } catch (NumberFormatException e) {
                System.out.println("Could not parse " + e);
            }
            if (number > 0) {
                holder.numberFood.setText(String.valueOf(number - 1));
            }

        });
        new DownloadImageTask((ImageView) (holder.imageView))
                .execute(food.getImage());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public String foodId;
        public ImageView imageView;
        public TextView tvFoodName;
        public EditText numberFood;
        public Button add, remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.foodOrderView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            numberFood = itemView.findViewById(R.id.txtNumberFood);
            add = itemView.findViewById(R.id.btnAdd);
            remove = itemView.findViewById(R.id.btnRemove);
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