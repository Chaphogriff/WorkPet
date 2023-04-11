package com.workthrutheweak.workpet.JsonManagement;

import android.os.Build;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

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
        writer.name("Title").value(task.getTitle());
        writer.name("Description").value(task.getDescription());
        writer.name("Year").value(task.getYear());
        writer.name("Month").value(task.getMonth());
        writer.name("Day").value(task.getDay());
        writer.name("Hour").value(task.getHour());
        writer.name("Minute").value(task.getMinute());
        writer.name("Gold").value(task.getGoldreward());
        writer.name("XP").value(task.getXpreward());
        writer.name("isTaskDone").value(task.isTaskDone());
        writer.name("Mode").value(task.getMode());
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
        String mode = null;

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
                month = reader.nextInt();
            } else if (name.equals("Day")) {
                day = reader.nextInt();
            } else if (name.equals("Hour")) {
                hour = reader.nextInt();
            } else if (name.equals("Minute")) {
                minute = reader.nextInt();
            } else if (name.equals("Gold")) {
                gold = reader.nextInt();
            } else if (name.equals("XP")) {
                XP = reader.nextInt();
            } else if (name.equals("isTaskDone")) {
                isTaskDone = reader.nextBoolean();
            } else if (name.equals("Mode")) {
                mode = reader.nextString();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        LocalDate localDate = LocalDate.of(year, month, day);
        LocalTime localTime = LocalTime.of(hour, minute);

        return new Task(title, description, year, month, day, hour, minute, gold, XP, isTaskDone, mode);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<String> readProfileStream(InputStream in) throws IOException {
        reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<String> profileList = null;
        try {

            reader.beginArray();
            while (reader.hasNext()) {
                profileList = readProfileInteger(reader);
            }
            reader.endArray();
        } finally {
            reader.close();
        }
        return profileList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<String> readProfileInteger(JsonReader reader) throws IOException {
        String level = "";
        String exp = "";
        String gold = "";
        String avatar = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("Level")) {
                level = Integer.toString(reader.nextInt());
            } else if (name.equals("Exp")) {
                exp = Integer.toString(reader.nextInt());
            } else if (name.equals("Gold")) {
                gold = Integer.toString(reader.nextInt());
            }else if(name.equals("Avatar")){
                avatar = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        List<String> profileVar = new ArrayList<String>();
        profileVar.add(level);
        profileVar.add(exp);
        profileVar.add(gold);
        profileVar.add(avatar);
        return profileVar;
    }

    public static void writeProfileStream(OutputStream out, List<String> profileList) throws IOException {
        writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writer.beginArray();
        writer.beginObject();
        writer.name("Level").value(profileList.get(0));
        writer.name("Exp").value(profileList.get(1));
        writer.name("Gold").value(profileList.get(2));
        writer.name("Avatar").value(profileList.get(3));
        writer.endObject();
        writer.endArray();
        writer.close();
    }


}

