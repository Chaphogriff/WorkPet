package com.workthrutheweak.workpet.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.workthrutheweak.workpet.data.Datasource;
import com.workthrutheweak.workpet.model.Task;

//TODO
public class AlarmReceiver extends BroadcastReceiver {
    private String TAG = AlarmReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() called with: context = [$context], intent = [$intent]");
        if (context != null && intent != null && intent.getAction() != null) {
            if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("Test")) {
                if (intent.getExtras() != null) {
                    Task task = Datasource.getTaskById(intent.getExtras().getString("TaskID"));
                    if (task != null) {
                        NotificationHelper.createNotificationForTask(context, task);
                    }
                }
            }
        }
    }
}
