package com.service.hci.hci_service_app.data_types;

import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Util {
    public static Timestamp parseTimestamp(String localDateTime) {
        return Timestamp.valueOf(localDateTime.replace("T"," ").replace("Z",""));
    }
}
