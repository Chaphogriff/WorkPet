package com.workthrutheweak.workpet.JsonManagement;

import android.util.JsonReader;
import android.util.JsonWriter;

import com.workthrutheweak.workpet.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonManager {

    public JsonManager() {
    }

    static JsonWriter writer;
    static JsonReader reader;

    public static void writeJsonStream(OutputStream out, List<Task> taskList) throws IOException {
        writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeTaskList(writer, taskList);
        writer.close();
    }

    public static void writeTaskList(JsonWriter writer, List<Task> taskList) throws IOException {
        writer.beginArray();
        for (Task task : taskList) {
            writeTask(writer, task);
        }
        writer.endArray();
    }

    public static void writeTask(JsonWriter writer, Task task) throws IOException {
        writer.beginObject();
        writer.name("Title").value(task.get_title());
        writer.name("Description").value(task.get_description());
        writer.name("Date").value(task.get_date());
        writer.name("Reward").value(task.get_reward());
        writer.name("isTaskDone").value(task.isTaskDone());
        writer.endObject();
    }

    public static List<Task> readJsonStream(InputStream in) throws IOException {
        reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readTaskList(reader);
        } finally {
            reader.close();
        }
    }

    public static List<Task> readTaskList(JsonReader reader) throws IOException {
        List<Task> taskList = new ArrayList<Task>();

        reader.beginArray();
        while (reader.hasNext()) {
            taskList.add(readTask(reader));
        }
        reader.endArray();
        return taskList;
    }

    public static Task readTask(JsonReader reader) throws IOException {
        String title = null;
        String description = null;
        String date = null;
        String reward = null;
        Boolean isTaskDone = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("Title")) {
                title = reader.nextString();
            } else if (name.equals("Description")) {
                description = reader.nextString();
            } else if (name.equals("Date")) {
                date = reader.nextString();
            } else if (name.equals("Reward")) {
                reward = reader.nextString();
            } else if (name.equals("isTaskDone")) {
                isTaskDone = reader.nextBoolean();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Task(title, description, date, reward, isTaskDone);
    }
}
