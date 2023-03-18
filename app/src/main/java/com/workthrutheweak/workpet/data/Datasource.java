package com.workthrutheweak.workpet.data;

import com.workthrutheweak.workpet.R;
import com.workthrutheweak.workpet.model.Task;

import java.util.List;

public class Datasource {
    public Datasource(){}
     public List<Task> loadTasks() {
         Task task1 = new Task(R.string.taskTitle1,R.string.taskDesc1,R.string.taskDate1,R.string.taskReward1);
         Task task2 = new Task(R.string.taskTitle2,R.string.empty,R.string.taskDate1,R.string.taskReward2);
         Task task3 = new Task(R.string.taskTitle3,R.string.taskDesc2,R.string.taskDate2,R.string.taskReward1);
         return List.of(task1,task2,task3);
     }
}
