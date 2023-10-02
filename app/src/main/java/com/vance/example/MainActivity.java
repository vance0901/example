package com.vance.example;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;

import com.vance.example.battery.BatteryActivity;
import com.vance.example.jank.LayoutActivity;
import com.vance.example.jank.SystraceActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
//        TraceCompat.beginSection("vance_setContent");
        //.....
        Handler handler = new Handler();
        new AsyncLayoutInflater(this).inflate(R.layout.activity_main, null,
                new AsyncLayoutInflater.OnInflateFinishedListener() {
                    @Override
                    public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                        setContentView(view);
                    }
                });

//        TraceCompat.endSection();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Debug.stopMethodTracing();
    }

    public void testSystrace(View view) {
        startActivity(new Intent(this, SystraceActivity.class));
    }

    public void testLayout(View view) {
        startActivity(new Intent(this, LayoutActivity.class));
    }

    public void testBattery(View view) {
        startActivity(new Intent(this, BatteryActivity.class));
    }
}
