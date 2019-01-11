package com.service.hci.hci_service_app.data_layer;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.json.*;
import javax.net.ssl.HttpsURLConnection;

public class Api {
    private String baseUrl;

    public Api(){
        baseUrl = "http://192.168.178.11:443/";
    }

    /**
     * get request body containing token
     * @return
     */
    private JSONObject requestBody() {
        String token = SessionManager.getToken();
        if(token != null) {
            try {
                return new JSONObject(String.format("{\"token\": \"%s\"}", token));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }

    /**
     * save response token and return response data
     * @param response
     * @return
     */
    private JSONObject getResponseData(JSONObject response) {
        JSONObject data = null;
        if(response != null && response.has("token")) {
            try {
                SessionManager.setUserData("token", response.get("token").toString());
                if (response.has("data"))
                    data = new JSONObject(response.get("data").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * authenticate qr code
     * @param qrToken
     */
    public void authenticate(String qrToken) {
        Request request = new Request();
        try {
            JSONObject requestBody = new JSONObject(String.format("{\"data\": {\"token\": \"%s\"}}", qrToken));
            JSONObject result = (JSONObject) request.execute("/v1/token", "GET", requestBody).get();
            JSONObject data = this.getResponseData(result);
            if(data != null) {
                if(data.has("employee")) {
                    SessionManager.setUserData("isEmployee", true);
                    SessionManager.setUserData("id", data.get("employee.id"));
                }else if(data.has("seat")) {
                    SessionManager.setUserData("isEmployee", false);
                    SessionManager.setUserData("id", data.get("seat.id"));
                }
            }
        } catch (InterruptedException | ExecutionException | JSONException e) {
            System.out.println("Exception while authenticating user: " + e);
        }
    }

    /**
     * get Items
     * @return
     */
    public JSONObject getItems() {
        JSONObject data = null;
        try {
            Request request = new Request();
            JSONObject requestBody = this.requestBody();
            JSONObject result = (JSONObject) request.execute("/v1/item", "GET", requestBody).get();
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
            JSONObject requestBody = this.requestBody();
            JSONObject result = (JSONObject) request.execute("/v1/booking/list", "GET", requestBody).get();
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
            JSONObject requestData = new JSONObject();
            if(!(Boolean) SessionManager.user("isEmployee")) {
                requestData.put("employee_id", SessionManager.user("id").toString());
            } else {
                requestData.put("seat_id", SessionManager.user("id").toString());
            }
            JSONObject requestBody = this.requestBody();
            requestBody.put("data", requestData);
            Request request = new Request();
            JSONObject result = (JSONObject) request.execute("/v1/booking/id", "GET", requestBody).get();
            responseData = this.getResponseData(result);
        } catch (InterruptedException | ExecutionException | JSONException e) {
            System.out.println("Exception while getting Items: " + e);
        }
        return responseData;
    }

    /**
     * place Order
     * @param orderList
     * @return
     */
    public JSONObject placeOrder(Map<Integer, Integer> orderList) {
        JSONObject responseData = null;
        if(!(Boolean) SessionManager.user("isEmployee")) {
            try {
                int itemCount = orderList.size();
                JSONObject requestData = new JSONObject();
                for (Map.Entry<Integer, Integer> entry : orderList.entrySet()) {
                    JSONObject order = new JSONObject();
                    order.put("item_id", entry.getKey());
                    order.put("amount", entry.getValue());
                    order.put("seat_id", SessionManager.user("id").toString());
                    requestData.put(String.valueOf(itemCount--), order);
                }
                JSONObject requestBody = this.requestBody();
                requestBody.put("data", requestData);
                Request request = new Request();
                JSONObject result = (JSONObject) request.execute("/v1/booking", "POST", requestBody).get();
                responseData = this.getResponseData(result);
            } catch (JSONException | InterruptedException | ExecutionException e) {
                System.out.println("Error while placing order: " + e);
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
    public JSONObject takeOrder(List<Integer> orderList) {
        JSONObject responseData = null;
        if((Boolean) SessionManager.user("isEmployee")) {
            try {
                int itemCount = orderList.size();
                JSONObject requestData = new JSONObject();
                for (Integer orderID : orderList) {
                    JSONObject order = new JSONObject();
                    order.put("id", orderID);
                    order.put("employee_id", SessionManager.user("id").toString());
                    requestData.put(String.valueOf(itemCount--), order);
                }
                JSONObject requestBody = this.requestBody();
                requestBody.put("data", requestData);
                Request request = new Request();
                JSONObject result = (JSONObject) request.execute("/v1/booking", "PUT", requestBody).get();
                responseData = this.getResponseData(result);
            } catch (JSONException | InterruptedException | ExecutionException e) {
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
            JSONObject requestData = new JSONObject();
            requestData.put("id", orderID);
            JSONObject requestBody = this.requestBody();
            requestBody.put("data", requestData);
            Request request = new Request();
            JSONObject result = (JSONObject) request.execute("/v1/booking", "DELETE", requestBody).get();
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