package com.example.wifiscanner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wifiscanner.databinding.ActivityMainBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_ID = 101;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.cameraImage);
        if (checkAndRequestPermissions(MainActivity.this))
            openScanner();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            
        }
        // else continue with any other code you need in the method

    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[] {Manifest.permission.CAMERA},
                    REQUEST_CAMERA_ID);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CAMERA_ID:
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "ALERT: Access to Camera required.", Toast.LENGTH_LONG)
                            .show();
                } else {
                    openScanner();
                }
                break;
        }
    }
    public void openScanner(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        List<String> listQrCode = new ArrayList<String>();
        listQrCode.add(IntentIntegrator.QR_CODE);
        intentIntegrator.setDesiredBarcodeFormats(listQrCode);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setPrompt("Scan QR Code of Wi-fi");
        intentIntegrator.initiateScan();
    }



}