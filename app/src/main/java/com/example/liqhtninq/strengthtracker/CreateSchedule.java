package com.example.liqhtninq.strengthtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Kailash Jayaram on 6/30/2017.
 */

/**
 * class CreateSchedule
 *
 * handles future recurring event scheduling activity
 *
 * names: list of names of workout templates
 * days: list of days to set recurring schedule
 * adapter: the list view adapter
 *
 */
public class CreateSchedule extends AppCompatActivity {

    // variable declaration
    private ArrayList<String> names;
    private ArrayList<String> days;
    ScheduleAdapter adapter;


    /**
     * runs on activity start and initializes list
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        //get available workout templates
        ArrayList<Workout> workoutList = WorkoutTemplateHandler.readAllWorkoutTemplates();

        //initialize lists
        names = new ArrayList<String>();
        days = new ArrayList<String>();

        //add template names to list
        for(int index = 0; index< workoutList.size(); index++){
            names.add(workoutList.get(index).getName());
        }
        // add first blank data
        days.add("");

        // initialize adapter and list
        ListView lv = (ListView)findViewById(R.id.dayList);
        adapter = new ScheduleAdapter(this, R.layout.create_schedule_list_view, days);
        lv.setAdapter(adapter);



    }

    // adds a day to the list
    public void onAddDay(View view){
        days.add("");
        adapter.notifyDataSetChanged();
    }

    // relaunch calanderview
    public void onCancel(View view){
        startActivity(new Intent(this, CalanderView.class));
    }

    // launch template creator
    public void onNewWorkout(View view){
        Intent in = new Intent(this, AddWorkout.class);
        in.putExtra("fromCreateSchedule", true);
        startActivity(in);
    }

    /**
     * saves the schedule
     * @param view: button pressed
     */
    public void onSave(View view){
        // if a schedule already exists
        if(ScheduleListFileHandler.fileExists()) {

            //set up alert dialog to ensure new schedule is wanted
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("A Workout Schedule Already Exists Do You Want to Replace it?")
                    // create new schedule
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ScheduleListFileHandler.deleteFile();
                            for (int index = 0; index < days.size(); index++) {
                                ScheduleItem si = new ScheduleItem(days.get(index), index);
                                ScheduleListFileHandler.writeItemToFile(si);
                            }

                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            String currentDateString = df.format(Calendar.getInstance().getTime());
                            String[] sepTime = currentDateString.split("-");
                            int[] date = {Integer.parseInt(sepTime[0]), Integer.parseInt(sepTime[1]), Integer.parseInt(sepTime[2])};
                            ScheduleListFileHandler.writeStartDate(date[0], date[1], date[2]);
                            startActivity(new Intent(getBaseContext(), CalanderView.class));
                        }
                    })
                    //relaunch calanderView
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getBaseContext(), CalanderView.class));
                        }
                    })
                    .show();
        }
        // if no schedule exists
        else {
            // save the schedule to file
            ScheduleListFileHandler.deleteFile();
            for (int index = 0; index < days.size(); index++) {
                ScheduleItem si = new ScheduleItem(days.get(index), index);
                ScheduleListFileHandler.writeItemToFile(si);

            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String currentDateString = df.format(Calendar.getInstance().getTime());
            String[] sepTime = currentDateString.split("-");
            int[] date = {Integer.parseInt(sepTime[2]), Integer.parseInt(sepTime[1]), Integer.parseInt(sepTime[0])};
            ScheduleListFileHandler.writeStartDate(date[0], date[1], date[2]);

            // launch calander view
            startActivity(new Intent(this, CalanderView.class));
        }

    }
}
