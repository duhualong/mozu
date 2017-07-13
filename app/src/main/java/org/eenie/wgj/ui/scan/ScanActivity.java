package org.eenie.wgj.ui.scan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import org.eenie.wgj.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/12 at 19:13
 * Email: 472279981@qq.com
 * Des:
 */

public class ScanActivity extends AppCompatActivity {


    @BindView(R.id.barcode_scanner_view)
    CompoundBarcodeView barcodeScannerView;
    private CaptureManager capture;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_setting);
        ButterKnife.bind(this);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }
    @OnClick(R.id.img_back)public void onClick(){
        onBackPressed();
    }



    @Override protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }



    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
