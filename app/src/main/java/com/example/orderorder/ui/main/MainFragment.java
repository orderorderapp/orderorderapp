package com.example.orderorder.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.orderorder.R;
import com.example.orderorder.SessionManager;
import com.example.orderorder.adapters.SubAdapter;
import com.example.orderorder.models.mainData.Sub;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainFragment extends Fragment {

    SessionManager sessionManager;

    private AppCompatActivity activity;
    private Toolbar toolbar;
    private FirebaseFirestore fs = FirebaseFirestore.getInstance();
    private CollectionReference subsRef;
    private SubAdapter adapter;
    FloatingActionButton addSubButton;

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        addSubButton = v.findViewById(R.id.button_add_sub);

        activity = (AppCompatActivity) getActivity();
        sessionManager = new SessionManager(activity.getApplicationContext());
        subsRef = fs.collection("subs/"+sessionManager.getUid()+"/userSubs");
        toolbar = v.findViewById(R.id.toolbar);

        //toolbar.setTitle("Lisää uusi tilaus");
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        return v;
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);


        //super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }

 */

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
        setUpRecyclerView();

        addSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getFragmentManager().beginTransaction()
                        .add(R.id.container, NewSubFragment.newInstance("ph1", "ph2"))
                        .commitNow();*/

                Fragment newSubFrag = new NewSubFragment();
                FragmentTransaction transaction= getFragmentManager().beginTransaction();

                transaction.replace(R.id.container, newSubFrag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


    }

    private void setUpRecyclerView() {
        Query query = subsRef.orderBy("price", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Sub> options = new FirestoreRecyclerOptions.Builder<Sub>()
                .setQuery(query, Sub.class)
                .build();
        adapter = new SubAdapter(options);

        RecyclerView recyclerView = getActivity().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnSubItemClickListener(new SubAdapter.OnSubItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Log.d("SubListener", "painettu");
                String dref =  documentSnapshot.getId();

                Fragment updateSubFrag = new NewSubFragment();

                Bundle args = new Bundle();
                args.putString("dref", dref);
                updateSubFrag.setArguments(args);

                FragmentTransaction transaction= getFragmentManager().beginTransaction();

                transaction.replace(R.id.container, updateSubFrag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
