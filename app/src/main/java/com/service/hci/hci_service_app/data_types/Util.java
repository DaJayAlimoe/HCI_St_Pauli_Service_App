package com.service.hci.hci_service_app.data_types;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static Timestamp parseTimestamp(String localDateTime) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date parsedDate = dateFormat.parse(localDateTime);
        return new Timestamp(parsedDate.getTime());
    }
}
