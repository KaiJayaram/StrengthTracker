package com.example.liqhtninq.strengthtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * Created by Kailash Jayaram on 6/30/2017.
 */

/**
 * class CurrentLiftView
 *
 * handles activity that runs each lift during a workout
 *      list: list of sets to be displayed
 *      lifT: the lift that is being displayed
 */
public class CurrentLiftView extends AppCompatActivity {

    // variable declarations
    private ArrayList<Set> list = new ArrayList<Set>();
    private Lift lift;

    /**
     * runs on start and initializes the list
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_lift_view);

        // get necessary llift information
        String name = getIntent().getStringExtra("liftName");
        int[] reps = getIntent().getIntArrayExtra("reps");
        double[] weight = getIntent().getDoubleArrayExtra("weight");
        int numSets = getIntent().getIntExtra("numSets" , 0);

        // read in a lift if it already exists in the temp file
        Lift l = LiftFileHandler.readLift(name, this);
        if(l != null)
        {
            lift = l;
            for(int index = 0; index< lift.getNumSets(); index++)
            {
                list.add(lift.getSet(index));
            }
        }
        else
        {
            // otherwise initialize list based on inputted values from previouse activity

            lift = new Lift(numSets, name);

            for(int i = 0; i < numSets; i++){
                Set set = new Set();
                set.setReps(reps[i]);
                set.setWeight(weight[i]);
                list.add(set);
                lift.setSet(i, set);
            }
        }

        // initialize title
        TextView tv = (TextView)findViewById(R.id.tvLiftName);
        tv.setText(name);

        // set up list view
        ListView lv = (ListView)findViewById(R.id.SetsList);
        CurrentLiftAdapter listAdapter = new CurrentLiftAdapter(this,
                R.layout.add_lift_child_view, list);
        lv.setAdapter(listAdapter);
    }

    // saves data to temp file and finishes activity
    public void NextLift(View view) {
        if(!LiftFileHandler.WriteLiftToFile(lift.getName(), this, lift)){
            LiftFileHandler.WriteLiftToFile(this,lift);
        }
        Lift l = LiftFileHandler.readLift(lift.getName(),this);

        finish();

    }

    //finishes without saving
    public void Cancel(View view) {
        finish();
    }
}
