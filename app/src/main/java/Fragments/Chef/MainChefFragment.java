package Fragments.Chef;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su21g3project.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.OrderDetail;
import adapter.Chef.ChefAdapter;

public class MainChefFragment extends Fragment {
    DatabaseReference reference;
    private RecyclerView recyclerView;
    private List<OrderDetail> list;
    private ChefAdapter chefAdapter;
    private TextView txtChefName;
    FirebaseUser user;
    private String role="";
    private int dvHeight;


    public MainChefFragment(FragmentActivity fragmentActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        fragmentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dvHeight = displayMetrics.heightPixels;
    }
    // TODO: Rename and change types and number of parameters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_chef, container, false);
        recyclerView=view.findViewById(R.id.chefRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("OrderDetails");
        /**
         * get all OrderDetail with accepted and have't done
         */
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    OrderDetail orderDetail=dataSnapshot.getValue(OrderDetail.class);
                    if (orderDetail.getStatus().equals("accepted")){
                        list.add(orderDetail);
                    }
                }
                chefAdapter=new ChefAdapter(list,getContext());
                recyclerView.setAdapter(chefAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}