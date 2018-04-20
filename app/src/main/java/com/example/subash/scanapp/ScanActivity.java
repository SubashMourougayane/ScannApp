package com.example.subash.scanapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.content.ContentValues.TAG;

public class ScanActivity extends Activity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    public static String scanned_text;
    String C_name,P_name,fassai,M_date,E_date;
    File root;
    File filepath;
    FileWriter writer;
    int length = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("asdf", rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        scanned_text = rawResult.getText();
        System.out.println("RESULT IS "+scanned_text);
        mScannerView.stopCamera();
        AlertDialog alertDialog = new AlertDialog.Builder(ScanActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(scanned_text);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Product",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mScannerView.startCamera();
                        //add product to json

                        C_name = scanned_text.split(",")[0];
                        P_name = scanned_text.split(",")[1];
                        fassai = scanned_text.split(",")[2];
                        M_date = scanned_text.split(",")[3];
                        E_date = scanned_text.split(",")[4];



                        JSONObject JO = new JSONObject();

                        JSONArray JA = new JSONArray();
                        JA.put(C_name);
                        JA.put(P_name);
                        JA.put(fassai);
                        JA.put(M_date);
                        JA.put(E_date);
                        root = new File(Environment.getExternalStorageDirectory(), "ScanApp");
                        File file = new File(root,"scan.txt");

                        StringBuilder text = new StringBuilder();

                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;

                            while ((line = br.readLine()) != null) {
                                text.append(line);
                                text.append('\n');
                            }
                            br.close();
                            System.out.println("qwerqwer"+text);
                            JSONObject JSON_Obj = new JSONObject(text.toString());
                            System.out.println("asdfasdf "+JSON_Obj.length());
                            length = JSON_Obj.length();
                            root = new File(Environment.getExternalStorageDirectory(), "ScanApp");


// if external memory exists and folder with name Notes
                            if (!root.exists()) {
                                root.mkdirs(); // this will create folder.
                            }
                            filepath = new File(root, "scan" + ".txt");  // file path to save
                            writer = new FileWriter(filepath);
                            JSON_Obj.put(Integer.toString(length+1),JA);
                            System.out.print("FINAL JSON IS "+JSON_Obj.toString());
                            writer.append(JSON_Obj.toString());
                            writer.flush();
                            writer.close();





                        }
                        catch (Exception e) {
                            try {

                                JO.put(Integer.toString(length+1),JA);
                                root = new File(Environment.getExternalStorageDirectory(), "ScanApp");
// if external memory exists and folder with name Notes
                                if (!root.exists()) {
                                    root.mkdirs(); // this will create folder.
                                }
                                filepath = new File(root, "scan" + ".txt");  // file path to save
                                writer = new FileWriter(filepath);
                                writer.append(JO.toString());
                                writer.flush();
                                writer.close();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            System.out.println("JSON IS "+JO);
                            //You'll need to add proper error handling here
                            System.out.println("EXCEPTIONNNNN "+e);
                        }




                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mScannerView.startCamera();

                        dialog.dismiss();
                    }

                });

        alertDialog.show();
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
}
