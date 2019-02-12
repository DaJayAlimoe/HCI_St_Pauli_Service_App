package com.service.hci.hci_service_app.data_layer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutionException;
import org.json.*;

public class Api {
    private String baseUrl;

    // to test on Emulator "http://10.0.2.2:443/";
    // local ip
    public Api(){
        baseUrl = "http://141.22.246.109:443";
//        baseUrl = "http://192.168.178.11:443";
    }

    /**
     * save response token and return response data
     * @param resp
     * @return
     */
    private JSONObject getResponseData(Object resp) {
        JSONObject response = (JSONObject)resp;
        return (response != null && response.length() > 0)? response : null;
    }

    /**
     * authenticate qr code
     * @param qrToken
     */
    public boolean authenticate(Context context, String qrToken) {
        boolean identified = false;
        Request request = new Request();
        try {
            Object result = request.execute("/v1/User", "GET", new JSONObject(), qrToken).get();
            JSONObject data = this.getResponseData(result);
            if(data != null) {
                if(!data.isNull("employee")) {
                    Session.setUserData(true,data.getJSONObject("employee").getInt("id"),qrToken);
                    identified = true;
                }else if(!data.isNull("seat")) {
                    Session.setUserData(false,data.getJSONObject("seat").getInt("id"),qrToken);
                    identified = true;
                } else {
                    identified = false;
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            System.out.println("Exception while authenticating user: " + e);
        }
        return identified;
    }

    /**
     * get Items
     * @return
     */
    public JSONObject getItems() {
        JSONObject data = null;
        try {
            Request request = new Request();
            Object result = request.execute("/v1/Item", "GET", new JSONObject(), Session.getToken()).get();
            data = this.getResponseData(result);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Exception while getting Items: " + e);
        }
        return data;
    }

    /**
     * get Orders
     * @return
     */
    public JSONObject getOrders() {
        JSONObject data = null;
        try {
            Request request = new Request();
            Object result = request.execute("/v1/Booking/List", "GET", new JSONObject(), Session.getToken()).get();
            data = this.getResponseData(result);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Exception while getting Items: " + e);
        }
        return data;
    }

    /**
     * get Orders belonging to VIP-costumer or Employee
     * @return
     */
    public JSONObject getMyOrders() {
        JSONObject responseData = null;
        try {
            Request request = new Request();
            Object result =  request.execute("/v1/Booking", "GET", new JSONObject(), Session.getToken()).get();
            responseData = this.getResponseData(result);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Exception while getting Orders: " + e);
        }
        return responseData;
    }

    /**
     * place Order
     * @param orderList
     * @return
     */
    public Boolean placeOrder(JSONArray orderList) {
        boolean response = false;
        if (Session.isSeat()) {
            try {
                Request request = new Request();
                request.execute("/v1/Booking", "POST", orderList, Session.getToken()).get();
                response = true;
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error while placing Order: " + e);
                response = false;
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * take Order
     * @param orderList
     * @return
     */
    public JSONObject takeOrder(JSONArray orderList) {
        JSONObject responseData = null;
        if(Session.isEmployee()) {
            try {
                Request request = new Request();
                JSONObject result = (JSONObject) request.execute("/v1/Booking", "PUT", orderList, Session.getToken()).get();
                responseData = this.getResponseData(result);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error while placing order: " + e);
                e.printStackTrace();
            }
        }
        return responseData;
    }

    /**
     * remnove Order
     * @param orderID
     * @return
     */
    public boolean cancelOrder(int orderID) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", orderID);
            Request request = new Request();
            JSONObject result = (JSONObject) request.execute("/v1/Booking/Cancel", "PUT", requestBody, Session.getToken()).get();
            this.getResponseData(result);
        } catch (JSONException | InterruptedException | ExecutionException e) {
            System.out.println("Error while removing order: " + e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * confirm Order
     * @param orderID
     * @return
     */
    public boolean confirmOrder(int orderID) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", orderID);
            Request request = new Request();
            JSONObject result = (JSONObject) request.execute("/v1/Booking/Confirm", "PUT", requestBody, Session.getToken()).get();
            this.getResponseData(result);
        } catch (JSONException | InterruptedException | ExecutionException e) {
            System.out.println("Error while removing order: " + e);
            e.printStackTrace();
            return false;
        }
        return true;
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
            Log.i("instance of array ", String.valueOf(param[2] instanceof JSONArray));
            Log.i("instance of object ", String.valueOf(param[2] instanceof JSONObject));

            try {

                // Open new Connection to server and send data
                Log.i("Methode ",method);
                Connection connection = new Connection(baseUrl+endpoint, method, param[3].toString());
                if(method.equalsIgnoreCase("POST")|| method.equalsIgnoreCase("PUT")) {
                    connection.send(param[2].toString());
//                    if(param[2] instanceof JSONArray){
//                        JSONArray data = (JSONArray) param[2];
//                        connection.send(data.toString());
//                        Log.i("DatenArray gesendet: ",data.toString());
//                    }
//                    else {
//                        JSONObject data = (JSONObject) param[2];
//                        connection.send(data.toString());
//                        Log.i("DatenObject gesendet: ",data.toString());
//                    }
                    Log.i("Connection gesendet ","");
                }


                // if there is a response code AND that response code is 200 OK
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    String received = connection.receive();
                    Log.i("Data received: ",received);
//                    JSONObject response = new JSONObject(received);
//                    Object jwt = response.get("token");
//                    responseData = new JSONObject(response.get("data").toString());
                    if(!received.isEmpty()){
                        responseData = new JSONObject(received);
                    }
                } else {
                    // Server returned HTTP error code.
                    Log.i("HTTP RESPONSE CODE: ", String.valueOf(responseCode));
                }


                // Close the connection to the server
                if(method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
                    connection.closeWithWriter();
                }else{
                    connection.close();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return responseData;
        }

//        @Override
//        protected void onPostExecute(Object o) {
//            super.onPostExecute(o);
//        }
    }

    /**
     * Connection class
     */
    private class Connection {
        private HttpURLConnection conn;
        private BufferedReader reader;
        private OutputStreamWriter writer;

        /**
         * Open new connection
         * @param url
         * @param method
         * @throws IOException
         */
        public Connection(String url, String method, String token) throws IOException {
            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setConnectTimeout(1000);

            conn.setReadTimeout(1000);

            conn.setDoInput(true);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("token", token);


            if(method.equalsIgnoreCase("POST")|| method.equalsIgnoreCase("PUT")) {
                conn.setDoOutput(true);
                writer = new OutputStreamWriter(conn.getOutputStream());
            }
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

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = "";
            String line;
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
            reader.close();
            conn.disconnect();
        }

        /**
         * close connection
         * @throws IOException
         */
        public void closeWithWriter () throws IOException{
            writer.close();
            reader.close();
            conn.disconnect();
        }

    }
}