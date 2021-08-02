package com.zaryab.omovie;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean isPrivacyAccepted() {
        return sharedPreferences.getBoolean("isPrivacyAccepted", false);
    }
    public void setIsPrivacyAccepted(boolean value) {
        editor.putBoolean("isPrivacyAccepted", value).commit();
    }



    ////////////////////////////////////////////////////////

    String getUserName() {
        return sharedPreferences.getString("username", "admin");
    }
    public void updateUserName(String value) {
        editor.putString("username", value).commit();
    }



    ////////////////////////////////////////////////////////

    String getPassword() {
        return sharedPreferences.getString("password", "123");
    }
    public void updatePassword(String value) {
        editor.putString("password", value).commit();
    }


}
