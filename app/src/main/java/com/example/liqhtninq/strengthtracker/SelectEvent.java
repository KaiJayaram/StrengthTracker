package com.example.liqhtninq.strengthtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * Created by Kailash Jayaram on 8/3/2017.
 */

/**
 * class SelectEvent
 *      allows the user to choose which event they want to access if
 *      there are multiple events on a single day
 */
public class SelectEvent extends AppCompatActivity {
    // whether class was launched to edit or view events
    boolean edit;
    // list of event names
    ArrayList<String> names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_event);

        //get information from previouse activity
        int[] time = getIntent().getIntArrayExtra("date");
        edit = getIntent().getBooleanExtra("edit", false);

        // get hte position in the list and read in the workouts
        PosHolder pos = new PosHolder(0);
        final ArrayList<WorkoutNode> workoutList = WorkoutFileHandler.readLifts(time, pos);

        // set up for on click method
        final PosHolder innerPos = pos;

        // get workout names
        names = new ArrayList<>();
        if(workoutList != null)
        for(int index = 0; index < workoutList.size(); index++) {
            names.add(workoutList.get(index).getWorkout().getName());
        }

        // initialize list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , names);
        ListView listView = (ListView)findViewById(R.id.EventList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the name of the selected event
                String eventName = names.get(position);
                if(workoutList != null)
                for(int index =0; index < workoutList.size(); index++){
                    if(workoutList.get(index).getWorkout().getName().equals(eventName)){
                        // launche workout editor
                        if(edit) {
                            Intent in = new Intent(getBaseContext(), EditWorkout.class);
                            in.putExtra("WorkoutName", eventName);
                            in.putExtra("ListPosition", index + innerPos.getPosition());
                            in.putExtra("fromEvent", false);
                            getBaseContext().startActivity(in);
                        }
                        // launch workout viewer
                        else{
                            Intent in = new Intent(getBaseContext(), ViewWorkout.class);
                            in.putExtra("WorkoutName", eventName);
                            in.putExtra("ListPosition", index + innerPos.getPosition());
                            in.putExtra("fromEvent", false);
                            getBaseContext().startActivity(in);
                        }
                    }
                }
            }
        });
    }

    // restart calanderview
    public void Cancel( View view) {
        startActivity(new Intent(this, CalanderView.class));
    }
}
