package adapter.General;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;

import java.util.ArrayList;
import java.util.List;

public class CommunicationAdapter extends RecyclerView.Adapter<CommunicationAdapter.ViewHolder> {
    private List<String> list;
    private Context mContext;
    private List<ViewHolder> viewHolders;
    private EditText txtReason;

    public CommunicationAdapter(List<String> list, Context mContext, EditText reason) {
        this.list = list;
        this.mContext = mContext;
        viewHolders = new ArrayList<>();
        txtReason = reason;
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
        ViewHolder viewHolder = new ViewHolder(uView);
        viewHolders.add(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = list.get(position);
        holder.checkBox.setText(s);
        holder.checkBox.setOnClickListener(v -> {
            String itemSelected = "";
            for (CommunicationAdapter.ViewHolder viewh : viewHolders) {
                if (viewh.checkBox.isChecked()) {
                    itemSelected += viewh.checkBox.getText() + "\n";
                }
            }
            txtReason.setText(itemSelected);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
