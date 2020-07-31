package com.example.orderorder.ui.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.orderorder.data.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.callback.Callback;

import static android.content.ContentValues.TAG;

public class PlanCalculation {

    Double totalSum = 0.00;
    String totalSumString = "";
    String uid;

    private CollectionReference cref;
    private FirebaseFirestore fs = FirebaseFirestore.getInstance();

    PlanCalculation(String uid) {
        this.uid = uid;
        this.cref = fs.collection("subs/"+uid+"/userSubs");
        //calculateYearTotal();
    }

    public void calculateYearTotal(final getTotalSumYear callback) {
        Log.d("", "");
        //final double totalSum = 0;

        cref
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("calculateYear", document.getId() + " => " + document.getData());

                                totalSum = totalSum + sumForSub(document);

                                Log.d("calculateYear",   " SUM: " + String.valueOf(totalSum) );
                            }
                        } else {
                            Log.d("calculateYear", "Error getting documents: ", task.getException());
                        }
                        totalSumString = String.valueOf(totalSum);

                        callback.onComplete(totalSumString);

                    }
                });



    }


    private double sumForSub(DocumentSnapshot doc) {
        int monthsLeft= 0;
        int daysLeft = 0;
        double amount = 0.00;
        int year = 0;
        String billingIntervalPeriod = doc.getString("billingIntervalPeriod");
        String eoy = "31/12/2020";
        Date dueDate = doc.getDate("billingDueDate");
        SimpleDateFormat formatDate = new SimpleDateFormat();
        Date billingDueDateDate = new Date();


        formatDate.applyPattern("dd/MM/yyyy");
        String dueDateString = formatDate.format(dueDate);
        try {
            billingDueDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(dueDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        double price = doc.getDouble("price");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        switch (billingIntervalPeriod) {
            case "DAY":
                try {
                    daysLeft = daysBetween(sdf.parse(eoy), billingDueDateDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                amount = monthsLeft * price;
                break;

            case "MONTH":
                try {
                    monthsLeft = monthsBetween(billingDueDateDate, sdf.parse(eoy));
                } catch (ParseException e) {
                    e.printStackTrace();

                }
                amount = monthsLeft * price;
                break;
            case "YEAR":
                sdf = new SimpleDateFormat("yyyy");
                if( String.valueOf(billingDueDateDate) == "2020" ) {
                    amount = price*1;
                }
                break;

        }



        return amount;


    }

    static int monthsBetween(Date a, Date b) {
        Calendar cal = Calendar.getInstance();
        if (a.before(b)) {
            cal.setTime(a);
        } else {
            cal.setTime(b);
            b = a;
        }
        int c = 0;
        while (cal.getTime().before(b)) {
            cal.add(Calendar.MONTH, 1);
            c++;
        }
        return c - 1;
    }

    static int daysBetween(Date a, Date b) {
        Calendar cal = Calendar.getInstance();
        if (a.before(b)) {
            cal.setTime(a);
        } else {
            cal.setTime(b);
            b = a;
        }
        int c = 0;
        while (cal.getTime().before(b)) {
            cal.add(Calendar.DATE, 1);
            c++;
        }
        return c - 1;
    }

    public void getTotalSumString(getTotalSumYear callback) {
        callback.onComplete(totalSumString);
    }
}



interface getTotalSumYear  {
    void onComplete(String sum);
}