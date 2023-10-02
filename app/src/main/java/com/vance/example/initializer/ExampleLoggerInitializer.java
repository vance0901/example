package com.vance.example.initializer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;

public class ExampleLoggerInitializer implements Initializer<ExampleLogger> {

    @Override
    public ExampleLogger create(Context context) {
        // WorkManager.getInstance() is non-null only after
        // WorkManager is initialized.
        return new ExampleLogger(WorkManager.getInstance(context));
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        List<Class<? extends Initializer<?>>> list = new ArrayList<>();
        list.add(WorkManagerInitializer.class);
        return list;
    }


}
