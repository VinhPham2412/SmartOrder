package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.su21g3project.R;

import java.util.ArrayList;
import java.util.List;

import adapter.BookedHistoryAdapter;
import adapter.OrderHistoryAdapter;
import model.OrderDetail;
import model.ProcessOrder;


public class HistoryOrderFragment extends Fragment {
    public HistoryOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.order_history, container, false);
    }
}