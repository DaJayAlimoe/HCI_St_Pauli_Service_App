package com.service.hci.hci_service_app.data_layer;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.HandlerThread;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStreamWriter;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import android.content.Context;

import org.json.*;

public class ClientAPI{

    private String baseUrl = "http://192.168.178.11:443/";//"http://10.0.2.2:443/";

    public ClientAPI(){
    }

    /**
     * GET response from the Rest API as Boolean.
     * @return Boolean
     */
        public Boolean sendRegist (String name, String vorname, String email, String password, String studiengang) {
            boolean result = false;
            try {
                RegistAsync regist= new RegistAsync();
                result = (boolean) regist.execute(name, vorname, email, password, studiengang).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    private class RegistAsync extends AsyncTask {
//        public Boolean sendRegist (String name, String vorname, String email, String password, String studiengang) throws Exception{
//
//        }
        @Override
        protected Boolean doInBackground(Object[] param) {
            String result;
            URL obj = null;
            try {
                obj = new URL(baseUrl + "user/register");

                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("POST");

                JSONObject regist = new JSONObject();
                regist.put("name", param[0]);
                regist.put("vorname", param[1]);
                regist.put("email", param[2]);
                regist.put("password", param[3]);
                regist.put("studiengang", param[4]);

                writeJsonObject(connection, regist);

                // if there is a response code AND that response code is 200 OK
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result = getResponse(connection);
                    //System.out.println("HTTP RESPONSE CODE: " + responseCode);
                    if (result.equals("true")) { // must be "true" if correct answer
                        disconnect(connection);
                        return true;
                    } else {
                        disconnect(connection);
                        return false;
                    }
                } else {
                    // Server returned HTTP error code.
                    //System.out.println("HTTP RESPONSE CODE: " + responseCode);
                    disconnect(connection);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

        /**
         * GET response from the Rest API as JSONObject(token).
         *
         * @return Boolean
         */
        public Boolean sendLogin(String email, String password,Context context) throws Exception {
            boolean result = false;
            try {
                LoginAsync login = new LoginAsync();
                result = (boolean) login.execute(email, password, context).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    private class LoginAsync extends AsyncTask {
        @Override
        protected Boolean doInBackground(Object[] param) {
            String result;
            try{
            URL obj = new URL(baseUrl + "user/login");
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setDoInput(true);
            connection.setRequestProperty("password",param[1].toString());
            connection.setRequestProperty("email",param[0].toString());
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("GET");

            // if there is a response code AND that response code is 200 OK
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                result = getResponse(connection);
                JSONObject reader = new JSONObject(result);
                String jid = reader.getString("id");
                String jtoken = reader.getString("token");
                String jemail = reader.getString("email");
                Context context = (Context) param[2]; // create a Session with Context EntryHandler
                SessionManager session = new SessionManager(context);
                session.createLoginSession(jid,jemail,jtoken);
                disconnect(connection);
                return true;
            } else {
                // Server returned HTTP error code.
                disconnect(connection);
                return false;
            }
        }catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        }
        /**
         * GET response from the Rest API as BOOLEAN.
         *
         * @return Boolean
         */
        public Boolean checkToken() throws Exception {
            boolean result = false;
            String token = getToken();
            try {
                CheckTokenAsync check = new CheckTokenAsync();
                result = (boolean) check.execute(token).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    private class CheckTokenAsync extends AsyncTask {
        @Override
        protected Boolean doInBackground(Object[] param) {
            try {
                URL obj = new URL(baseUrl + "user/tokenExist");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setDoInput(true);
                connection.setRequestProperty("token", param[0].toString());
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String result = getResponse(connection);
                    if (result.equals("true")) {
                        disconnect(connection);
                        return true;
                    } else {
                        disconnect(connection);
                        return false;
                    }
                } else {
                    disconnect(connection);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * POST response from the Rest API as JSONArray.
     *
     * @return HashMap
     */
    public HashMap getAvailableModules() throws Exception {
        HashMap result = null;
        String token = getToken();
        try {
            GetAvailableModulesAsync getModules = new GetAvailableModulesAsync();
            result = (HashMap) getModules.execute(token).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class GetAvailableModulesAsync extends AsyncTask {

        @Override
        protected HashMap doInBackground(Object[] param) {
            String result;
            HashMap<Integer, String> moduleMap = null;
            try {
                URL obj = new URL(baseUrl + "module/availableModule");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("token", param[0].toString());
                connection.setRequestMethod("GET");

                // if there is a response code AND that response code is 200 OK
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    moduleMap = new HashMap<Integer, String>();
                    result = getResponse(connection);

                    JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); i++) {
                        JSONObject module = reader.getJSONObject(i);
                        int jid = module.getInt("id");
                        String jname = module.getString("name");
                        moduleMap.put(jid,jname);
                    }
                    for (Map.Entry ob : moduleMap.entrySet()){
                        Log.i("Key",ob.getKey().toString());
                        Log.i("Value",ob.getValue().toString());
                    }

                    disconnect(connection);
                } else {
                    disconnect(connection);
                    return moduleMap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return moduleMap;
        }
    }

    /**
     * POST response from the Rest API as JSONArray.
     *
     * @return ArryList within Profil
     */
    public ArrayList<Profil> searchForAdvert(Integer id, String name) throws Exception {
        ArrayList result = null;
        String token = getToken();
        try {
            SearchForAdvertAsync searchAdvert = new SearchForAdvertAsync();
            result = (ArrayList) searchAdvert.execute(token,id,name).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class SearchForAdvertAsync extends AsyncTask {
        //
        @Override
        protected ArrayList doInBackground(Object[] param) {
            ArrayList<Profil> listProfil = new ArrayList<>();
            try {
                URL obj = new URL(baseUrl + "module/findPartner");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("token", param[0].toString());
                connection.setRequestMethod("POST");

                JSONObject search = new JSONObject();
                search.put("id", param[1]);
                search.put("name", param[2]);
                writeJsonObject(connection, search);

                // if there is a response code AND that response code is 200 OK
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String result = getResponse(connection);

                    JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); i++) {
                        JSONObject profil = reader.getJSONObject(i);
                        String jemail = profil.getString("email");
                        String jvorname = profil.getString("vorname");
                        String jnachname = profil.getString("name");
                        String jstudiengang = profil.getString("studiengang");
                        String jsonstiges = profil.getString("sonstiges");
                        listProfil.add(new Profil(jvorname,jnachname,jemail,jstudiengang,jsonstiges));
                    }
                    disconnect(connection);
                    return listProfil;
                } else {
                    disconnect(connection);
                    return listProfil;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return listProfil;
        }
    }

        /**
         * POST response from the Rest API as BOOLEAN.
         *
         * @return Boolean
         */
        public Boolean createAdvert(HashMap<Integer,String> veranstaltungen) throws Exception {
            boolean result = false;
            String token = getToken();
            try {
                CreateAdvertAsync newAdvert = new CreateAdvertAsync();
                result = (boolean) newAdvert.execute(token, veranstaltungen).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        private class CreateAdvertAsync extends AsyncTask {

            @Override
            protected Boolean doInBackground(Object[] param) {
                String result;
                try {
                    URL obj = new URL(baseUrl + "module/attendModul");
                    HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("token", param[0].toString());
                    connection.setRequestMethod("POST");

                    JSONArray advert = new JSONArray();
                    HashMap<Integer,String> map = (HashMap<Integer,String>) param[1];
                    for(Map.Entry entry : map.entrySet()){
                        JSONObject json = new JSONObject();
                        json.put("id",entry.getKey());
                        json.put("name",entry.getValue());
                        advert.put(json);
                    }
                   writeJsonArray(connection,advert);
                    // if there is a response code AND that response code is 200 OK
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        result = getResponse(connection);
                        if (result.equals("true")) { // must be "true" if correct answer
                            disconnect(connection);
                            return true;
                        } else {
                            disconnect(connection);
                            return false;
                        }
                    } else {
                        // Server returned HTTP error code.
                        disconnect(connection);
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

    /**
     * GET response from the Rest API as BOOLEAN.
     *
     * @return Boolean
     */
    public Boolean editProfil(String vorname, String name, String studiengang, String sonstiges) throws Exception {
        boolean result = false;
        String token = getToken();
        try {
            EditProfilAsync login = new EditProfilAsync();
            result = (boolean) login.execute(token,vorname,name,studiengang,sonstiges).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class EditProfilAsync extends AsyncTask {
        @Override
        protected Boolean doInBackground(Object[] param) {
            try {
                URL obj = new URL(baseUrl + "user/editProfil");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("token", param[0].toString());
                connection.setRequestMethod("PUT");

                JSONObject edit = new JSONObject();
                edit.put("vorname", param[1]);
                edit.put("name", param[2]);
                edit.put("studiengang", param[3]);
                edit.put("sonstiges", param[4]);
                writeJsonObject(connection, edit);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String result = getResponse(connection);
                    if (result.equals("true")) {
                        disconnect(connection);
                        return true;
                    } else {
                        disconnect(connection);
                        return false;
                    }
                } else {
                    disconnect(connection);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * GET response from the Rest API as BOOLEAN.
     *
     * @return Boolean
     */
    public Boolean changePassword(String email, String password) throws Exception {
        boolean result = false;
        String token = getToken();
        try {
            ChangePasswordAsync login = new ChangePasswordAsync();
            result = (boolean) login.execute(token,email,password).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class ChangePasswordAsync extends AsyncTask {
        @Override
        protected Boolean doInBackground(Object[] param) {
            try {
                URL obj = new URL(baseUrl + "user/changePassword");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("token", param[0].toString());
                connection.setRequestMethod("PUT");

                JSONObject edit = new JSONObject();
                edit.put("email", param[1]);
                edit.put("password", param[2]);
                writeJsonObject(connection, edit);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String result = getResponse(connection);
                    if (result.equals("true")) {
                        disconnect(connection);
                        return true;
                    } else {
                        disconnect(connection);
                        return false;
                    }
                } else {
                    disconnect(connection);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * GET response from the Rest API as BOOLEAN.
     *
     * @return Boolean
     */
    public Boolean sendMessages(String from, String to, String freitext ) throws Exception {
        boolean result = false;
        String token = getToken();
        try {
            SendMessagesAsync message = new SendMessagesAsync();
            result = (boolean) message.execute(token,from,to, freitext).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class SendMessagesAsync extends AsyncTask {
        @Override
        protected Boolean doInBackground(Object[] param) {
            try {
                URL obj = new URL(baseUrl + "message/sendMessage");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("token", param[0].toString());
                connection.setRequestMethod("POST");

                JSONObject json = new JSONObject();
                json.put("sender", param[1]);
                json.put("to", param[2]);
                json.put("content", param[3]);
                writeJsonObject(connection, json);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String result = getResponse(connection);
                    if (result.equals("true")) {
                        disconnect(connection);
                        return true;
                    } else {
                        disconnect(connection);
                        return false;
                    }
                } else {
                    disconnect(connection);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * GET response from the Rest API as JSONArray.
     *
     * @return MyMessage in ArrayList
     */
    public ArrayList getMessages() throws Exception {
        ArrayList result = null;
        String token = getToken();
        try {
            GetMessagesAsync message = new GetMessagesAsync();
            result = (ArrayList) message.execute(token).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class GetMessagesAsync extends AsyncTask {
        @Override
        protected ArrayList<MyMessage> doInBackground(Object[] param) {
            ArrayList<MyMessage> myMessages = null;
            try {
                myMessages = new ArrayList<>();
                URL obj = new URL(baseUrl + "message/getMessage");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setRequestProperty("token", param[0].toString());
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String result = getResponse(connection);

                    JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); i++) {
                        JSONObject message = reader.getJSONObject(i);
                        String jfrom = message.getString("sender");
                        String jto = message.getString("to");
                        String jfreitext = message.getString("content");
                        myMessages.add(new MyMessage(jfrom, jto, jfreitext));
                    }
                        disconnect(connection);
                        return myMessages;
                } else {
                    disconnect(connection);
                    return myMessages;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return myMessages;
        }
    }

    /**
     * GET response from the Rest API as BOOLEAN.
     *
     * @return Boolean
     */
    public Boolean logoutUser() throws Exception {
        boolean result = false;
        String token = getToken();
        try {
            LogoutAsync login = new LogoutAsync();
            result = (boolean) login.execute(token).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class LogoutAsync extends AsyncTask {
        @Override
        protected Boolean doInBackground(Object[] param) {
            String result;
            try{
                URL obj = new URL(baseUrl + "user/logout");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setDoInput(true);
                connection.setRequestProperty("token",param[0].toString());
                connection.setRequestMethod("GET");

                // if there is a response code AND that response code is 200 OK
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    result = getResponse(connection);
                    if(result.equals("true")){
                        disconnect(connection);
                        return true;
                    }
                } else {
                    disconnect(connection);
                    return false;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }
    /**
     * GET response from the Rest API as JSONObject.
     *
     * @return  Your Profil
     */
    public Profil getProfil() throws Exception {
        Profil result = null;
        String token = getToken();
        try {
            GetProfilAsync profil = new GetProfilAsync();
            result = (Profil) profil.execute(token).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class GetProfilAsync extends AsyncTask {
        //
        @Override
        protected Profil doInBackground(Object[] param) {
            Profil profilObj = new Profil();
            try {
                URL obj = new URL(baseUrl + "user/getProfile");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setRequestProperty("token", param[0].toString());
                connection.setRequestMethod("GET");

                // if there is a response code AND that response code is 200 OK
                int responseCode = connection.getResponseCode();
                Log.i("ProfilresP: ", String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String result = getResponse(connection);
                    JSONObject profil = new JSONObject(result);
                    Integer jid = profil.getInt("id");
                    String jpassword = profil.getString("password");
                    String jemail = profil.getString("email");
                    String jvorname = profil.getString("vorname");
                    String jnachname = profil.getString("name");
                    String jstudiengang = profil.getString("studiengang");
                    String jsonstiges = profil.getString("sonstiges");
                    profilObj = new Profil(jid,jpassword,jvorname,jnachname,jemail,jstudiengang,jsonstiges);
                    Log.i("Profil: ", profilObj.getId().toString());
                    Log.i("Profil: ", profilObj.getEmail().toString());
                    disconnect(connection);
                    return profilObj;
                } else {
                    disconnect(connection);
                    return profilObj;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return profilObj;
        }
    }

    /**
     * GET response from the Rest API as JSONArray.
     *
     * @return  HashMap with your Modules
     */
    public HashMap<Integer, String> getMyModules() throws Exception {
        HashMap<Integer, String> result = null;
        String token = getToken();
        try {
            GetMyModulesAsync myModules = new GetMyModulesAsync();
            result = (HashMap<Integer, String>) myModules.execute(token).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class GetMyModulesAsync extends AsyncTask {
        //
        @Override
        protected HashMap<Integer, String> doInBackground(Object[] param) {
            HashMap<Integer, String> map = new HashMap<Integer, String>();
            try {
                URL obj = new URL(baseUrl + "module/getModules");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setRequestProperty("token", param[0].toString());
                connection.setRequestMethod("GET");

                // if there is a response code AND that response code is 200 OK
                int responseCode = connection.getResponseCode();
                Log.i("ModulesP: ", String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    map = new HashMap<Integer, String>();
                   String result = getResponse(connection);

                    JSONArray reader = new JSONArray(result);
                    for (int i = 0; i < reader.length(); i++) {
                        JSONObject module = reader.getJSONObject(i);
                        int jid = module.getInt("id");
                        String jname = module.getString("name");
                        map.put(jid,jname);
                    }
                    for (Map.Entry ob : map.entrySet()){
                        Log.i("Key",ob.getKey().toString());
                        Log.i("Value",ob.getValue().toString());
                    }
                } else {
                    disconnect(connection);
                    return map;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }
    }


    /**
     * GET response from the Rest API as Boolean.
     *
     * @return  Boolean
     */
    public Boolean checkMailNotExist(String email) throws Exception {
        Boolean result = false;
        try {
            CheckEmailAsync check = new CheckEmailAsync();
            result = (boolean) check.execute(email).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class CheckEmailAsync extends AsyncTask {
        //
        @Override
        protected Boolean doInBackground(Object[] param) {
            try {
                URL obj = new URL(baseUrl + "user/checkMail");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setRequestProperty("email", param[0].toString());
                connection.setRequestMethod("GET");

                // if there is a response code AND that response code is 200 OK
                int responseCode = connection.getResponseCode();
                Log.i("CheckMail: ", String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String result = getResponse(connection);
                    result = getResponse(connection);
                    //System.out.println("HTTP RESPONSE CODE: " + responseCode);
                    if (result.equals("true")) { // must be "true" if correct answer
                        disconnect(connection);
                        return true;
                    } else {
                        disconnect(connection);
                        return false;
                    }
                } else {
                    disconnect(connection);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }






    /**
     * GET response from the Rest API as Boolean.
     *
     * @return  Boolean
     */
    public Boolean deleteAdvert(HashMap<Integer,String> module) throws Exception {
        Boolean result = false;
        try {
            String token = getToken();
            DeleteAdvertAsync deleteAdv = new DeleteAdvertAsync();
            result = (boolean) deleteAdv.execute(token,module).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private class DeleteAdvertAsync extends AsyncTask {
        //
        @Override
        protected Boolean doInBackground(Object[] param) {
            try {
                URL obj = new URL(baseUrl + "module/deleteModule");
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("token", param[0].toString());
                connection.setRequestMethod("DELETE");

                JSONArray advert = new JSONArray();
                HashMap<Integer,String> map = (HashMap<Integer,String>) param[1];
                for(Map.Entry entry : map.entrySet()){
                    JSONObject json = new JSONObject();
                    json.put("id",entry.getKey());
                    json.put("name",entry.getValue());
                    advert.put(json);
                }
                writeJsonArray(connection,advert);

                // if there is a response code AND that response code is 200 OK
                int responseCode = connection.getResponseCode();
                Log.i("deleteAdv: ", String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String result = getResponse(connection);
                    result = getResponse(connection);
                    //System.out.println("HTTP RESPONSE CODE: " + responseCode);
                    if (result.equals("true")) { // must be "true" if correct answer
                        disconnect(connection);
                        return true;
                    } else {
                        disconnect(connection);
                        return false;
                    }
                } else {
                    disconnect(connection);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }


        // ---------------------Helper Functions--------------------------------//
        // close Connection
        private void disconnect(HttpURLConnection connection) {
            connection.disconnect();
        }

        // return all response from Server as String
        private String getResponse(HttpURLConnection connection) throws Exception {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // return response body
            return response.toString();
        }
        // get the token from the currently logged in user.
        private static String getToken(){
            HashMap<String,String> userMap = SessionManager.getUserDetails();
            String token = userMap.get("token");
            return token;
        }
        // write Stream as JSONObject
        private static void writeJsonObject(HttpURLConnection connection, JSONObject output) throws IOException {
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(output.toString());
            out.flush();
            out.close();
        }
    // write Stream as JSONArray
         private static void writeJsonArray(HttpURLConnection connection, JSONArray output) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write(output.toString());
        out.flush();
        out.close();
        }
}