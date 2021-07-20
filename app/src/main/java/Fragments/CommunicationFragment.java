package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.su21g3project.Customer.CommunicationCustomer;
import com.example.su21g3project.R;


public class CommunicationFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_communication, container, false);
//        startActivity(new Intent(getContext(), CommunicationCustomer.class));
        return view;

    }

}