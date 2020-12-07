package com.example.datamanagementexternalstorage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    private final String fileName = "data.txt";
    private String baseFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            //baseFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

            if (!file.exists()) {
                try {

                    writeFile(file);

                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_SHORT).show();
                }
            }
            // Read the data from the text file and display it
            try {

                readFile(file);

            } catch (IOException e) {
                Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void writeFile(File outFile) throws FileNotFoundException {

        FileOutputStream fos = new FileOutputStream(outFile);

        PrintWriter pw = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(fos)));

        pw.println("Line 1: This is a test of the File Writing API");
        pw.println("Line 2: This is a test of the File Writing API");
        pw.println("Line 3: This is a test of the File Writing API");

        pw.close();

    }

    private void readFile(File inFile) throws IOException {

        FileInputStream fis = new FileInputStream(inFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = "";

        while (null != (line = br.readLine())) {

            TextView tv = findViewById(R.id.fileOutTV);
            tv.append(line+"\n");

        }

        br.close();

    }
}
