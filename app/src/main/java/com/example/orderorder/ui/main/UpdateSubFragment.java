package com.example.orderorder.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.orderorder.R;

import java.text.ParseException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateSubFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateSubFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DREF = "dref";

    private String drefString;


    public UpdateSubFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateSubFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateSubFragment newInstance(String param1, String param2) {
        UpdateSubFragment fragment = new UpdateSubFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DREF, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drefString = getArguments().getString(ARG_DREF);

        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.new_sub_menu, menu);


        //super.onCreateOptionsMenu(menu, inflater);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_sub:
                try {
                    if(  saveSub() ) {
                        getFragmentManager().popBackStack();
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return super.onOptionsItemSelected(item);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

    private boolean saveSub() {

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_sub, container, false);
    }
}