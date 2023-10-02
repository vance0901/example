package com.vance.example;

import android.app.Application;
import android.os.Debug;
import android.os.StrictMode;
import android.util.Log;

public class MyApplication extends Application {

    public MyApplication() {
        try {
            Debug.startMethodTracing("vance");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("vance","请读者自行放入文件");
        }
    }


    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            //线程检测策略
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()   //读、写操作
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()   //Sqlite对象泄露
                    .detectLeakedClosableObjects()  //未关闭的Closable对象泄露
                    .penaltyLog()  //违规打印日志
                    .penaltyDeath() //违规崩溃
                    .build());
        }

        super.onCreate();
//        AppInitializer.getInstance(this)
//                .initializeComponent(ExampleLoggerInitializer.class);
//        BlockCanary.install();
//        ChoreographerHelper.start();
    }
}
