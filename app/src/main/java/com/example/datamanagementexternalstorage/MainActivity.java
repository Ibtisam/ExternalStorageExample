package com.example.datamanagementexternalstorage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    private String fileName = "data";
    private EditText editText;
    private File primaryExternalStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!isExternalStorageWritable()){
            Toast.makeText(this, "Media not available, app will exit", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }finally {
                finish();
            }
        }
    }

    // Checks if a volume containing external storage is available
    // for read and write.
    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // Checks if a volume containing external storage is available to at least read.
    private boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    private void writeToFile(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));

        pw.println(editText.getText().toString());
        pw.close();

    }

    private void readFromFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = "";
        while (null != (line = br.readLine())) {
            editText.append(line);
        }
        br.close();
    }

    public void filesWBClicked(View v){
        try {
            //selecting the external storage in case sd card is also available
            File[] externalStorageVolumes = getExternalFilesDirs(null);
            primaryExternalStorage = externalStorageVolumes[0];
            File file = new File(primaryExternalStorage, fileName);
            writeToFile(file);
        } catch (IOException e) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_SHORT).show();
        }
    }

    public void cacheWBClicked(View v){
        try {
            File[] externalStorageVolumes = getExternalCacheDirs();
            primaryExternalStorage = externalStorageVolumes[0];
            File file = new File(primaryExternalStorage, fileName);
            writeToFile(file);
        } catch (IOException e) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_SHORT).show();
        }
    }

    public void filesRBClicked(View v){
        try {
            File[] externalStorageVolumes = getExternalFilesDirs(null);
            primaryExternalStorage = externalStorageVolumes[0];
            File file = new File(primaryExternalStorage, fileName);
            readFromFile(file);
        } catch (IOException e) {
            Toast.makeText(this, "IOException"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void cacheRBClicked(View v){
        try {
            File[] externalStorageVolumes = getExternalCacheDirs();
            primaryExternalStorage = externalStorageVolumes[0];
            File file = new File(primaryExternalStorage, fileName);
            readFromFile(file);
        } catch (IOException e) {
            Toast.makeText(this, "IOException"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void resetBClicked(View v){
        editText.setText("");
    }

    public void delBClicked(View v){
        File cacheDir = getExternalCacheDir();
        for(File file:cacheDir.listFiles()){
            file.delete();
        }
        File filesDir = getExternalFilesDir(null);
        for(File file:filesDir.listFiles()){
            file.delete();
        }
        Toast.makeText(this, "All files deleted from internal storage", Toast.LENGTH_SHORT).show();
    }

}
