package com.example.liqhtninq.strengthtracker;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Kaiilash Jayaram on 8/5/2017.
 */

/**
 * class ScheduleAdapter
 *      handles the adapter for the future events scheduling list
 */
public class ScheduleAdapter extends ArrayAdapter<String> {
    // class vars
    private ArrayList<String> list = new ArrayList<>();
    private Context ctx;

    // constructor initializes class variables
    public ScheduleAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
        ctx = context;
    }

    // returns number of items in list
    @Override
    public int getCount() {
        return super.getCount();
    }

    /**
     * gives the view for a given item in the list
     * @param position: position of list item
     * @param convertView: item view
     * @param parent: parent of item view
     * @return: item view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the set and layout objects
        Spinner workout;
        TextView number;
        String set = list.get(position);

        View v = convertView;
        LayoutInflater inflater;

        //initialize layout if not yet initialized
        if (convertView == null) {
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.create_schedule_list_view, null);
            number = (TextView) v.findViewById(R.id.dayCount);
            workout = (Spinner) v.findViewById(R.id.dropDown);
            if (number != null)
                v.setTag(R.id.dayCount, number);
            if (workout != null)
                v.setTag(R.id.dropDown, workout);
        } else {
            // fined objects in layout
            workout = (Spinner) v.getTag(R.id.dropDown);
            number = (TextView) v.getTag(R.id.dayCount);
        }

        // set the day number
        if(number!=null) {
            String day = "Day " + (position+1);
            number.setText(day);
        }
        if(workout != null && list.size()-1 <= position) {
            // read in the workouts
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<Workout> workoutList = WorkoutTemplateHandler.readAllWorkoutTemplates();

            // put the workout names inot the names list
            if(workoutList!=null)
            for(int index = 0; index< workoutList.size(); index++){
                names.add(workoutList.get(index).getName());
            }
            // add a rest day into the first position
            names.add(0, "rest day");
            // place the list in the spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            final int p = position;
            // once an item is selected set the workout
            workout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setWorkout(p, parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

            workout.setAdapter(adapter);
        }

        return v;

    }

    // set the name of teh workout at the given position
    public void setWorkout(int position, String name) {
        list.set(position, name);
    }
}
