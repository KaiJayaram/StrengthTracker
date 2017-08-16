package com.example.liqhtninq.strengthtracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 8/1/2017.
 */

/**
 * class EditWorkout
 *      allows the user to edit previous workouts and workout templates
 */

public class EditWorkout extends AppCompatActivity{

    //max number of lifts/sets
    private static final int MAX_NUM = 15;
    // list off lifst to be edited
    private ArrayList<Lift> SectionList = new ArrayList<>();
    // list adapter
    private AddWorkoutAdapter listAdapter;
    // list view
    private ExpandableListView expandableListView;
    // whether updating a template or a workout
    private boolean isTemplate;
    // the node that is being updated if it is a previous workout
    WorkoutNode thisNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);

        verifyStoragePermissions(this);
        isTemplate = false;

        EditText etWorkoutName = (EditText)findViewById(R.id.etWorkoutName);

        // get extra to determine where it was called from
        String name = getIntent().getStringExtra("WorkoutName");
        if(name == null) {
            name = getIntent().getStringExtra("TemplateName");
            isTemplate = true;
        }


        if(isTemplate) {
            // read in the template
            Workout temp = WorkoutTemplateHandler.readWorkoutTemplate(name);
            // initialize the list
            if (temp != null)
            {
                etWorkoutName.setText(temp.getName());
                for (int index = 0; index < temp.getNumLifts(); index++) {
                    SectionList.add(temp.getLift(index));
                }
            }
        }
        else{
            // read in all workouts
            ArrayList<WorkoutNode> tempList = WorkoutFileHandler.readAllLifts(this);
            if(tempList!=null){
                // get the correct workout
                thisNode = tempList.get(Integer.parseInt(getIntent().getExtras().get("ListPosition").toString()));
                Workout temp = thisNode.getWorkout();

                //initialize list
                etWorkoutName.setText(temp.getName());
                for (int index = 0; index < temp.getNumLifts(); index++) {
                    SectionList.add(temp.getLift(index));
                }
            }
        }


        //get reference to the ExpandableListView
        expandableListView = (ExpandableListView) findViewById(R.id.workoutView);
        //create the adapter by passing ArrayList data
        listAdapter = new AddWorkoutAdapter(EditWorkout.this, SectionList);
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

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expandableListView.collapseGroup(i);
        }
    }

    /**
     * adds a lift to the list
     * @param view: button pressed
     */
    public void addLift(View view){

        // check if exceeds max num
        if(SectionList.size() < MAX_NUM) {
            // add a lift
            Lift lift = new Lift();
            SectionList.add(lift);
            listAdapter.notifyDataSetChanged();
            expandAll();
        }
        else
        {
            // error message
            Toast.makeText(this, "Maximum number of lifts is " + MAX_NUM, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * adds a set to the lift
     * @param view: button pressed
     */
    public void addSet(View view) {
        // get the index of the lif to add to
        String tag = (String)view.getTag();
        int index = Integer.parseInt(tag);

        // check if exceeds max number of sets
        Lift lift = SectionList.get(index);
        if(lift.getNumSets() < MAX_NUM) {
            // add set
            lift.addSet(lift.getNumSets(), new Set());
            listAdapter.notifyDataSetChanged();
        }
        else
        {
            // error message
            Toast.makeText(this, "Maximum number of sets is " + MAX_NUM, Toast.LENGTH_LONG).show();
        }
    }

    // go back
    public void Cancel(View view) {
        finish();
    }

    /**
     * saves the lift to either template or workout file
     * @param view: button pressed
     */
    public void Save(View view) {
        // get the name
        EditText name = (EditText)findViewById(R.id.etWorkoutName);
        String nameString = name.getText().toString();
        Workout workout = new Workout();
        workout.setName(nameString);

        // create the workout from the inputted information
        for(int index = 0; index < listAdapter.getGroupCount(); index++)
        {
            workout.addLift(index, (Lift)listAdapter.getGroup(index));
        }
        // save it to the proper file
        if(isTemplate)
        WorkoutTemplateHandler.updateWorkoutTemplate(workout);
        else
        WorkoutFileHandler.updateWorkoutInFile(thisNode);

        //check if file exists
        File file = new File(Environment.getExternalStorageDirectory() +
                File.separator + "StrengthData" + File.separator +"template.datafile");
        if(file.exists())
            Toast.makeText(getBaseContext(),"saved",
                    Toast.LENGTH_LONG).show();

        // launch home
        finish();
        startActivity(new Intent(this, Home.class));

    }

    /**
     * deletes the workout from the correct file
     * @param view: button pressed
     */
    public void Delete(View view){
        // delete form template
        if(isTemplate){
            WorkoutTemplateHandler.deleteTemplate(getIntent().getStringExtra("TemplateName"));
            finish();
            startActivity(new Intent(this, Home.class));
        }
        // delete from workout list
        else{
            WorkoutFileHandler.deleteWorkout(Integer.parseInt(getIntent().getExtras().get("ListPosition").toString()));
            finish();
            startActivity(new Intent(this, CalanderView.class));
        }
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
