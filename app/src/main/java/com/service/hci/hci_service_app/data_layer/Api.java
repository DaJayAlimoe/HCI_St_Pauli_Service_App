package com.service.hci.hci_service_app.data_layer;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStreamWriter;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.content.Context;

import org.json.*;

import javax.net.ssl.HttpsURLConnection;

public class Api {
    private String baseUrl;

    public Api(){
        baseUrl = "http://192.168.178.11:443/";
    }

    /**
     * authenticate qr code
     * @param qrToken
     */
    public void authenticate(String qrToken) {
        Request request = new Request();
        try {
            JSONObject result = (JSONObject) request.execute("/v1/Token", "GET", qrToken).get();
            if(result.has("token")) {
                SessionManager.setUserData("token", result.get("token").toString());
                if(result.has("data")) {
                    JSONObject data = result.getJSONObject("data");
                    if(data.has("employee")) {
                        SessionManager.setUserData("isEmployee", true);
                        SessionManager.setUserData("id", data.get("employee.id"));
                    }
                    if(data.has("seat")) {
                        SessionManager.setUserData("isEmployee", false);
                        SessionManager.setUserData("id", data.get("seat.id"));
                    }
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            System.out.println("Exception while trying to authenticate user: " + e);
        }
    }


    /**
     * place oder as a VIP customer
     * @param orderList
     * @return
     */
    public JSONObject placeOrder(Map<Integer, Integer> orderList) {
        JSONObject responseData = null;
        if(! (Boolean) SessionManager.user("isEmployee")) {
            int id = Integer.parseInt(SessionManager.user("id").toString());
            JSONObject data = new JSONObject();
            for (Map.Entry<Integer, Integer> entry : orderList.entrySet()) {
                JSONObject order = new JSONObject();
                try {
                    order.put("item_id", entry.getKey());
                    order.put("amount", entry.getValue());
                    order.put("seat_id", id);
                    data.put("0", order);
                } catch (JSONException e) {
                    System.out.println("JSONException placing order: " + e);
                }
            }
        }
        return responseData;
    }



    private class Request extends AsyncTask {
        /**
         *
         * @param param [String : Endpoint, String : RequestMethod, JSONObject : Body]
         * @return
         */
        @Override
        protected JSONObject doInBackground(Object[] param) {
            String endpoint = param[0].toString();
            String method = param[1].toString();
            JSONObject responseData = null;
            try {
                JSONObject data = (JSONObject) param[2];

                // Open new Connection to server and send data
                Connection connection = new Connection(baseUrl+endpoint, method);
                connection.send(data.toString());

                // if there is a response code AND that response code is 200 OK
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String received = connection.receive();
                    JSONObject response = new JSONObject(received);
                    if(response.get("status").equals("OK")) {
                        Object jwt = response.get("token");
                        responseData = new JSONObject(response.get("data").toString());
                    }
                } else {
                    // Server returned HTTP error code.
                    System.out.println("HTTP RESPONSE CODE: " + responseCode);
                }

                // Close the connection to the server
                connection.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return responseData;
        }

//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//        }

        /**
         * Connection class
         */
        private class Connection {
            private HttpsURLConnection conn;
            private BufferedReader reader;
            private OutputStreamWriter writer;

            /**
             * Open new connection
             * @param url
             * @param method
             * @throws IOException
             */
            public Connection(String url, String method) throws IOException {
                URL obj = new URL(url);
                conn = (HttpsURLConnection) obj.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod(method);
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                writer = new OutputStreamWriter(conn.getOutputStream());
            }

            /**
             * get the response code
             * @return
             * @throws IOException
             */
            public int getResponseCode() throws IOException {
                return conn.getResponseCode();
            }

            /**
             * receive response through connection
             * @return
             * @throws IOException
             */
            public String receive() throws IOException {
                String response = "";
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                return response;
            }

            /**
             * send data through connection
             * @param data
             * @throws IOException
             */
            public void send(String data) throws IOException {
                writer.write(data);
                writer.flush();

            }

            /**
             * close connection
             * @throws IOException
             */
            public void close () throws IOException{
                writer.close();
                reader.close();
                conn.disconnect();
            }

        }

    }
}