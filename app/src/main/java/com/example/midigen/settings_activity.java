package com.example.midigen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Objects;

public class settings_activity extends AppCompatActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");

        spinner = findViewById(R.id.filespinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.file_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onClickSave(View view) {
        int filePreference = spinner.getSelectedItemPosition();
        Intent saveExit = new Intent(getBaseContext(), MainActivity.class);
        saveExit.putExtra("filePreference", filePreference);
        startActivity(saveExit);
    }
}
