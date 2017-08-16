package com.example.liqhtninq.strengthtracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 7/4/2017.
 */
/**
 * Class AddWorkout:
 *     Handles The AddWorkout Activity that Allows the User to create a Workout Template to use
 *     in future workouts.
 *
 *     MAX_NUM: maximum number of sets and lifts a user can add for the sake of file handling
 *     SectionList: This List is the one sent to the list adapter and keeps track of each new lift
 *         and set the user adds.
 *     listAdapter: the adapter for the list view that displays the lifts and sets being inputted
 *         by the user
 *     expandableListView: the list view itself.
 */
public class AddWorkout extends AppCompatActivity {

    //CONSTANTS
    private static final int MAX_NUM = 15;

    //Variable Declarations
    private ArrayList<Lift> SectionList = new ArrayList<>();
    private AddWorkoutAdapter listAdapter;
    private ExpandableListView expandableListView;

    /**
     * Method runs on Activity start. It initializes the class varaibles and sets up the screen
     * the user first sees
     *
     *
     * @param savedInstanceState: object used to overWrite super class method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set up the xml file and run super class method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        verifyStoragePermissions(this);

        // create a blank first entry for the list
        Lift lift = new Lift();
        SectionList.add(lift);

        //get reference to the ExpandableListView
        expandableListView = (ExpandableListView) findViewById(R.id.workoutView);
        //create the adapter by passing the list just created
        listAdapter = new AddWorkoutAdapter(AddWorkout.this, SectionList);
        //attach the adapter to the list
        expandableListView.setAdapter(listAdapter);


        //expand all Groups
        expandAll();

    }

    /**
     * Expands all groups in the list view
     */
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expandableListView.expandGroup(i);
        }
    }

    /**
     * Collapses all groups in the list view
     */
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expandableListView.collapseGroup(i);
        }
    }

    /**
     * Handles the functionality of the add Lift button by adding a new blank lift to the list
     *
     * @param view: the button calling this method
     */
    public void addLift(View view){


        // prohibits adding more lifts than the file handling allows for
        if(SectionList.size() < MAX_NUM) {
            Lift lift = new Lift();
            SectionList.add(lift);
            listAdapter.notifyDataSetChanged();
            expandAll();
        }
        else
        {
            Toast.makeText(this, "Maximum number of lifts is " + MAX_NUM, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Handles the add Set button. Creates a new blank set in the selected lift
     * @param view: the button that was clicked
     */
    public void addSet(View view) {

        // gets the index of the view in the list of the button that was clicked
        String tag = (String)view.getTag();
        int index = Integer.parseInt(tag);

        //adds a new blank set as long as it does not exceed maximum number of sets allowed
        Lift lift = SectionList.get(index);
        if(lift.getNumSets() < MAX_NUM) {
            lift.addSet(lift.getNumSets(), new Set());
            listAdapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(this, "Maximum number of sets is " + MAX_NUM, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * handles cancel button by closing activity
     * @param view: the button that was pressed
     */
    public void Cancel(View view) {
        finish();
    }

    /**
     * Handles the Save button. Saves all of the user inputted data into a new Workout Template
     *
     * @param view: The button that was pressed
     */
    public void Save(View view) {

        // set up the workout that is to be added to the template file
        EditText name = (EditText)findViewById(R.id.etWorkoutName);
        String nameString = name.getText().toString();
        Workout workout = new Workout();
        workout.setName(nameString);

        // add in each lift from the list
        for(int index = 0; index < listAdapter.getGroupCount(); index++)
        {
            workout.addLift(index, (Lift)listAdapter.getGroup(index));
        }

        // save the template to the file
        WorkoutTemplateHandler.writeWorkoutTemplate(workout);


        // check to see if the file was properly created
        File file = new File(Environment.getExternalStorageDirectory() +
                File.separator + "StrengthData" + File.separator + "template.datafile");
        if(file.exists())
        Toast.makeText(getBaseContext(),"saved",
                Toast.LENGTH_LONG).show();

        //launches a different activity for each context this activity is launched from
        boolean fromSelect = getIntent().getBooleanExtra("fromSelectTemplate",false);
        if(fromSelect)
        {
            Intent in = new Intent(this, SelectTemplateForEvent.class);
            in.putExtra("date",getIntent().getIntArrayExtra("date"));
            startActivity(in);
        }
        else if(getIntent().getBooleanExtra("fromCreateSchedule", false))
        {
            startActivity(new Intent(this, CreateSchedule.class));
        }
        else {
            startActivity(new Intent(this, Home.class));
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
     * @param activity: the activity calling the method
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
