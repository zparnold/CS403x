package com.starboardland.pedometer;

import android.app.Activity;
import android.widget.TextView;

import java.util.TimerTask;

/**
 * Created by Zach on 4/4/2015.
 */
public class MinuteCountIncrementTask extends TimerTask {
    private CounterActivity activity;

    public  MinuteCountIncrementTask(CounterActivity A){
        this.activity = A;
    }
    /**
     *
     */
    @Override
    public void run() {
        int count = this.activity.getMinuteCount();
        count++;

        //activity.setMinCount(tvMinCount);
        this.activity.setMinuteCount(count);
    }
}
