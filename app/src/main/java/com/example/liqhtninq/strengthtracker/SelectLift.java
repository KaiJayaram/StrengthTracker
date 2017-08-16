package com.example.liqhtninq.strengthtracker;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.R.id.list;
/**
 * Created by Kailash Jayaram on 8/3/2017.
 */

/**
 * class SelectLift
 *      handles the acitivty that allows the user to select the lift desired
 */
public class SelectLift extends AppCompatActivity {

    // list of lift names
    private ArrayList<String> names;
    // workout inside of
    private Workout workout;

    /**
     * initializes the list
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lift);

        // get the name of the workout and read it in
        String name =  getIntent().getExtras().getString("name");
        workout = WorkoutTemplateHandler.readWorkoutTemplate(name);

        // get the lift names
        names = new ArrayList<String>();
        for(int index = 0; index < workout.getNumLifts(); index++)
        {
            names.add(workout.getLift(index).getName());
        }

        // intialize the list
        ListView lv = (ListView)findViewById(R.id.LiftSelection);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the liftname selected
                String liftName = names.get(position);

                Intent intent = new Intent(getBaseContext(), CurrentLiftView.class);
                Lift lift = workout.getLift(position);

                //get the number of sets and reps and weight
                int numSets = lift.getNumSets();
                int[] reps = new int[numSets];
                double[] weight = new double[numSets];

                for(int i = 0; i < numSets; i++)
                {
                    reps[i] = lift.getSet(i).getReps();
                    weight[i] = lift.getSet(i).getWeight();
                }

                // send information to next activity and launch it
                intent.putExtra("numSets", numSets);
                intent.putExtra("reps", reps);
                intent.putExtra("weight", weight);
                intent.putExtra("liftName", liftName);

                startActivity(intent);
            }
        }

        );
    }

    /**
     * deletes the lift file and finishes activity
     * @param view: button pressed
     */
    public void Cancel(View view){

        File file = new File(Environment.getExternalStorageDirectory() +
                File.separator + "StrengthData" + File.separator + "liftFile");
        file.delete();

        finish();
    }

    /**
     * saves the workout
     * @param view: button pressed
     */
    public void Save(View view) {
        // read in all lifts in the temp file
        ArrayList<Lift> list = LiftFileHandler.readAllLifts(this);
        if(list != null){
            // add the lifts to the workout
            for(int index = 0; index < list.size(); index++)
            {
                workout.setLift(index, list.get(index));
            }
            workout.setNumLifts(list.size());

            // get the date and format it
            GregorianCalendar date = new GregorianCalendar();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String[] separatedTime = df.format(date.getTime()).split("-");
            int year = Integer.parseInt(separatedTime[0]);
            int month = Integer.parseInt(separatedTime[1]);
            int day = Integer.parseInt(separatedTime[2]);
            long time = date.getTimeInMillis();
            int[] dates = {year, month, day};

            // reset temp file
            LiftFileHandler.delete();

            // write object to file
            WorkoutNode node = new WorkoutNode(workout, dates, time);
            WorkoutFileHandler.WriteLiftToFile(this, node);

            finish();
        }


    }


}
