package com.workthrutheweak.workpet.JsonManagement;

import android.os.Build;
import android.util.JsonReader;
import android.util.JsonWriter;

import androidx.annotation.RequiresApi;

import com.workthrutheweak.workpet.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JsonManager {

    public JsonManager() {
    }

    static JsonWriter writer;
    static JsonReader reader;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void writeJsonStream(OutputStream out, List<Task> taskList) throws IOException {
        writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeTaskList(writer, taskList);
        writer.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void writeTaskList(JsonWriter writer, List<Task> taskList) throws IOException {
        writer.beginArray();
        for (Task task : taskList) {
            writeTask(writer, task);
        }
        writer.endArray();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void writeTask(JsonWriter writer, Task task) throws IOException {
        writer.beginObject();
        writer.name("Title").value(task.get_title());
        writer.name("Description").value(task.get_description());
        writer.name("Year").value(task.getLocalDate().getYear());
        writer.name("Month").value(task.getLocalDate().getMonthValue());
        writer.name("Day").value(task.getLocalDate().getDayOfMonth());
        writer.name("Hour").value(task.getLocalTime().getHour());
        writer.name("Minute").value(task.getLocalTime().getMinute());
        writer.name("Gold").value(task.getGoldreward());
        writer.name("XP").value(task.getXpreward());
        writer.name("isTaskDone").value(task.isTaskDone());
        writer.endObject();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Task> readJsonStream(InputStream in) throws IOException {
        reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readTaskList(reader);
        } finally {
            reader.close();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Task> readTaskList(JsonReader reader) throws IOException {
        List<Task> taskList = new ArrayList<Task>();

        reader.beginArray();
        while (reader.hasNext()) {
            taskList.add(readTask(reader));
        }
        reader.endArray();
        return taskList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Task readTask(JsonReader reader) throws IOException {
        String title = null;
        String description = null;
        int year = 1;
        int month = 1;
        int day = 1;
        int hour = 0;
        int minute = 0;
        int gold = 0;
        int XP = 0;
        Boolean isTaskDone = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("Title")) {
                title = reader.nextString();
            } else if (name.equals("Description")) {
                description = reader.nextString();
            } else if (name.equals("Year")) {
                year = reader.nextInt();
            } else if (name.equals("Month")) {
                year = reader.nextInt();
            } else if (name.equals("Day")) {
                year = reader.nextInt();
            } else if (name.equals("Hour")) {
                year = reader.nextInt();
            } else if (name.equals("Minute")) {
                year = reader.nextInt();
            } else if (name.equals("Gold")) {
                gold = reader.nextInt();
            } else if (name.equals("XP")) {
                XP = reader.nextInt();
            } else if (name.equals("IsTaskDone")) {
                isTaskDone = reader.nextBoolean();
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        LocalDate localDate = LocalDate.of(year,month,day);
        LocalTime localTime = LocalTime.of(hour,minute);

        return new Task(title, description, localDate, localTime, gold, XP, isTaskDone);
    }
}
