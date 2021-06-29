package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.MainActivity;
import com.example.su21g3project.R;

import java.sql.SQLException;
import java.util.List;

import dao.FoodDAO;
import model.Buffet;
import model.Food;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<Buffet> buffetList;
    private List<Food> foodList;
    private Context mContext;

    public MenuAdapter(List<Buffet> buffetList, Context mContext) {
        this.buffetList = buffetList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View uView =
                inflater.inflate(R.layout.custom_menu, parent, false);

        ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Buffet buffet=buffetList.get(position);
        holder.buffet_name.setText("SET "+(int)buffet.getPrice()+" K");
        holder.buffet_description.setText(buffet.getDescription());
        foodList= new FoodDAO().getFoodsOfBuffet(buffet.getId());
        for (Food fod: foodList) {
            TextView textView=new TextView(mContext);
            textView.setText(". "+fod.getName());
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextSize(15);
            holder.gridLayout.addView(textView);
        }


    }

    @Override
    public int getItemCount() {
        return buffetList.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView buffet_name;
        public TextView buffet_description;
        public GridLayout gridLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buffet_name=itemView.findViewById(R.id.buffet_name);
            buffet_description=itemView.findViewById(R.id.bufet_description);
            gridLayout=itemView.findViewById(R.id.gridLayout);
        }
    }
}
