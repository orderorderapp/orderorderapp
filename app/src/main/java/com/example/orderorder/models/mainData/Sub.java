package com.example.orderorder.models.mainData;

import android.annotation.SuppressLint;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.orderorder.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "sub_table")
public class Sub {

    @PrimaryKey
    private String title;
    private double price;
    private String currency = "€";
    private String priceString;
    private Date billingDateStart;
    private Date billingDueDate;
    private String billingDueDateString;
    private String[] BillingIntervalType = new String[]  {
        "DAY", "MONTH", "YEAR", "na"
    };
    private int billingIntervalInt;
    private String billingIntervalPeriod; //0 = day, 1 = month, 2 = year
    private int foCIntervalInt;
    private String foCIntervalPeriod;
    private Date foCDateStart;
    private Date foCDateEnd;
    private String category;

    public Sub() {
        //empty constructor needed for Firebase
    }


    @SuppressLint("DefaultLocale")
    public Sub(String title, double price, String currency, Date billingDateStart, Date billingDueDate, int billingIntervalInt, int foCIntervalInt, Date foCDateStart, String category) {
        this.title = title;
        this.price = price;
        this.currency = currency;
        this.priceString = convertFloatToString(price);
        this.billingDateStart = billingDateStart;
        this.billingDueDate = billingDueDate;
        this.billingIntervalInt = billingIntervalInt;
        this.billingIntervalPeriod = billingIntervalPeriod;
        this.foCIntervalInt = foCIntervalInt;
        this.foCIntervalPeriod = foCIntervalPeriod;
        this.foCDateStart = foCDateStart;
        this.category = category;
        this.billingDueDateString = convertDataToString(billingDueDate);
    }

    public Sub(String title, double price, Date billingDueDate, int[] billingInterval, int[] foCInterval) {
        this.title = title;
        this.price = price;
        this.currency = currency;
        this.priceString = convertFloatToString(price);
        this.billingDateStart = billingDueDate;
        this.billingDueDate = billingDueDate;
        this.billingIntervalInt = billingInterval[0];
        this.billingIntervalPeriod = BillingIntervalType[billingInterval[1]];
        this.foCIntervalInt = foCInterval[0];
        this.foCIntervalPeriod = BillingIntervalType[foCInterval[1]];
        this.foCDateStart = foCDateStart;
        this.category = category;
        this.billingDueDateString = convertDataToString(billingDueDate);
    }

    public String convertDataToString(Date date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return dateFormat.format(date);
        } catch (Exception e) {
            return "pvm ?";
        }
        //return "pvm ?";
    }

    public String convertFloatToString(double d) {


        try {
            return String.valueOf(d);
        } catch (Exception e) {
            return "na";
        }
        //return "19.99";
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPriceString() {
        return priceString;
    }

    public Date getBillingDateStart() {
        return billingDateStart;
    }

    public Date getBillingDueDate() {
        return billingDueDate;
    }

    public String getBillingDueDateString() {
        return billingDueDateString;
    }

    public int getBillingIntervalInt() {
        return billingIntervalInt;
    }

    public int getFoCIntervalInt() {
        return foCIntervalInt;
    }

    public String getFoCIntervalPeriod() {
        return foCIntervalPeriod;
    }

    public Date getFoCDateStart() {
        return foCDateStart;
    }

    public Date getFoCDateEnd() {
        return foCDateEnd;
    }

    public String getCategory() { return category;  }

    public String getBillingIntervalPeriod() {
        return billingIntervalPeriod;
    }
}
