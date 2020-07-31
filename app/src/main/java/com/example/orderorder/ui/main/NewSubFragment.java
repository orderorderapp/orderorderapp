package com.example.orderorder.ui.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.orderorder.R;
import com.example.orderorder.SessionManager;
import com.example.orderorder.models.mainData.Sub;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewSubFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewSubFragment extends Fragment {


    private DocumentReference dref;
    private FirebaseFirestore fs = FirebaseFirestore.getInstance();
    SessionManager sessionManager;

    private EditText editTextTitle;
    private EditText editTextPrice;
    private Date billingDueDate;
    private DatePicker datePicker;
    private AppCompatActivity activity;
    private Toolbar toolbar;
    private NumberPicker billingIntervalPicker;
    private LinearLayout billingIntervalCustomParent;
    private NumberPicker billingIntervalCustom1;
    private NumberPicker billingIntervalCustom2;
    private String[] billingIntervalPickerVals = new String[]{"kuukausittain", "vuosittain", "muu aikaväli"};
    private String[] billingIntervalPickerCustomVals = new String[]{"päivän välein", "kuukauden välein"};
    private int[] billingIntervalSelected = new int[]{1, 1}; //billingIntervalSelected {a, b} a = "kuinka usein", b = 0=päivä, 1=kuukausi 2=vuosi. Oletusarvo kuukausittain
    private NumberPicker foCIntervalPicker;
    private LinearLayout foCIntervalCustomParent;
    private NumberPicker foCIntervalCustom1;
    private NumberPicker foCIntervalCustom2;
    private String[] foCIntervalPickerVals = new String[]{ "-", "kuukausi", "vuosi", "muu pituus"}; //eka arvo tyhjä koska pickerin range on 1-3
    private String[] foCIntervalPickerCustomValsSingle = new String[]{"päivä", "kuukausi"};
    private String[] foCIntervalPickerCustomValsMulti = new String[]{"päivää", "kuukautta"};
    private String[] BillingIntervalType = new String[]  {
            "DAY", "MONTH", "YEAR", "na"
    };

    private int[] foCIntervalSelected = new int[]{0, 3}; //billingIntervalSelected {a, b} a = "kuinka kauan", b = 0=päivä, 1=kuukausi 2=vuosi, 3 = na. Oletusarvo "-"


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DREF = "dref";

    private String drefString = "";


    public NewSubFragment() {
        // Required empty public constructor
    }

    private void goBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment NewSubFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewSubFragment newInstance(String param1) {
        NewSubFragment fragment = new NewSubFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DREF, param1);

        //fragment.setArguments(args);
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
                if (getArguments() == null) {
                    try {
                        if (saveSub()) {
                            getFragmentManager().popBackStack();
                            return true;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return super.onOptionsItemSelected(item);
                    }
                } else if(getArguments() != null) {
                    if (updateSub()) {

                        getFragmentManager().popBackStack();
                        return true;
                    }


                }


            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }


    private boolean updateSub() {

        Map<String, Object> sub = new HashMap<>();
        sub.put("title", editTextTitle.getText().toString());
        try {
            double price = Double.parseDouble(editTextPrice.getText().toString());
            sub.put("price", price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String billingDate = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();

        try {
            billingDueDate = new SimpleDateFormat("dd/MM/yyyy").parse(billingDate);
            sub.put("billingDueDate", billingDueDate );
        } catch (ParseException e) {
            e.printStackTrace();
        }



        sub.put("billingIntervalInt", billingIntervalSelected[0]);
        sub.put("billingIntervalPeriod", BillingIntervalType[billingIntervalSelected[1]]);
        sub.put("foCIntervalInt", foCIntervalSelected[0]);
        sub.put("foCIntervalPeriod", BillingIntervalType[foCIntervalSelected[1]]);
        dref.update(sub);

        return  true;
    }

    private boolean saveSub() throws ParseException {
        String title = editTextTitle.getText().toString();
        String priceString = editTextPrice.getText().toString();


        //SimpleDateFormat billingDueDate = new SimpleDateFormat("dd/MM/yyyy");
        //billingDueDate.format(datePicker.getDayOfMonth() +"/"+datePicker.getMonth()+"/"+datePicker.getYear());
        String billingDate = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();

        billingDueDate = new SimpleDateFormat("dd/MM/yyyy").parse(billingDate);


        if (title.trim().isEmpty() || priceString.trim().isEmpty()) {
            //Toast.makeText(, "Syötä tilauksen nimi ja hinta");
            Log.d("UusiSub", "Was empty");
            return false;
        }
        try {
            Log.d("UusiSub", "Jatkui");
            double price = Double.parseDouble(priceString);
            CollectionReference subsref = fs
                    .collection("subs/"+sessionManager.getUid()+"/userSubs");
            subsref.add(new Sub(title, price, billingDueDate, billingIntervalSelected, foCIntervalSelected));
            //Toast.makeText(, "Syötä tilauksen nimi ja hinta");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        return true;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_sub, container, false);

        activity = (AppCompatActivity) getActivity();
        sessionManager = new SessionManager(activity.getApplicationContext());

        editTextTitle = v.findViewById(R.id.edit_text_title);
        editTextPrice = v.findViewById(R.id.edit_text_price);
        datePicker = v.findViewById(R.id.date_picker_due_date);

        billingIntervalPicker = v.findViewById(R.id.billingIntervalPicker);
        billingIntervalCustomParent = v.findViewById(R.id.billingIntervalCustomParent);
        billingIntervalCustom1 = v.findViewById(R.id.billingIntervalCustom1);
        billingIntervalCustom2 = v.findViewById(R.id.billingIntervalCustom2);

        foCIntervalPicker = v.findViewById(R.id.FoCPicker);
        foCIntervalCustomParent = v.findViewById(R.id.FoCPickerCustomParent);
        foCIntervalCustom1 = v.findViewById(R.id.FoCPickerCustom1);
        foCIntervalCustom2 = v.findViewById(R.id.FoCPickerCustom2);


        toolbar = v.findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.menu_baseline_clear_close);

        toolbar.setTitle("Lisää uusi tilaus");
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        //billingIntervaliPicker:::
        billingIntervalPicker.setMinValue(0);
        billingIntervalPicker.setMaxValue(2); //{"kuukausittain", "vuosittain", "muu"}
        billingIntervalPicker.setDisplayedValues(billingIntervalPickerVals);
        billingIntervalCustom1.setMinValue(1);
        billingIntervalCustom1.setMaxValue(365);
        billingIntervalCustom2.setMinValue(0);
        billingIntervalCustom2.setMaxValue(1);
        billingIntervalCustom2.setDisplayedValues(billingIntervalPickerCustomVals);
        //billingIntervalSelected {a, b} a = "kuinka usein", b = 0=päivä, 1=kuukausi 2=vuosi

        billingIntervalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (newVal) {
                    case 0: //kuukausittain
                        billingIntervalSelected[0] = 1;
                        billingIntervalSelected[1] = 1;
                        billingIntervalCustomParent.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        billingIntervalSelected[0] = 1;
                        billingIntervalSelected[1] = 2;
                        billingIntervalCustomParent.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        billingIntervalSelected[1] = 0; // aseta periodi arvo takaisin päiväksi
                        billingIntervalCustomParent.setVisibility(View.VISIBLE);
                        break;
                    default:
                        return;


                }

            }
        });

        // 1 - 365
        billingIntervalCustom1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                billingIntervalSelected[0] = newVal;
            }
        });
        //{"päivän","kuukauden"}
        billingIntervalCustom2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (newVal) {
                    case 0:
                        billingIntervalSelected[1] = newVal;
                        break;
                    case 1:
                        billingIntervalSelected[1] = newVal;
                        break;
                }
            }
        });

        //FoCIntervaliPicker:::
        foCIntervalPicker.setMinValue(0);
        foCIntervalPicker.setMaxValue(3); //{"-","kuukausittain", "vuosittain", "muu"}
        foCIntervalPicker.setDisplayedValues(foCIntervalPickerVals);
        foCIntervalCustom1.setMinValue(1);
        foCIntervalCustom1.setMaxValue(365);
        foCIntervalCustom2.setMinValue(0);
        foCIntervalCustom2.setMaxValue(1);
        foCIntervalCustom2.setDisplayedValues(foCIntervalPickerCustomValsSingle);
        //billingIntervalSelected {a, b} a = "kuinka usein", b = 0=päivä, 1=kuukausi 2=vuosi

        foCIntervalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (newVal) {
                    case 0: //-
                        foCIntervalSelected[0] = 0;
                        foCIntervalSelected[1] = 3;
                        foCIntervalCustomParent.setVisibility(View.INVISIBLE);
                        break;
                    case 1: //kuukausittain
                        foCIntervalSelected[0] = 1;
                        foCIntervalSelected[1] = 1;
                        foCIntervalCustomParent.setVisibility(View.INVISIBLE);
                        break;
                    case 2: //vuosittain
                        foCIntervalSelected[0] = 1;
                        foCIntervalSelected[1] = 2;
                        foCIntervalCustomParent.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        foCIntervalSelected[1] = 0; // aseta periodi arvo takaisin päiväksi
                        foCIntervalCustomParent.setVisibility(View.VISIBLE);
                        break;
                    default:
                        return;


                }

            }
        });

        // 1 - 365
        foCIntervalCustom1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //aseta sanamuoto yksikköön tai monikkoon pickerissä olevan luvun perusteella
                if (newVal == 1) {
                    foCIntervalCustom2.setDisplayedValues(foCIntervalPickerCustomValsSingle);
                } else if (oldVal == 1) {
                    foCIntervalCustom2.setDisplayedValues(foCIntervalPickerCustomValsMulti);

                }
                foCIntervalSelected[0] = newVal;

            }
        });

        //{"päivän","kuukauden"}
        foCIntervalCustom2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (newVal) {
                    case 0:
                        foCIntervalSelected[1] = newVal;
                        break;
                    case 1:
                        foCIntervalSelected[1] = newVal;
                        break;
                }
            }
        });


        if (getArguments() != null) {
            toolbar.setTitle("Päivitä tilauksen tietoja");
            updateSubInit(drefString);
        }

        return v;
    }

    //hakee tilauksen tiedot tietokannasta ja päivittää näkymään.
    private void updateSubInit(String drefString) {

        Log.d("updateSub", "dref::" + drefString);
        dref = fs.collection("subs/"+sessionManager.getUid()+"/userSubs").document(drefString);



        dref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null) {
                        editTextTitle.setText(doc.getString("title"));
                        editTextPrice.setText(String.valueOf(doc.getDouble("price")));

                        try {
                            Date dueDate = doc.getDate("billingDueDate");
                            SimpleDateFormat formatDate = new SimpleDateFormat();


                            formatDate.applyPattern("dd");
                            String dayS = formatDate.format(dueDate);
                            int day = Integer.valueOf(dayS);
                            formatDate.applyPattern("MM");
                            String monthS = formatDate.format(dueDate);
                            int month = Integer.valueOf(monthS) - 1;
                            formatDate.applyPattern("yyyy");
                            String yearS = formatDate.format(dueDate);
                            int year = Integer.valueOf(yearS);

                            datePicker.updateDate(year, month, day);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // ASETA laskutusvälin arvot::
                        Double billingIntervalIntD = doc.getDouble("billingIntervalInt");
                        int billingIntervalInt = (int) Math.round(billingIntervalIntD);

                        String billingIntervalPeriod = doc.getString("billingIntervalPeriod");

                        Log.d("billingInterval", "Int::" + String.valueOf(billingIntervalInt));
                        Log.d("billingInterval", "Int::" + billingIntervalPeriod);

                        if (billingIntervalInt == 1) {
                            switch (billingIntervalPeriod) {
                                case "MONTH":
                                    billingIntervalSelected[0] = 1;
                                    billingIntervalSelected[1] = 1;
                                    billingIntervalPicker.setValue(0);
                                    Log.d("billingInterval", "Tuli ekaan: :Int::" + billingIntervalPeriod);
                                    break;
                                case "YEAR":
                                    billingIntervalSelected[0] = 1;
                                    billingIntervalSelected[1] = 2;
                                    billingIntervalPicker.setValue(1);
                                    Log.d("billingInterval", "Tuli tokaan: :Int::" + billingIntervalPeriod);
                                    break;
                                case "DAY":
                                    billingIntervalSelected[0] = 1;
                                    billingIntervalSelected[1] = 0;
                                    billingIntervalCustomParent.setVisibility(View.VISIBLE);
                                    billingIntervalPicker.setValue(2);
                                    Log.d("billingInterval", "Tuli tokaan: :Int::" + billingIntervalPeriod);
                                    break;
                            }
                        } else {
                            Log.d("billingInterval", "Tuli elseen: :Int::" + billingIntervalPeriod);
                            billingIntervalCustomParent.setVisibility(View.VISIBLE);
                            billingIntervalPicker.setValue(3);
                            billingIntervalSelected[0] = billingIntervalInt;

                            billingIntervalCustom1.setValue(billingIntervalInt);
                            if (billingIntervalPeriod == "DAY") {
                                billingIntervalSelected[1] = 0;
                                billingIntervalCustom2.setValue(0);
                            } else if (billingIntervalPeriod == "MONTH") {
                                billingIntervalSelected[1] = 1;
                                billingIntervalCustom2.setValue(1);
                                billingIntervalCustom2.setValue(1);
                            }

                        }

                        // ASETA ilmaisjakson arvot::
                        Double foCIntervalIntD;
                        try {
                            foCIntervalIntD = doc.getDouble("foCIntervalInt");
                        } catch (Exception e) {
                            foCIntervalIntD = 0.00;
                        }
                        int foCIntervalInt = (int) Math.round(foCIntervalIntD);
                        String foCIntervalPeriod = doc.getString("foCIntervalPeriod");

                        Log.d("focInterval", "int::" + String.valueOf(foCIntervalInt));
                        if (foCIntervalInt == 0) {
                            foCIntervalSelected[0] = 0;
                            foCIntervalSelected[1] = 3;
                            foCIntervalPicker.setValue(0);
                        } else if (foCIntervalInt == 1) {

                            Log.d("focInterval", "Period::" + foCIntervalPeriod );
                            switch (foCIntervalPeriod) {
                                case "MONTH":
                                    foCIntervalSelected[0] = 1;
                                    foCIntervalSelected[1] = 1;
                                    foCIntervalPicker.setValue(1);
                                    Log.d("foCIntervalPeriod", "Tuli ekaan: :Int::" + foCIntervalPeriod);
                                    break;
                                case "YEAR":
                                    foCIntervalSelected[0] = 1;
                                    foCIntervalSelected[1] = 2;
                                    foCIntervalPicker.setValue(2);
                                    Log.d("foCIntervalPeriod", "Tuli tokaan: :Int::" + foCIntervalPeriod);
                                    break;
                                case "DAY":
                                    foCIntervalSelected[0] = 1;
                                    foCIntervalSelected[1] = 0;
                                    foCIntervalCustomParent.setVisibility(View.VISIBLE);
                                    foCIntervalPicker.setValue(3);
                                    Log.d("billingInterval", "Tuli tokaan: :Int::" + billingIntervalPeriod);
                                    break;
                            }
                        } else {
                            Log.d("focinterval", "Tuli elseenFOC: :Int::" + foCIntervalPeriod);
                            foCIntervalCustomParent.setVisibility(View.VISIBLE);
                            foCIntervalPicker.setValue(3);
                            foCIntervalSelected[0] = foCIntervalInt;
                            foCIntervalCustom1.setValue(foCIntervalInt);

                            switch (foCIntervalPeriod) {
                                case "DAY":
                                    Log.d("focinterval", "tulo DAY::" + foCIntervalPeriod);
                                    foCIntervalSelected[1] = 0;
                                    foCIntervalCustom2.setValue(0);
                                    break;
                                case "MONTH":
                                    Log.d("focinterval", "Tuli MONTH: :Int::" + foCIntervalPeriod);
                                    foCIntervalSelected[1] = 1;
                                    foCIntervalCustom2.setValue(1);
                                    break;

                            }


                        }

                    }
                }
            }
        });


    }
}