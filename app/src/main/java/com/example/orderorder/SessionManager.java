package com.example.orderorder;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    String uid;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("Uid", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setLogin(boolean login) {
        editor.putBoolean("KEY_LOGIN", login);
        editor.commit();
    }

    public boolean getLogin() {
        return sharedPreferences.getBoolean("KEY_LOGIN",false);
    }

    public void setUid(String uid) {
        editor.putString("KEY_UID", uid);
        editor.commit();
    }

    public String getUid() {
        return sharedPreferences.getString("KEY_UID", "");
    }
}
