package com.workthrutheweak.workpet.data;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.workthrutheweak.workpet.R;
import com.workthrutheweak.workpet.model.Task;

import java.io.Serializable;
import java.util.List;

//Class to get data for recycler view

//Class to delete, unused for now
public class Datasource  {
    public Datasource(){}
    public List<Task> TaskList;

    public static Task getTaskById(String id){
        Task ret = new Task();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docref = db.collection("Users").document(user.getUid());
        DocumentReference taskref = docref.collection("Tasks").document(id);

        //TODO
        return ret;
    }
}
