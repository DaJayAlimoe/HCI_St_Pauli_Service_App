package com.service.hci.hci_service_app.data_layer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


// this class holds the session over the match-day. After that time, the properties are reset!
public class Session {
    private static Session session = null;

    // Sharedpref file name
    private static final String PREF_NAME = "sharedSession";

    private static final String IS_EMPLOYEE = "isEmployee";

    private static final String IS_SEAT = "isSeat";

    private static final String TOKEN = "token";

    private static final String ID = "id";

    // initialize helpful variable
    private static SharedPreferences.Editor editor;

    private static SharedPreferences preferences;


    private Session(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, 0); // 0 - for private mode
        this.editor = preferences.edit();
    }

    /**
     * set user session data
     * @param isEmpl
     * @param id
     * @param qrToken
     */
    public static void setUserData(Context context, boolean isEmpl, int id, String qrToken) {
        if (session == null) {
            session = new Session(context);
        }
        if(isEmpl) {
            editor.putBoolean(IS_EMPLOYEE, true);
            editor.putBoolean(IS_SEAT, false);

        }else{
            editor.putBoolean(IS_EMPLOYEE, false);
            editor.putBoolean(IS_SEAT, true);
        }
        editor.putString(TOKEN, qrToken);
        editor.putInt(ID, id);

        editor.commit();
    }

    /**
     * check if user exists
     * @return boolean
     */
    public static Boolean isSeat() {
        return preferences.getBoolean(IS_SEAT,false);
    }

    /**
     * get user session data
     * @return boolean
     */
    public static Boolean isEmployee() {
            return preferences.getBoolean(IS_EMPLOYEE,false);
    }

    /**
     * get user Token
     * @return
     */
    public static String getToken() {
        return preferences.getString(TOKEN,null);
    }

    /**
     * get user Token
     * @return
     */
    public static int getUserId() {
        return preferences.getInt(ID,-1);
    }

}

