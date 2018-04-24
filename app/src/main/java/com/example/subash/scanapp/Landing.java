package com.example.subash.scanapp;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Landing extends AppCompatActivity {
    File root;
    File filepath;
    FileWriter writer;
    int length;
    Button scan;
    String C_name,P_name,fassai,M_date,E_date,scanned_text;

    ArrayList<String> ProdName = new ArrayList<>();
    ArrayList<String> CompName = new ArrayList<>();
    ArrayList<String> ExpDate = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        scan = (Button) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("boww thaka boww");
                startActivity(new Intent(getApplicationContext(), ScanActivity.class));
            }
        });
        root = new File(Environment.getExternalStorageDirectory(), "ScanApp");
        File file = new File(root, "scan.txt");

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            JSONObject JSON_Obj = new JSONObject(text.toString());
            length = JSON_Obj.length();

            for (int i=1;i<=length;i++){
                System.out.println("looool "+i+" is "+JSON_Obj.get(Integer.toString(i)));
                scanned_text = JSON_Obj.get(Integer.toString(i)).toString();
                C_name = scanned_text.split(",")[0];
                P_name = scanned_text.split(",")[1];
                fassai = scanned_text.split(",")[2];
                M_date = scanned_text.split(",")[3];
                E_date = scanned_text.split(",")[4];
                //adding list da hhs
                ProdName.add(P_name);
                CompName.add(C_name);
                ExpDate.add(E_date);
            }

            System.out.println("hhs LISTS are "+ ProdName);
            System.out.println("hhs LISTS are "+ CompName);
            System.out.println("hhs LISTS are "+ ExpDate);


        } catch (Exception e) {
            System.out.println("Expetion Occuered "+e);
        }
    }
}
