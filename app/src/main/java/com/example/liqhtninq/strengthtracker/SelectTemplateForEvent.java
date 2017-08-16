package com.example.liqhtninq.strengthtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Created by Kailash Jayaram on 8/3/2017.
 */

/**
 * class SelectTemplateForEvent
 *      handles activity for user selection of future activities
 */
public class SelectTemplateForEvent extends AppCompatActivity {

    // list of template titles
    ArrayList<String> titles;

    /**
     * initializes the list
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_template_for_event);

        // get the workout templates
        ArrayList<Workout> list= WorkoutTemplateHandler.readAllWorkoutTemplates();
        titles = new ArrayList<String>();

        ListView lv = (ListView)findViewById(R.id.WorkoutList);

        // add the template names in
        if(list != null) {
            for (int index = 0; index < list.size(); index++) {
                if(list.get(index).getName()!=null)
                    titles.add(list.get(index).getName());
            }
        }

        // set the list view up
        SelectTemplateAdapter adapter = new SelectTemplateAdapter(this, R.layout.display_lifts_list_view, titles);
        lv.setAdapter(adapter);

    }

    /**
     * updates the file
     * @param name: name of the event
     */
    public void updateEvent(String name){
        // get the date
        int[] date = getIntent().getIntArrayExtra("date");
        // make a new event
        Event e = new Event(name, date);
        // try to update the event in file if it already exists
        boolean test = EventFileHandler.updateEventInFile(e);
        // otherwise write a new event in
        if(!test) EventFileHandler.writeEventToFile(e);
        // relaunch calanderview
        finish();
        startActivity(new Intent(this, CalanderView.class));
    }

    /**
     * create a new workout template
     * @param view: button pressed
     */
    public void addWorkout(View view){
        // launch add workout
        Intent in = new Intent(this, AddWorkout.class);
        in.putExtra("fromSelectTemplate", true);
        in.putExtra("date",getIntent().getIntArrayExtra("date"));
        startActivity(in);
    }

    // go back
    public void cancel(View view) {
        finish();
    }

    /**
     * delete this event
     * @param view: button pressed
     */
    public void deleteEvent(View view) {
        // delete teh desired event
        EventFileHandler.deleteEvent(getIntent().getIntExtra("pos", -1));
        // restart calendar view
        Intent in = new Intent(this, CalanderView.class);
        finish();
        finish();
        startActivity(in);
    }
}
