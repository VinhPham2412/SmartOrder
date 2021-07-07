package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;

import java.io.InputStream;
import java.util.List;

import dao.BuffetDAO;
import model.Buffet;

public class BuffetAdapter extends RecyclerView.Adapter<BuffetAdapter.ViewHolder> {
    private int checkedPosition=0;
    private Context mContext;
    private List<Buffet> list;
    private BuffetDAO buffetDAO;

    public BuffetAdapter(Context mContext, List<Buffet> list) {
        this.mContext = mContext;
        this.list = list;
        buffetDAO=new BuffetDAO();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View uView =
                inflater.inflate(R.layout.custom_getbuffet, parent, false);

        ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final Buffet buffet=list.get(position);
        holder.txtBufffetName.setText(buffet.getName()+" "+(int)(buffet.getPrice())+"K");
        holder.txtBuffetPrice.setText(buffet.getDescription());
        if(checkedPosition==-1){
            holder.imageView.setVisibility(View.GONE);
        }else{
            if(checkedPosition==holder.getAdapterPosition()){
                holder.imageView.setVisibility(View.VISIBLE);
            }else{
                holder.imageView.setVisibility(View.GONE);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageView.setVisibility(View.VISIBLE);
                if(checkedPosition!=holder.getAdapterPosition()){
                    notifyItemChanged(checkedPosition);
                    checkedPosition=holder.getAdapterPosition();
                }
            }
        });

        new DownloadImageTask((ImageView) (holder.imageViewBuffet))
                .execute(buffet.getImage());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageViewBuffet;
        public TextView txtBufffetName,txtBuffetPrice;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBuffet=itemView.findViewById(R.id.imageViewBuffet);
            txtBufffetName=itemView.findViewById(R.id.txtBuffetName);
            txtBuffetPrice=itemView.findViewById(R.id.txtBuffetPrice);
            imageView=itemView.findViewById(R.id.imageViewChecked);

        }
    }
    public Buffet getSelected(){
        if (checkedPosition!=-1){
            return list.get(checkedPosition);
        }
        return null;
    }
    private   class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
