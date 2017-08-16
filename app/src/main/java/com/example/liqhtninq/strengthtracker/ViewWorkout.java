package com.example.liqhtninq.strengthtracker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * Created by Kailash Jayaram on 8/3/2017.
 */

/**
 * class ViewWorkout
 *      handles activity that allows user to view a completed workout
 */
public class ViewWorkout extends AppCompatActivity {
    // list of lifts to display
    private ArrayList<Lift> SectionList = new ArrayList<>();

    // list and list adapter
    private ViewWorkoutAdapter listAdapter;
    private ExpandableListView expandableListView;

    /**
     * initializes the list
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workout);

        // get information from previous activity
        String name = getIntent().getStringExtra("WorkoutName");
        boolean fromEvent = getIntent().getBooleanExtra("fromEvent", false);
        boolean fromSchedule = getIntent().getBooleanExtra("fromSchedule", false);

        // find text view
        TextView etWorkoutName = (TextView)findViewById(R.id.etWorkoutName);

        //figure out which activity this was called from
        if(fromSchedule)
        {
            // read in template and display it
            Workout w = WorkoutTemplateHandler.readWorkoutTemplate(name);
            if (w != null) {
                etWorkoutName.setText(w.getName());
                for (int index = 0; index < w.getNumLifts(); index++) {
                    SectionList.add(w.getLift(index));
                }
            }
        }
        else if(!fromEvent) {
            // read in completed workout and display it
            ArrayList<WorkoutNode> tempList = WorkoutFileHandler.readAllLifts(this);

            if (tempList != null) {
                Workout temp = tempList.get(Integer.parseInt(getIntent().getExtras().get("ListPosition").toString())).getWorkout();
                etWorkoutName.setText(temp.getName());
                for (int index = 0; index < temp.getNumLifts(); index++) {
                    SectionList.add(temp.getLift(index));
                }
            }
        }
        else
        {
            // read in future events
            ArrayList<Event> tempList = EventFileHandler.readAllEventsFromFile();

            //get the correct event and display it
            if(tempList.get(Integer.parseInt(getIntent().getExtras().get("ListPosition").toString())) != null) {
                String eName = tempList.get(Integer.parseInt(getIntent().getExtras().get("ListPosition").toString())).getName();
                Workout w = WorkoutTemplateHandler.readWorkoutTemplate(eName);

                if (w != null) {
                    etWorkoutName.setText(w.getName());
                    for (int index = 0; index < w.getNumLifts(); index++) {
                        SectionList.add(w.getLift(index));
                    }
                }
            }
        }

        //get reference to the ExpandableListView
        expandableListView = (ExpandableListView) findViewById(R.id.workoutView);
        //create the adapter by passing ArrayList data
        listAdapter = new ViewWorkoutAdapter(ViewWorkout.this, SectionList);
        listAdapter.fromEditWorkout();
        //attach the adapter to the list
        expandableListView.setAdapter(listAdapter);


        //expand all Groups
        expandAll();
    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expandableListView.expandGroup(i);
        }
    }

    // finish activity on button press
    public void  Done(View v){
        finish();
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
