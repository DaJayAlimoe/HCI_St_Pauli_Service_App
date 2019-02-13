package com.service.hci.hci_service_app.activity_handler;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.CustomerMain;
import com.service.hci.hci_service_app.activity_handler.service.ServiceMain;
import com.service.hci.hci_service_app.data_layer.Api;
import com.service.hci.hci_service_app.data_layer.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Main extends AppCompatActivity implements View.OnClickListener {

    private Button btn_service;
    private Button btn_customer;
    private Button btn_qr;

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "ScanQRCode";

    private static final int MY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Session(this);
        Session.remove();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // test comment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // the button are invisible now, but still there. to reactivate go to main.xml and click on the button -> visibility -> none
        // findViewById for booth buttons and add a onClickListener object to it
//        this.btn_customer = findViewById(R.id.btn_main_to_customer);
//        btn_customer.setOnClickListener(this);
//        this.btn_service = findViewById(R.id.btn_main_to_service);
//        btn_service.setOnClickListener(this);
        this.btn_qr = findViewById(R.id.btn_main_to_qr);
        btn_qr.setOnClickListener(this);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            Intent intent = new Intent(getApplicationContext(), Main.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

//    private void appCurrentlyRun() {
//        // check, if session already available
//        Intent intent;
//        if (Session.isSeat()) {
//            intent = new Intent(Main.this, CustomerMain.class);
//            startActivity(intent);
//            finish();
//        } else if (Session.isEmployee()) {
//            intent = new Intent(Main.this, ServiceMain.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    public void onClick(View v) {
        int actView = v.getId();

        if (actView == R.id.btn_main_to_customer) {
            Intent intent = new Intent(Main.this, CustomerMain.class);
            startActivity(intent);
            finish();
        } else if (actView == R.id.btn_main_to_service) {
            Intent intent = new Intent(Main.this, ServiceMain.class);
            startActivity(intent);
            finish();
        } else if (actView == R.id.btn_main_to_qr) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);

            } else {
                Intent intent = new Intent(this, QrCodeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_QR_SCAN);
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(this, QrCodeActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_QR_SCAN);

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Zugriffsfehler");
                    alertDialog.setMessage("Zugriff auf Speicher und Kamera wird für QR Code Scanner benötigt");
                }
                return;
            }


        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            Session session = new Session(this);

            Api stApi = Api.getInstance();

            boolean auth = false;

            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.i(LOGTAG, "QR Code scanned; result is: " + result);

            stApi.authenticate(result);

            // check token
            Intent intent;
            if (session.isSeat()) {
                intent = new Intent(Main.this, CustomerMain.class);
            } else if (session.isEmployee()) {
                intent = new Intent(Main.this, ServiceMain.class);
            } else {
                intent = new Intent(Main.this, Main.class);
            }

            startActivity(intent);
            finish();
        }
    }

//    public String test() {
//        Api stApi = Api.getInstance();
//
//        //employee test
//        boolean auth = stApi.authenticate(this,"seat:4842f5964017ee57");
//
//        Log.i("Authenticate- flag: ", Boolean.toString(auth));
//        Log.i("isSeat: ", Boolean.toString(Session.isSeat()));
//        Log.i("isEmployee: ", Boolean.toString(Session.isEmployee()));
//
//
//        JSONObject items = stApi.getItems();
//        Log.i("items customer: ", items.toString());
//
//
//        JSONArray orderList = new JSONArray();
//        try {
//            int userID = Session.getUserId();
//            JSONObject j1 = new JSONObject();
//            j1.put("amount",5);
//            j1.put("item_id",2);
//            j1.put("seat_id", userID);
//            orderList.put(j1);
//
//            JSONObject j2 = new JSONObject();
//            j2.put("amount",5);
//            j2.put("item_id",1);
//            j2.put("seat_id", userID);
//            orderList.put(j2);
//
//            JSONObject j3 = new JSONObject();
//            j3.put("amount",5);
//            j3.put("item_id",3);
//            j3.put("seat_id", userID);
//            orderList.put(j3);
//
//            JSONObject j4 = new JSONObject();
//            j4.put("amount",5);
//            j4.put("item_id",3);
//            j4.put("seat_id", userID);
//            orderList.put(j4);
//
//            JSONObject j5 = new JSONObject();
//            j5.put("amount",5);
//            j5.put("item_id",3);
//            j5.put("seat_id", userID);
//            orderList.put(j5);
//
//            JSONObject j6 = new JSONObject();
//            j6.put("amount",5);
//            j6.put("item_id",3);
//            j6.put("seat_id", userID);
//            orderList.put(j6);
//
//            JSONObject j7 = new JSONObject();
//            j7.put("amount",5);
//            j7.put("item_id",3);
//            j7.put("seat_id", userID);
//            orderList.put(j7);
//
//            Log.i("orderList seat: ", orderList.toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        boolean placedOrders = stApi.placeOrder(orderList);
//        Log.i("placed Orders seat: ", Boolean.toString(placedOrders));
//
//
//        JSONObject myOrders = stApi.getMyOrders();
//        Log.i("myOrders seat: ", myOrders.toString());
//
//        boolean isCanc = stApi.cancelOrder(63);
//        Log.i("isCanceld seat: ", Boolean.toString(isCanc));
//
//
//        // service test
//        boolean auth1 = stApi.authenticate(this,"empl:7d3248f91c93cfa");
//        Log.i("Authenticate1- flag: ", Boolean.toString(auth1));
//        Log.i("isSeat: ", Boolean.toString(Session.isSeat()));
//        Log.i("isEmployee: ", Boolean.toString(Session.isEmployee()));
//
//        JSONObject orders = stApi.getOrders();
//        Log.i("orders empl: ", orders.toString());
//
//        JSONObject myOrdersEmpl = stApi.getMyOrders();
//        Log.i("myOrders empl: ", myOrdersEmpl.toString());
//
//        items = stApi.getItems();
//        Log.i("items empl: ", items.toString());
//
//        JSONArray orderList1 = new JSONArray();
//
//        try {
//            JSONObject order1 = new JSONObject();
//            order1.put("booking_id",65);
//            JSONObject order2 = new JSONObject();
//            order2.put("booking_id",64);
//
//            orderList1.put(order1);
//            orderList1.put(order2);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Log.i("takeOrders empl: ", orderList1.toString());
//        JSONObject takenOrders = stApi.takeOrder(orderList1);
//        Log.i("takeOrders empl: ", takenOrders.toString());
//
//       boolean confirmOrder = stApi.confirmOrder(65);
//        Log.i("isConfirmed empl: ", Boolean.toString(confirmOrder));
//
//        Log.i("test complete: ", Boolean.toString(true));
//        return "test complete";
//    }
}


// output

//2019-02-09 14:39:17.622 27582-27600/com.service.hci.hci_service_app I/instance of array: false
//        2019-02-09 14:39:17.622 27582-27600/com.service.hci.hci_service_app I/instance of object: true
//        2019-02-09 14:39:17.622 27582-27600/com.service.hci.hci_service_app I/Methode: GET
//        2019-02-09 14:39:17.625 27582-27600/com.service.hci.hci_service_app D/NetworkSecurityConfig: No Network Security Config specified, using platform default
//2019-02-09 14:39:17.874 27582-27600/com.service.hci.hci_service_app I/Data received:: {"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"employee":null}
//        2019-02-09 14:39:17.896 27582-27582/com.service.hci.hci_service_app I/Authenticate- flag:: true
//        2019-02-09 14:39:17.896 27582-27582/com.service.hci.hci_service_app I/isSeat:: true
//        2019-02-09 14:39:17.896 27582-27582/com.service.hci.hci_service_app I/isEmployee:: false
//        2019-02-09 14:39:17.897 27582-27604/com.service.hci.hci_service_app I/instance of array: false
//        2019-02-09 14:39:17.897 27582-27604/com.service.hci.hci_service_app I/instance of object: true
//        2019-02-09 14:39:17.897 27582-27604/com.service.hci.hci_service_app I/Methode: GET
//        2019-02-09 14:39:17.922 27582-27604/com.service.hci.hci_service_app I/Data received:: {"items":[{"id":1,"name":"Bier","description":"Ein cooles Blondes Pils","pic_url":"Bier_pic"},{"id":2,"name":"Cola","description":"Coca Cola","pic_url":"Cola_pic"},{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"},{"id":4,"name":"Wasser","description":"Wasser","pic_url":"Wasser_pic"},{"id":5,"name":"Sprite","description":"Sprite","pic_url":"Sprite_pic"},{"id":6,"name":"Breezel","description":"Eine leckere st.p Paulianische Brääzzel","pic_url":"Breezel_pic"}]}
//        2019-02-09 14:39:17.926 27582-27582/com.service.hci.hci_service_app I/items customer:: {"items":[{"id":1,"name":"Bier","description":"Ein cooles Blondes Pils","pic_url":"Bier_pic"},{"id":2,"name":"Cola","description":"Coca Cola","pic_url":"Cola_pic"},{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"},{"id":4,"name":"Wasser","description":"Wasser","pic_url":"Wasser_pic"},{"id":5,"name":"Sprite","description":"Sprite","pic_url":"Sprite_pic"},{"id":6,"name":"Breezel","description":"Eine leckere st.p Paulianische Brääzzel","pic_url":"Breezel_pic"}]}
//        2019-02-09 14:39:17.928 27582-27582/com.service.hci.hci_service_app I/orderList seat:: [{"amount":5,"item_id":2,"seat_id":39},{"amount":5,"item_id":1,"seat_id":39},{"amount":5,"item_id":3,"seat_id":39},{"amount":5,"item_id":3,"seat_id":39},{"amount":5,"item_id":3,"seat_id":39},{"amount":5,"item_id":3,"seat_id":39},{"amount":5,"item_id":3,"seat_id":39}]
//        2019-02-09 14:39:17.932 27582-27604/com.service.hci.hci_service_app I/instance of array: true
//        2019-02-09 14:39:17.932 27582-27604/com.service.hci.hci_service_app I/instance of object: false
//        2019-02-09 14:39:17.932 27582-27604/com.service.hci.hci_service_app I/Methode: POST
//        2019-02-09 14:39:18.189 27582-27582/com.service.hci.hci_service_app I/placed Orders seat:: true
//        2019-02-09 14:39:18.190 27582-27604/com.service.hci.hci_service_app I/instance of array: false
//        2019-02-09 14:39:18.190 27582-27604/com.service.hci.hci_service_app I/instance of object: true
//        2019-02-09 14:39:18.190 27582-27604/com.service.hci.hci_service_app I/Methode: GET
//        2019-02-09 14:39:18.285 27582-27604/com.service.hci.hci_service_app I/Data received:: {"bookings":[{"id":59,"eta":0,"position":1,"activeAt":"2019-02-09T14:39:18.634052","createdOn":"2019-02-09T14:39:18.634052","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.634052","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":2,"name":"Cola","description":"Coca Cola","pic_url":"Cola_pic"}},{"id":60,"eta":0,"position":2,"activeAt":"2019-02-09T14:39:18.673945","createdOn":"2019-02-09T14:39:18.673945","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.673945","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":1,"name":"Bier","description":"Ein cooles Blondes Pils","pic_url":"Bier_pic"}},{"id":61,"eta":0,"position":3,"activeAt":"2019-02-09T14:39:18.681923","createdOn":"2019-02-09T14:39:18.681923","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.681923","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":62,"eta":0,"position":4,"activeAt":"2019-02-09T14:39:18.692894","createdOn":"2019-02-09T14:39:18.692894","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.692894","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":63,"eta":0,"position":5,"activeAt":"2019-02-09T14:39:18.700872","createdOn":"2019-02-09T14:39:18.700872","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.701869","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":64,"eta":5,"position":6,"activeAt":"2019-02-09T14:40:18.715832","createdOn":"2019-02-09T14:39:18.715832","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.715832","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":65,"eta":5,"position":7,"activeAt":"2019-02-09T14:40:18.730791","createdOn":"2019-02-09T14:39:18.730791","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.730791","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}}]}
//        2019-02-09 14:39:18.304 27582-27582/com.service.hci.hci_service_app I/myOrders seat:: {"bookings":[{"id":59,"eta":0,"position":1,"activeAt":"2019-02-09T14:39:18.634052","createdOn":"2019-02-09T14:39:18.634052","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.634052","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":2,"name":"Cola","description":"Coca Cola","pic_url":"Cola_pic"}},{"id":60,"eta":0,"position":2,"activeAt":"2019-02-09T14:39:18.673945","createdOn":"2019-02-09T14:39:18.673945","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.673945","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":1,"name":"Bier","description":"Ein cooles Blondes Pils","pic_url":"Bier_pic"}},{"id":61,"eta":0,"position":3,"activeAt":"2019-02-09T14:39:18.681923","createdOn":"2019-02-09T14:39:18.681923","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.681923","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":62,"eta":0,"position":4,"activeAt":"2019-02-09T14:39:18.692894","createdOn":"2019-02-09T14:39:18.692894","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.692894","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":63,"eta":0,"position":5,"activeAt":"2019-02-09T14:39:18.700872","createdOn":"2019-02-09T14:39:18.700872","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.701869","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":64,"eta":5,"position":6,"activeAt":"2019-02-09T14:40:18.715832","createdOn":"2019-02-09T14:39:18.715832","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.715832","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":65,"eta":5,"position":7,"activeAt":"2019-02-09T14:40:18.730791","createdOn":"2019-02-09T14:39:18.730791","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.730791","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}}]}
//        2019-02-09 14:39:18.306 27582-27600/com.service.hci.hci_service_app I/instance of array: false
//        2019-02-09 14:39:18.306 27582-27600/com.service.hci.hci_service_app I/instance of object: true
//        2019-02-09 14:39:18.306 27582-27600/com.service.hci.hci_service_app I/Methode: PUT
//        2019-02-09 14:39:18.392 27582-27582/com.service.hci.hci_service_app I/isCanceld seat:: true
//        2019-02-09 14:39:18.394 27582-27600/com.service.hci.hci_service_app I/instance of array: false
//        2019-02-09 14:39:18.394 27582-27600/com.service.hci.hci_service_app I/instance of object: true
//        2019-02-09 14:39:18.394 27582-27600/com.service.hci.hci_service_app I/Methode: GET
//        2019-02-09 14:39:18.417 27582-27600/com.service.hci.hci_service_app I/Data received:: {"seat":null,"employee":{"id":13,"name":"Employee1","validTimeStamp":"2019-02-09T13:38:57.077+0000","qrtoken":{"id":7,"token":"empl:7d3248f91c93cfa"}}}
//        2019-02-09 14:39:18.425 27582-27582/com.service.hci.hci_service_app I/Authenticate1- flag:: true
//        2019-02-09 14:39:18.425 27582-27582/com.service.hci.hci_service_app I/isSeat:: false
//        2019-02-09 14:39:18.425 27582-27582/com.service.hci.hci_service_app I/isEmployee:: true
//        2019-02-09 14:39:18.425 27582-27604/com.service.hci.hci_service_app I/instance of array: false
//        2019-02-09 14:39:18.425 27582-27604/com.service.hci.hci_service_app I/instance of object: true
//        2019-02-09 14:39:18.425 27582-27604/com.service.hci.hci_service_app I/Methode: GET
//        2019-02-09 14:39:18.448 27582-27604/com.service.hci.hci_service_app I/Data received:: {"bookings":[{"id":59,"eta":0,"position":1,"activeAt":"2019-02-09T14:39:18.634052","createdOn":"2019-02-09T14:39:18.634052","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.634052","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":2,"name":"Cola","description":"Coca Cola","pic_url":"Cola_pic"}},{"id":60,"eta":0,"position":2,"activeAt":"2019-02-09T14:39:18.673945","createdOn":"2019-02-09T14:39:18.673945","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.673945","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":1,"name":"Bier","description":"Ein cooles Blondes Pils","pic_url":"Bier_pic"}},{"id":61,"eta":0,"position":3,"activeAt":"2019-02-09T14:39:18.681923","createdOn":"2019-02-09T14:39:18.681923","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.681923","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":62,"eta":0,"position":4,"activeAt":"2019-02-09T14:39:18.692894","createdOn":"2019-02-09T14:39:18.692894","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.692894","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}}]}
//        2019-02-09 14:39:18.454 27582-27582/com.service.hci.hci_service_app I/orders empl:: {"bookings":[{"id":59,"eta":0,"position":1,"activeAt":"2019-02-09T14:39:18.634052","createdOn":"2019-02-09T14:39:18.634052","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.634052","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":2,"name":"Cola","description":"Coca Cola","pic_url":"Cola_pic"}},{"id":60,"eta":0,"position":2,"activeAt":"2019-02-09T14:39:18.673945","createdOn":"2019-02-09T14:39:18.673945","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.673945","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":1,"name":"Bier","description":"Ein cooles Blondes Pils","pic_url":"Bier_pic"}},{"id":61,"eta":0,"position":3,"activeAt":"2019-02-09T14:39:18.681923","createdOn":"2019-02-09T14:39:18.681923","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.681923","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":62,"eta":0,"position":4,"activeAt":"2019-02-09T14:39:18.692894","createdOn":"2019-02-09T14:39:18.692894","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.692894","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}}]}
//        2019-02-09 14:39:18.455 27582-27600/com.service.hci.hci_service_app I/instance of array: false
//        2019-02-09 14:39:18.455 27582-27600/com.service.hci.hci_service_app I/instance of object: true
//        2019-02-09 14:39:18.455 27582-27600/com.service.hci.hci_service_app I/Methode: GET
//        2019-02-09 14:39:18.486 27582-27600/com.service.hci.hci_service_app I/Data received:: {"bookings":[]}
//        2019-02-09 14:39:18.490 27582-27582/com.service.hci.hci_service_app I/myOrders empl:: {"bookings":[]}
//        2019-02-09 14:39:18.492 27582-27600/com.service.hci.hci_service_app I/instance of array: false
//        2019-02-09 14:39:18.492 27582-27600/com.service.hci.hci_service_app I/instance of object: true
//        2019-02-09 14:39:18.492 27582-27600/com.service.hci.hci_service_app I/Methode: GET
//        2019-02-09 14:39:18.509 27582-27600/com.service.hci.hci_service_app I/Data received:: {"items":[{"id":1,"name":"Bier","description":"Ein cooles Blondes Pils","pic_url":"Bier_pic"},{"id":2,"name":"Cola","description":"Coca Cola","pic_url":"Cola_pic"},{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"},{"id":4,"name":"Wasser","description":"Wasser","pic_url":"Wasser_pic"},{"id":5,"name":"Sprite","description":"Sprite","pic_url":"Sprite_pic"},{"id":6,"name":"Breezel","description":"Eine leckere st.p Paulianische Brääzzel","pic_url":"Breezel_pic"}]}
//        2019-02-09 14:39:18.513 27582-27582/com.service.hci.hci_service_app I/items empl:: {"items":[{"id":1,"name":"Bier","description":"Ein cooles Blondes Pils","pic_url":"Bier_pic"},{"id":2,"name":"Cola","description":"Coca Cola","pic_url":"Cola_pic"},{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"},{"id":4,"name":"Wasser","description":"Wasser","pic_url":"Wasser_pic"},{"id":5,"name":"Sprite","description":"Sprite","pic_url":"Sprite_pic"},{"id":6,"name":"Breezel","description":"Eine leckere st.p Paulianische Brääzzel","pic_url":"Breezel_pic"}]}
//        2019-02-09 14:39:18.513 27582-27582/com.service.hci.hci_service_app I/takeOrders empl:: [{"booking_id":65},{"booking_id":64}]
//        2019-02-09 14:39:18.514 27582-27604/com.service.hci.hci_service_app I/instance of array: true
//        2019-02-09 14:39:18.514 27582-27604/com.service.hci.hci_service_app I/instance of object: false
//        2019-02-09 14:39:18.514 27582-27604/com.service.hci.hci_service_app I/Methode: PUT
//        2019-02-09 14:39:18.590 27582-27604/com.service.hci.hci_service_app I/Data received:: {"bookings":[{"id":59,"eta":0,"position":-1,"activeAt":"2019-02-09T14:39:18.634052","createdOn":"2019-02-09T14:39:18.634052","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.634052","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":2,"name":"Cola","description":"Coca Cola","pic_url":"Cola_pic"}},{"id":60,"eta":0,"position":0,"activeAt":"2019-02-09T14:39:18.673945","createdOn":"2019-02-09T14:39:18.673945","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.673945","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":1,"name":"Bier","description":"Ein cooles Blondes Pils","pic_url":"Bier_pic"}},{"id":61,"eta":0,"position":1,"activeAt":"2019-02-09T14:39:18.681923","createdOn":"2019-02-09T14:39:18.681923","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.681923","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":62,"eta":0,"position":2,"activeAt":"2019-02-09T14:39:18.692894","createdOn":"2019-02-09T14:39:18.692894","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.692894","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}}]}
//        2019-02-09 14:39:18.596 27582-27582/com.service.hci.hci_service_app I/takeOrders empl:: {"bookings":[{"id":59,"eta":0,"position":-1,"activeAt":"2019-02-09T14:39:18.634052","createdOn":"2019-02-09T14:39:18.634052","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.634052","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":2,"name":"Cola","description":"Coca Cola","pic_url":"Cola_pic"}},{"id":60,"eta":0,"position":0,"activeAt":"2019-02-09T14:39:18.673945","createdOn":"2019-02-09T14:39:18.673945","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.673945","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":1,"name":"Bier","description":"Ein cooles Blondes Pils","pic_url":"Bier_pic"}},{"id":61,"eta":0,"position":1,"activeAt":"2019-02-09T14:39:18.681923","createdOn":"2019-02-09T14:39:18.681923","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.681923","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}},{"id":62,"eta":0,"position":2,"activeAt":"2019-02-09T14:39:18.692894","createdOn":"2019-02-09T14:39:18.692894","status":"PREORDERED","amount":5,"version":0,"lastUpdatedOn":"2019-02-09T14:39:18.692894","employee":null,"seat":{"id":39,"seatNr":0,"qrtoken":{"id":19,"token":"seat:4842f5964017ee57"}},"item":{"id":3,"name":"Fanta","description":"Fanta","pic_url":"Fanta_pic"}}]}
//        2019-02-09 14:39:18.597 27582-27600/com.service.hci.hci_service_app I/instance of array: false
//        2019-02-09 14:39:18.598 27582-27600/com.service.hci.hci_service_app I/instance of object: true
//        2019-02-09 14:39:18.598 27582-27600/com.service.hci.hci_service_app I/Methode: PUT
//        2019-02-09 14:39:18.657 27582-27582/com.service.hci.hci_service_app I/isConfirmed empl:: true
//        2019-02-09 14:39:18.657 27582-27582/com.service.hci.hci_service_app I/test complete:: true

