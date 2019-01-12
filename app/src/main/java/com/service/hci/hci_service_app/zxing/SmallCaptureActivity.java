package com.service.hci.hci_service_app.zxing;
import com.service.hci.hci_service_app.R;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * This activity has a margin.
 */
public class SmallCaptureActivity extends CaptureActivity {
    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.capture_small);
        return (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }
}
