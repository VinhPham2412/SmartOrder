package adapter.General;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;

import java.util.ArrayList;
import java.util.List;

public class CommunicationAdapter  extends RecyclerView.Adapter<CommunicationAdapter.ViewHolder>{
    private List<String> list;
    private Context mContext;
    private List<ViewHolder> viewHolders;

    public CommunicationAdapter(List<String> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        viewHolders=new ArrayList<>();

    }

    public List<ViewHolder> getViewHolders() {
        return viewHolders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View uView =
                inflater.inflate(R.layout.custom_reason_communication, parent, false);
        ViewHolder viewHolder=new ViewHolder(uView);
        viewHolders.add(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s=list.get(position);
        holder.txtReason.setText(s);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtReason;
        public CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReason=itemView.findViewById(R.id.txtReason);
            checkBox=itemView.findViewById(R.id.checkBox);
        }
    }
}
