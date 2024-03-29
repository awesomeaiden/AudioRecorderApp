package com.example.midigen;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;

public class files_activity extends AppCompatActivity {

    private TextView pathshow;
    private Button btn_startplay;
    private Button btn_stopplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_activity);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Media Player");

        pathshow = findViewById(R.id.filepath);
        btn_startplay = findViewById(R.id.btn_startplay);
        btn_stopplay = findViewById(R.id.btn_stopplay);
        btn_startplay.setEnabled(false);
        btn_stopplay.setEnabled(false);
    }

    public void onClickFile(View view) {
        Intent openFiles = new Intent(Intent.ACTION_GET_CONTENT);
        openFiles.setType("*/*");
        startActivityForResult(openFiles, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == 10) && (resultCode == RESULT_OK)){
            String filepath = Objects.requireNonNull(Objects.requireNonNull(data).getData()).getPath();
            pathshow.setText(filepath);
            btn_startplay.setEnabled(true);
        }
    }

    public void onClickPlay(View view) {
        btn_stopplay.setEnabled(true);
        btn_startplay.setEnabled(false);

        final MediaPlayer mediaPlayer = new MediaPlayer();
        String relpath = pathshow.getText().toString();

        if (relpath.length() > 53) {
            try{
                String filepathplay = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + (relpath.substring(relpath.length() - 53));
                mediaPlayer.setDataSource(filepathplay);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Toast.makeText(this, "Starting Playback...", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Invalid File!", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "Invalid File!", Toast.LENGTH_LONG).show();
        }

        btn_stopplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_startplay.setEnabled(true);
                btn_stopplay.setEnabled(false);
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }
}
