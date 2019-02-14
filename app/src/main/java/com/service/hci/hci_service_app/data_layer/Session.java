package com.service.hci.hci_service_app.data_layer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;


// this class holds the session over the match-day. After that time, the properties are reset!
public class Session {
    //private  Session session = null;

    // Sharedpref file name
    private final String PREF_NAME = "sharedSession";

    private final String IS_EMPLOYEE = "isEmployee";

    private final String IS_SEAT = "isSeat";

    private final String TOKEN = "token";

    private final String ID = "id";
    private final SharedPreferences.Editor editor;
    private final SharedPreferences preferences;
    private static Session session = null;


    public static Session getInstance(Context context) {
        if (session == null)
            session = new Session(context);

        return session;
    }

    private Session(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = preferences.edit();
//        this.context = context;
    }

    /**
     * set user session data
     * @param isEmpl
     * @param id
     * @param qrToken
     */
    public void setUserData(boolean isEmpl, int id, String qrToken) {
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
    public Boolean isSeat() {
        return preferences.getBoolean(IS_SEAT,false);
    }

    /**
     * check if user is a customer
     * @return boolean
     */
    public Boolean isEmployee() {
            return preferences.getBoolean(IS_EMPLOYEE,false);
    }

    /**
     * get user Token
     * @return
     */
    public String getToken() {
        return preferences.getString(TOKEN,null);
    }

    /**
     * get user id
     * @return
     */
    public int getUserId() {
        return preferences.getInt(ID,-1);
    }

    /**
     * get menu items
     * @return
     */
    public String getItems() { return preferences.getString("items", "");}

    /**
     * set menu items
     * @param items
     */
    public void setItems(String items) { editor.putString("items", items);}

    // to clear all session data
    public void remove(){
        editor.clear();
        editor.commit();
    }

}

