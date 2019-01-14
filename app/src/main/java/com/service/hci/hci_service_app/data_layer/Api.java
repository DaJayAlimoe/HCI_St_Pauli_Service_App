package com.service.hci.hci_service_app.data_layer;

import android.os.AsyncTask;
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

    public Api(){
        baseUrl = "http://http://localhost:443/";
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
    public boolean authenticate(String qrToken) {
        boolean identified = false;
        Request request = new Request();
        try {
            Object result = request.execute("/v1/User", "GET", new JSONObject()).get();
            JSONObject data = this.getResponseData(result);
            if(data != null) {
                if(!data.isNull("employee")) {
                    Session.setUserData("isEmployee", Boolean.TRUE);
                    Session.setUserData("id", data.getJSONObject("employee").getInt("id"));
                    identified = true;
                }else if(!data.isNull("seat")) {
                    Session.setUserData("isEmployee", Boolean.FALSE);
                    Session.setUserData("id", data.getJSONObject("seat").getInt("id"));
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
    public JSONObject placeOrder(JSONObject orderList) {
        JSONObject responseData = null;
        if (Session.user("isEmployee").equals(Boolean.FALSE)) {
            try {
                Request request = new Request();
                JSONObject result = (JSONObject) request.execute("/v1/Booking", "POST", orderList, Session.getToken()).get();
                responseData = this.getResponseData(result);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error while placing Order: " + e);
                e.printStackTrace();
            }
        }
        return responseData;
    }

    /**
     * take Order
     * @param orderList
     * @return
     */
    public JSONObject takeOrder(JSONObject orderList) {
        JSONObject responseData = null;
        if(Session.user("isEmployee").equals(Boolean.TRUE)) {
            try {
                Request request = new Request();
                JSONObject result = (JSONObject) request.execute("/v1/booking", "PUT", orderList, Session.getToken()).get();
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
    public void removeOrder(int orderID) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", orderID);
            Request request = new Request();
            JSONObject result = (JSONObject) request.execute("/v1/booking", "DELETE", requestBody, Session.getToken()).get();
            this.getResponseData(result);
        } catch (JSONException | InterruptedException | ExecutionException e) {
            System.out.println("Error while removing order: " + e);
            e.printStackTrace();
        }
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
                Connection connection = new Connection(baseUrl+endpoint, method, param[3].toString());
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
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("token", token);
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
            writer.close();
            reader.close();
            conn.disconnect();
        }

    }
}