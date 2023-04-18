package com.workthrutheweak.workpet.notification;


import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.workthrutheweak.workpet.MainActivity;
import com.workthrutheweak.workpet.R;
import com.workthrutheweak.workpet.model.Task;
//TODO
public class NotificationHelper {
    static public void createNotificationChannel(Context context, int importance, boolean showBadge, String name, String description) {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 2
            String channelId = "${context.packageName}-$name";
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(showBadge);

            // 3
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    static public void createSampleDataNotification(Context context, String title, String message,
                                                    String bigText, Boolean autoCancel) {
        // 1
        String channelId = "${context.packageName}-${context.getString(R.string.app_name)}";
// 2
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo_workpet) // 3
                .setContentTitle(title) // 4
                .setContentText(message) // 5
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText)) // 6
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 7
                .setAutoCancel(autoCancel); // 8
        // 1
        Intent intent;
        intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// 2
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
// 2
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    1001);
            return;
        } else {
            notificationManager.notify(1001, notificationBuilder.build());
        }
    }

    static public void createNotificationForTask(Context context, Task task) {

        // create a group notification
        NotificationCompat.Builder groupBuilder = buildGroupNotification(context, task);

        // create the pet notification
        NotificationCompat.Builder notificationBuilder = buildNotificationForTask(context, task);

        // add an action to the pet notification
        PendingIntent homePendingIntent = createPendingIntentForAction(context, task);
        notificationBuilder.addAction(R.drawable.baseline_home_24, "Home", homePendingIntent);

        // call notify for both the group and the pet notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    1002);
            return;
        }
        notificationManager.notify(1002, groupBuilder.build());
        notificationManager.notify(1003, notificationBuilder.build());
    }

    private static NotificationCompat.Builder buildGroupNotification(Context context, Task task) {
        String channelId = "${context.packageName}-Tasks";
        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo_workpet)
                .setContentTitle("Tasks")
                .setContentText("Notification group for tasks")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Notification group for tasks"))
                .setAutoCancel(true)
                .setGroupSummary(true)
                .setGroup("Tasks");
    }


    private static NotificationCompat.Builder buildNotificationForTask(Context context, Task task){


        String channelId = "${context.packageName}-Tasks";

        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo_workpet)
                .setContentTitle(task.getTitle())
                .setContentText(task.getDescription())
                .setAutoCancel(true);
    }


    private static PendingIntent createPendingIntentForAction(Context context, Task task) {

        Intent homeIntent = new Intent(context, MainActivity.class)
                .setAction("Home")
                .putExtra("notification_id", task.getTaskId())
                .putExtra("TaskID", task.getTaskId());

        return PendingIntent.getBroadcast(context, 12345, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
