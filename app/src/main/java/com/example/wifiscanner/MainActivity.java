package com.example.wifiscanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.cameraImage);
        if (checkAndRequestPermissions(MainActivity.this))
            openScanner();

    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        List<String> permissionNeeded = new ArrayList<>();
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        if (cameraPermission != PackageManager.PERMISSION_GRANTED)
            permissionNeeded.add(Manifest.permission.CAMERA);

        if (permissionNeeded.size() > 0) {
            ActivityCompat.requestPermissions(context, permissionNeeded
                            .toArray(new String[permissionNeeded.size()]),
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            if(isWifiQr(scanResult)){
                HashMap<String, String> content = contentParsing(scanResult.getContents());
                connectToWifi(content);
            }
            else{
                Toast.makeText(getApplicationContext(),
                        "QR Code is not of Wifi", Toast.LENGTH_LONG)
                        .show();
            }
        }


    }
    private boolean isWifiQr(IntentResult scanResult){
        String content = scanResult.toString();
        return content.indexOf("WIFI") != -1;
    }
    public HashMap<String, String> contentParsing(String content) {
        StringTokenizer tokens = new StringTokenizer(content, ":,;");

        String idLabelValue = tokens.nextToken();
        String ssidLabel = tokens.nextToken();
        String ssid = tokens.nextToken();
        String typeLabel = tokens.nextToken();
        String type = tokens.nextToken();
        String passwordLabel = tokens.nextToken();
        String password= tokens.nextToken();

        HashMap<String, String> map = new HashMap<>();
        map.put("id", idLabelValue);
        map.put("ssid", ssid);
        map.put("type", type);
        map.put("password", password);

        return map;
    }
    private void connectToWifi(HashMap<String, String> content){
    }



}