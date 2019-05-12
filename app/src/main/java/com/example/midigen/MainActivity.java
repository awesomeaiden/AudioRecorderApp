package com.example.midigen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button buttonstart;
    Button buttonstop;
    public String pathSave = "";
    MediaRecorder mediaRecorder;
    public int filePreference;

    final int REQUEST_PERMISSION_CODE = 1000;

    public void onClickFiles(View view) {
        Intent goToFiles = new Intent(getBaseContext(), files_activity.class);
        startActivity(goToFiles);
    }

    public void onClickSettings(View view) {
        Intent goToSettings = new Intent(getBaseContext(), settings_activity.class);
        startActivity(goToSettings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonstart = findViewById(R.id.buttonstart);
        buttonstop = findViewById(R.id.buttonstop);
        buttonstop.setEnabled(false);

        buttonstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionFromDevice()) {
                    setupMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    buttonstart.setEnabled(false);
                    buttonstop.setEnabled(true);

                    Toast.makeText(MainActivity.this, "Recording!", Toast.LENGTH_SHORT).show();
                }
                else
                    requestPermission();
            }
        });

        buttonstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                }
                buttonstop.setEnabled(false);
                buttonstart.setEnabled(true);
            }
        });
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // Get file preference
        filePreference = getIntent().getIntExtra("filePreference", 0);
        switch (filePreference) {
            case 0:
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.THREE_GPP);
                pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + UUID.randomUUID().toString() + "_audio_record.3gp";
                break;
            case 1:
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4);
                pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + UUID.randomUUID().toString() + "_audio_record.mp4";
                break;
        }
        mediaRecorder.setOutputFile(pathSave);
    }

    private boolean checkPermissionFromDevice() {
        int writeexternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recordaudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        return writeexternal == PackageManager.PERMISSION_GRANTED && recordaudio == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}
