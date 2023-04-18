package com.workthrutheweak.workpet.notification;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.workthrutheweak.workpet.R;
import com.workthrutheweak.workpet.model.Task;

public class ReminderBroadcast extends BroadcastReceiver {
    NotificationCompat.Builder builder;
    ReminderBroadcast(Context context, Task task,PendingIntent pendingIntent){
        builderForTaskReminderNotification(context,task,pendingIntent);
    }

    public void builderForTaskReminderNotification(Context context, Task task, PendingIntent pendingIntent) {
        builder = new NotificationCompat.Builder(context, "TaskChannel")
                .setSmallIcon(R.drawable.logo_workpet)
                .setContentTitle(task.getTitle())
                .setContentText("${task.getTitle()} - ${task.getDescription}")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent);


    }
    public static void createNotificationChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("TaskChannel", "Task", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for Task Reminder");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "TaskChannel")
                .setSmallIcon(R.drawable.logo_workpet)
                .setContentTitle("Task Reminder")
                .setContentText("You have some task to do today")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        //TODO doesnt work
        notificationManager.notify(200, mBuilder.build());
    }
}
