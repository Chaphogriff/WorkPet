package com.workthrutheweak.workpet.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.workthrutheweak.workpet.model.Task;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;
//TODO Doesnt work
public class AlarmScheduler {

    public static void scheduleAlarm(Context context, Task task, PendingIntent alarmIntent, AlarmManager alarmMgr) {

        // Set up the time to schedule the alarm
        Calendar datetimeToAlarm = Calendar.getInstance(Locale.getDefault());
        datetimeToAlarm.setTimeInMillis(System.currentTimeMillis());
        datetimeToAlarm.set(Calendar.HOUR_OF_DAY, task.getHour());
        datetimeToAlarm.set(Calendar.MINUTE, task.getMinute());
        datetimeToAlarm.set(Calendar.SECOND, 0);
        datetimeToAlarm.set(Calendar.MILLISECOND, 0);
        datetimeToAlarm.set(Calendar.DAY_OF_WEEK, task.getDay());
        datetimeToAlarm.set(Calendar.MONTH, task.getMonth());
        datetimeToAlarm.set(Calendar.YEAR, task.getYear());
        long timeDiff = datetimeToAlarm.getTime().getTime() - Calendar.getInstance(Locale.getDefault()).getTime().getTime();
        // Compare the datetimeToAlarm to today
        Calendar today = Calendar.getInstance(Locale.getDefault());
        //if (shouldNotifyToday(task.getDay(), today, datetimeToAlarm)) {

            // schedule for today
        Toast.makeText(context, "Reminder Set!", Toast.LENGTH_SHORT).show();

        alarmMgr.set(AlarmManager.RTC_WAKEUP, timeDiff, alarmIntent);
            //return;
      //  }

/*        // schedule 1 week out from the day
        datetimeToAlarm.roll(Calendar.WEEK_OF_YEAR, 1);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                datetimeToAlarm.getTimeInMillis(),(long) (1000 * 60 * 60 * 24 * 7), alarmIntent);*/
    }

    public static PendingIntent createPendingIntent(Context context, Task task){
        // create the intent using a unique type
        Intent intent = new Intent(context, AlarmReceiver.class)
               // .setAction("Test")
                .setType("${task.getDay()}-${task.getTitle()}-${task.isTaskDone()}-${task.getTaskId()}")
                .putExtra("TaskID", task.getTaskId());

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }
    private static Boolean shouldNotifyToday(int dayOfWeek, Calendar today, Calendar datetimeToAlarm) {
        return dayOfWeek == today.get(Calendar.DAY_OF_WEEK) &&
                today.get(Calendar.HOUR_OF_DAY) <= datetimeToAlarm.get(Calendar.HOUR_OF_DAY) &&
                today.get(Calendar.MINUTE) <= datetimeToAlarm.get(Calendar.MINUTE);
    }
    private void removeAlarmsForReminder( Context context, Task task) {
        Intent intent = new Intent(context, AlarmReceiver.class)
            .setAction("Action")
            .putExtra("TaskID", task.getTaskId());

        String type = String.format(Locale.getDefault(), "%s-%s-%s-%s", task.getDay(), task.getTitle(), task.isTaskDone(), task.getTaskId());

        intent.setType(type);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(alarmIntent);
    }
}



