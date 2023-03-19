package com.workthrutheweak.workpet.model;

//Class for a Task item for recycler view
public class Task {
    //Task Title string ID
    public int titleId;
    //Task Description string ID
    public int descriptionId;
    //Task Date string ID
    public int dateId;
    //Task Date string ID
    public int rewardId;
    //Task boolean to check if Task is done for checkbox
    public boolean isTaskDone = false;

    public Task(int titleId, int descriptionId, int dateId, int rewardId){
        this.titleId = titleId;
        this.descriptionId = descriptionId;
        this.dateId = dateId;
        this.rewardId = rewardId;
    }
}
