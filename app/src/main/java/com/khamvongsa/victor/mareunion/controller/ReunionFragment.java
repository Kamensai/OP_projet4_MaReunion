package com.khamvongsa.victor.mareunion.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khamvongsa.victor.mareunion.R;
import com.khamvongsa.victor.mareunion.model.Salle;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.graphics.Color.RED;


public class ReunionFragment extends Fragment {

    private Date mDateDebut = new Date(2019/10/22);
    private Date mDateFin = new Date(2019/10/23);
    private List<String> mparticipants = Arrays.asList("Jean", "Baptiste");
    private Salle mMario = new Salle (0, "Mario", RED);

    public ExempleReunion mReunion;
    private RecyclerView mRecyclerView;

    public static ReunionFragment newInstance() {
        return new ReunionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReunion = new ExempleReunion(0,mDateDebut, mDateFin, mMario, "Essai", mparticipants);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_reunion, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.setAdapter(new MyReunionRecyclerViewAdapter(mReunion));
    }

}
