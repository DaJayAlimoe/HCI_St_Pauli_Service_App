package com.service.hci.hci_service_app.data_layer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


// this class holds the session over the match-day. After that time, the properties are reset!
public class Session {
    //private static Session session = null;

    // Sharedpref file name
    private static final String PREF_NAME = "sharedSession";

    private static final String IS_EMPLOYEE = "isEmployee";

    private static final String IS_SEAT = "isSeat";

    private static final String TOKEN = "token";

    private static final String ID = "id";

    // Shared pref mode
    private int PRIVATE_MODE = 0;



    private static Context context;

    // initialize helpful variable
    private static SharedPreferences.Editor editor;

    private static SharedPreferences preferences;


    public Session(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE); // 0 - for private mode
        this.editor = preferences.edit();
        this.context = context;
    }

    /**
     * set user session data
     * @param isEmpl
     * @param id
     * @param qrToken
     */
    public static void setUserData(boolean isEmpl, int id, String qrToken) {
//        if (session == null) {
//            session = new Session(context);
//        }
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
     * check if user is a customer
     * @return boolean
     */
    public static Boolean isSeat() {
        return preferences.getBoolean(IS_SEAT,false);
    }

    /**
     * check if user is a customer
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
     * get user id
     * @return
     */
    public static int getUserId() {
        return preferences.getInt(ID,-1);
    }

    /**
     * get menu items
     * @return
     */
    public static String getItems() { return preferences.getString("items", "");}

    /**
     * set menu items
     * @param items
     */
    public static void setItems(String items) { editor.putString("items", items);}

    // to clear all session data
    public static void remove(){
        editor.clear();
        editor.commit();
    }

}

