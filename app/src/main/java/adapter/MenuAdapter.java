package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.Buffet;
import model.Food;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<Buffet> buffetList;
    private List<Food> foods;
    private Context mContext;
    private DatabaseReference reference;

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
        //get food id in this buffet
         foods= new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Buffet").child(buffet.getId()).child("foods");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot snap:snapshot.getChildren()){
                    Food food = snap.getValue(Food.class);
                    foods.add(food);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        for (Food fod: foods) {
            TextView textView=new TextView(mContext);
            textView.setText(". "+fod.getName());
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextSize(15);
            holder.gridLayout.addView(textView);
        }
        new DownloadImageTask((ImageView) (holder.imageViewMenu))
                .execute(buffet.getImage());


    }

    @Override
    public int getItemCount() {
        return buffetList.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView buffet_name;
        public TextView buffet_description;
        public GridLayout gridLayout;
        public ImageView imageViewMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buffet_name=itemView.findViewById(R.id.buffet_name);
            buffet_description=itemView.findViewById(R.id.bufet_description);
            gridLayout=itemView.findViewById(R.id.gridLayout);
            imageViewMenu=itemView.findViewById(R.id.imageViewMenu);
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
