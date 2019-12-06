package com.shabywoks.songstudio.components;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shabywoks.songstudio.MainActivity;
import com.shabywoks.songstudio.R;
import com.shabywoks.songstudio.core.LocalDB;
import com.shabywoks.songstudio.core.StudioManager;

public class Payments extends AppCompatActivity {

    Button  proceed;
    Toolbar toolbarTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        proceed     = (Button)findViewById(R.id.proceed);
        toolbarTop  = (Toolbar) findViewById(R.id.toolbar_top);

        proceed.setOnClickListener(proceedClickListener);

        setSupportActionBar(toolbarTop);
        configureToolbar();
    }

    View.OnClickListener proceedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StudioManager.localDB.setBoolean(LocalDB.PREMIUM, true);
            Toast.makeText(Payments.this, "Your payment is done.", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        finish();
        return true;
    }

    private void configureToolbar() {
        getSupportActionBar().setTitle("Payments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

}
