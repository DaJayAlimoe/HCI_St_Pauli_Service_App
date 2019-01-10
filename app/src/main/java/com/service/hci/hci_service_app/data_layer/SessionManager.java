package com.service.hci.hci_service_app.data_layer;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SessionManager {
    private static SessionManager session = null;
    private HashMap<String, Object> userSession;

    private SessionManager() {
        userSession = new HashMap<>();
    }

    /**
     * set user session data
     * @param key
     * @param value
     */
    public static void setUserData(String key, Object value) {
        if (session == null) {
            session = new SessionManager();
        }
        session.userSession.put(key, value);
    }

    /**
     * get user session data
     * @param key
     * @return
     */
    public static Object user(String key) {
        if (session == null) {
            return null;
        }
        return session.userSession.get(key);
    }

}

