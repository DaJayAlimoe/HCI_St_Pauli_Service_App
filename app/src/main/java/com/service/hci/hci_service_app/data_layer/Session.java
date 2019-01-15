package com.service.hci.hci_service_app.data_layer;

import java.util.HashMap;


public class Session {
    private static Session session = null;
    private HashMap<String, Object> userSession;

    private Session() {
        userSession = new HashMap<>();
    }

    /**
     * set user session data
     * @param key
     * @param value
     */
    public static void setUserData(String key, Object value) {
        if (session == null) {
            session = new Session();
        }
        session.userSession.put(key, value);
    }

    /**
     * check if user exists
     * @return
     */
    private static boolean userExists() {
        return (session != null && session.userSession.containsKey("id"));
    }

    /**
     * get user session data
     * @param key
     * @return
     */
    public static Object user(String key) {
        if (Session.userExists()) {
            return session.userSession.get(key);
        }
        return Boolean.FALSE;
    }

    /**
     * get user Token
     * @return
     */
    public static String getToken() {
        if (Session.userExists()) {
            return session.userSession.get("token").toString();
        }
        return null;
    }

}

