package com.vance.example.battery;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.vance.example.initializer.WorkManagerInitializer;

import java.util.concurrent.TimeUnit;

public class BatteryActivity extends AppCompatActivity {
    private static final String TAG = "BatteryActivity";
    private PowerConnectionReceiver powerConnectionReceiver;
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkBattery();
        register();
        wakeLock();
        doWork();
    }

    private void wakeLock() {
        // 跨进程获取 PowerManager 服务
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        // 判断是否支持 CPU 唤醒
        boolean isWakeLockLevelSupported = powerManager.
                isWakeLockLevelSupported(PowerManager.PARTIAL_WAKE_LOCK);
        Log.e(TAG, "是否支持wakelock: " + isWakeLockLevelSupported);
        // 支持 CPU 唤醒 , 才保持唤醒
        if (isWakeLockLevelSupported) {
            // 创建只唤醒 CPU 的唤醒锁
            mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "example:WAKE_LOCK");
            // 开始唤醒 CPU
            mWakeLock.acquire();
        }
    }


    private void doWork() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)  //Wi-Fi
                .setRequiresCharging(true) //在设备充电时运行
                .setRequiresBatteryNotLow(true) //电量不足不会运行
                .build();
        OneTimeWorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(UploadWorker.class)
                        .setConstraints(constraints)
                        .build();

        WorkManager
                .getInstance(this)
                .enqueueUniqueWork("upload", ExistingWorkPolicy.KEEP,uploadWorkRequest);
    }

    private void register() {
        IntentFilter ifilter = new IntentFilter();
        //充电状态
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        //电量显著变化
        ifilter.addAction(Intent.ACTION_BATTERY_LOW);
        ifilter.addAction(Intent.ACTION_BATTERY_OKAY);
        powerConnectionReceiver = new PowerConnectionReceiver();
        registerReceiver(powerConnectionReceiver, ifilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerConnectionReceiver);
        if (mWakeLock != null) {
            mWakeLock.release();
        }
    }

    private void checkBattery() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        // 是否正在充电
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // 什么方式充电？
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        //usb
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        //充电器
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        //获得电量
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level * 100 / (float) scale;

        Log.e(TAG, "isCharging: " + isCharging + "  usbCharge: " + usbCharge + "  acCharge:" + acCharge);
        Log.e(TAG, "当前电量: " + batteryPct);
    }
}
