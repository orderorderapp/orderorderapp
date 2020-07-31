package com.example.orderorder.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.orderorder.R;
import com.example.orderorder.SessionManager;

import javax.security.auth.callback.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//Fragmentti laskee tilausten vuosikulun ja esittää sen käyttäjälle
public class PlanFragment extends Fragment {
    SessionManager sessionManager;

    public void goBack() {
        getFragmentManager().popBackStack();
    }

    private AppCompatActivity activity;
    private Toolbar toolbar;
    private TextView totalSumTextView;
    private String totalSum = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanFragment newInstance(String param1, String param2) {
        PlanFragment fragment = new PlanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

       // setHasOptionsMenu(true);




    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);


        //super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plan, container, false);

        activity = (AppCompatActivity) getActivity();
        sessionManager = new SessionManager(activity.getApplicationContext());

        toolbar = v.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.menu_baseline_clear_close);




        toolbar.setTitle("Vuosinäkymä");
        //activity.setSupportActionBar(toolbar);

        setHasOptionsMenu(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("navigationClicker", "painettu close");
                goBack();
            }
        });




        totalSumTextView = v.findViewById(R.id.totalSum);

        PlanCalculation planCalculation = new PlanCalculation(sessionManager.getUid());

        planCalculation.calculateYearTotal(new getTotalSumYear() {

            @Override
            public void onComplete(String sum) {
                totalSumTextView.setText(sum+"€");
            }
        }  );



        // Inflate the layout for this fragment
        return v;
    }





}