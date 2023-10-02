package com.vance.example.jank.choreographer;

import android.os.Build;
import android.view.Choreographer;

import java.util.concurrent.TimeUnit;

public class ChoreographerHelper {
    public static void start() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
                long lastFrameTimeNanos = 0;

                @Override
                public void doFrame(long frameTimeNanos) {
                    //上次回调时间
                    if (lastFrameTimeNanos == 0) {
                        lastFrameTimeNanos = frameTimeNanos;
                        Choreographer.getInstance().postFrameCallback(this);
                        return;
                    }
                    long diff = (frameTimeNanos - lastFrameTimeNanos) / 1_000_000;
                    if (diff > 16.6f) {
                        //掉帧数
                        int droppedCount = (int) (diff / 16.6);
                    }
                    lastFrameTimeNanos = frameTimeNanos;
                    Choreographer.getInstance().postFrameCallback(this);
                }
            });
        }
    }
}
