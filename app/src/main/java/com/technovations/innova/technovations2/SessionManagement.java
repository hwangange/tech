package com.technovations.innova.technovations2;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.technovations.innova.technovations2.Login;

public class SessionManagement {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "isLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_YEAR = "year";
    public static final String KEY_HOURS = "hour";

    // Constructor
    public SessionManagement (Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String username, String name, String email, int year, int hours) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_YEAR, String.valueOf(year));
        editor.putString(KEY_HOURS, String.valueOf(hours));
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, "null"));
        user.put(KEY_NAME, pref.getString(KEY_NAME, "null"));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, "null"));
        user.put(KEY_YEAR, pref.getString(KEY_YEAR, "null"));
        user.put(KEY_HOURS, pref.getString(KEY_HOURS, "null"));
        return user;
    }

    public void updateHours(String hours) {
        editor.putString(KEY_HOURS, hours);
        editor.commit();
    }

    public void checkLogin() {
        if(!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }



    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}