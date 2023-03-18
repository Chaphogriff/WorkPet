package com.workthrutheweak.workpet.model;

public class Task {
    public int titleId;
    public int descriptionId;
    public int dateId;
    public int rewardId;

    public boolean isTaskDone = false;


    public Task(int titleId, int descriptionId, int dateId, int rewardId){
        this.titleId = titleId;
        this.descriptionId = descriptionId;
        this.dateId = dateId;
        this.rewardId = rewardId;
    }
}
