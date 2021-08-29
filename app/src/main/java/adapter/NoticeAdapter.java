package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.su21g3project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Model.Notice;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private List<Notice>  noticeList;
    private Context mContext;
    private String role;
    private DatabaseReference reference;
    private ViewBinderHelper viewBinderHelper=new ViewBinderHelper();

    public NoticeAdapter(List<Notice> noticeList, Context mContext,String role) {
        this.noticeList = noticeList;
        this.mContext = mContext;
        reference= FirebaseDatabase.getInstance().getReference("Communications").child(role);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View uView =
                inflater.inflate(R.layout.custom_notice, parent, false);

       ViewHolder viewHolder = new ViewHolder(uView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notice notice=noticeList.get(position);
        holder.txtYourMessage.setText(notice.getMessage());
        String reply = notice.getMessageReply();
        if(reply!=null){
            holder.txtMessage.setText(reply);
        }
        viewBinderHelper.bind(holder.swipeRevealLayout,notice.getId());
        holder.layoutDelete.setOnClickListener(v -> reference.child(notice.getId()).child("isSeen").setValue(true));

    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtMessage,txtYourMessage;
        public SwipeRevealLayout swipeRevealLayout;
        public LinearLayout layoutDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage=itemView.findViewById(R.id.txtMessage);
            swipeRevealLayout=itemView.findViewById(R.id.SwipeRevealLayout);
            layoutDelete=itemView.findViewById(R.id.layoutDelete);
            txtYourMessage=itemView.findViewById(R.id.txtYourMessgae);
        }
    }
}
