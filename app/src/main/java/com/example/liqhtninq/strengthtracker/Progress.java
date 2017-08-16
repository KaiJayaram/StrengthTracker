package com.example.liqhtninq.strengthtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 * Created by Kailash Jayaram on 8/11/2017.
 */

/**
 * class Progress
 *      handles the progress activity that displays graphs
 */
public class Progress extends AppCompatActivity {

    //constant option names
    private static final String OPTION1 = "Total Volume Per Workout";
    private static final String OPTION2 = "Total Volume Per Lift";
    private static final String OPTION3 = "By Highest Estimated One Rep Max Per Lift";

    // constant graph names
    private static final String OPTION1TITLE = " Volume By Day";
    private static final String OPTION2TITLE = " Volume By Day";
    private static final String OPTION3TITLE = " Estimated One Rep Max By Day";

    //class data
    private LineGraphSeries<DataPoint> series;
    private GraphView graph;

    /**
     * initializes graph and spinner
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        // find the graph and spinner
        graph = (GraphView) findViewById(R.id.graph);
        Spinner spin = (Spinner)findViewById(R.id.SelectData);

        // set up the graph a bit
        series = new LineGraphSeries<>();
        series.setDrawDataPoints(true);
        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getBaseContext()));

        // get the graph type selection
        String option = getIntent().getStringExtra("name");

        // for each graph type
        switch(option){
            case OPTION1:

                // put workout names in the spinner
                final ArrayList<String>  workoutNames = getWorkoutNames();
                ArrayAdapter<String> workoutAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item , workoutNames);
                spin.setAdapter(workoutAdapter);

                // on spinner seleciton
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // get name
                        String name = workoutNames.get(position);
                        // holds the list of date data points
                        ArrayList<Date> dates = new ArrayList<Date>();
                        // holds the list of volume data points
                        ArrayList<Integer> volumes = getWorkoutVolumes(name, dates);
                        // make the data point array
                        DataPoint[] points = createDataPointArray(dates, volumes);
                        // make the graph
                        createGraph(volumes, points, dates, name + OPTION1TITLE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                break;
            case OPTION2:

                // put lift names in spinner
                final ArrayList<String>  liftNames = getLiftNames();
                ArrayAdapter<String> liftAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item , liftNames);
                spin.setAdapter(liftAdapter);

                // on spinner selection
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // get lift name selected
                        String name = liftNames.get(position);
                        // holds date data points
                        ArrayList<Date> dates = new ArrayList<Date>();
                        // holds volume data points
                        ArrayList<Integer> volumes = getLiftVolumes(name, dates);
                        // create data point array
                        DataPoint[] points = createDataPointArray(dates, volumes);
                        // create the graph
                        createGraph(volumes, points, dates, name + OPTION2TITLE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case OPTION3:
                //put lift names in spinner
                final ArrayList<String>  liftNames1 = getLiftNames();
                ArrayAdapter<String> liftAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item , liftNames1);
                spin.setAdapter(liftAdapter1);

                // on spinner selection
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // get selected lift name
                        String name = liftNames1.get(position);
                        // holds date data points
                        ArrayList<Date> dates = new ArrayList<Date>();
                        // holds volume data points
                        ArrayList<Integer> volumes = getEstimatedORM(name, dates);
                        // create data point array
                        DataPoint[] points = createDataPointArray(dates, volumes);
                        // create graph
                        createGraph(volumes, points, dates, name + OPTION3TITLE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
        }



    }

    /**
     * initializes the graph
     * @param volumes: volume data points
     * @param points: data point array
     * @param dates: date data points
     * @param Title: name of the grpah
     */
    public void createGraph(ArrayList<Integer> volumes, DataPoint[] points, ArrayList<Date> dates, String Title) {


        //reset graph data
        series.resetData(points);

        // display error text if not enough data found
        TextView errorText = (TextView)findViewById(R.id.ErrorText);
        if(points.length < 2)
        {
            errorText.setVisibility(View.VISIBLE);
            graph.setVisibility(View.INVISIBLE);
        }
        else
        {
            // make graph visible and error text invisible
            errorText.setVisibility(View.INVISIBLE);
            graph.setVisibility(View.VISIBLE);
        }



        // set horizontal data point style
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getGridLabelRenderer().setPadding(100);


        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(dates.get(0).getTime());
        graph.getViewport().setMaxX(dates.get(dates.size() - 1).getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        int min = getMin(volumes);
        int max = getMax(volumes);

        // set manual y bounds for nice steps
        graph.getViewport().setMinY(min - (min / 10));
        if(max-min > 2000 ) {
            // limit max range if range exceeds 2000
            graph.getViewport().setMaxY(min + 2000);
        }
        else{
            graph.getViewport().setMaxY(max + (max/10));
        }

        graph.getViewport().setYAxisBoundsManual(true);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalableY(true);

        // initialize graph for dates and set the title
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.setTitle(Title);
    }

    /**
     * returns an array list of the names of all workouts completed
     * @return: array list of workout names
     */
    public ArrayList<String> getWorkoutNames(){
        // get all workouts
        ArrayList<WorkoutNode> list = WorkoutFileHandler.readAllLifts(this);
        // to be filled with names
        ArrayList<String> names = new ArrayList<>();
        // error check
        if(list == null) return null;
        // fill list with names
        for(int index = 0; index < list.size(); index++){
            String workoutName = list.get(index).getWorkout().getName().toLowerCase();
            if(!names.contains(workoutName))
            {
                names.add(workoutName);
            }
        }
        // sort names alphabetically
        Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    /**
     * returns a list of all lift names in workout history
     * @return: list of lift names
     */
    public ArrayList<String> getLiftNames(){
        // read in all workouts
        ArrayList<WorkoutNode> list = WorkoutFileHandler.readAllLifts(this);
        // holds lift names
        ArrayList<String> names = new ArrayList<>();
        // error check
        if(list == null) return null;
        // go through each workout
        for(int index = 0; index < list.size(); index++){
            // go through each lift
            for(int numLifts =0; numLifts < list.get(index).getWorkout().getNumLifts(); numLifts++)
            {
                // get the lift name and add it if its not already in the list
                Lift temp = list.get(index).getWorkout().getLift(numLifts);
                String liftName = temp.getName().toLowerCase();
                if(!names.contains(liftName)){
                    names.add(liftName);
                }
            }
        }
        // sort names alphabetically
        Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    /**
     * returns an arraylist containg the volume completed in each workout
     * @param workoutName: name of the workouts to read
     * @param dates: dates to be returned
     * @return: list of volumes
     */
    public ArrayList<Integer> getWorkoutVolumes(String workoutName, ArrayList<Date> dates) {
        // read in all workouts
        ArrayList<WorkoutNode> list = WorkoutFileHandler.readAllLifts(this);
        // holds volumes
        ArrayList<Integer> volumes = new ArrayList<>();
        // error check
        if(list == null) return null;
        // go through workouts
        for(int index = 0; index < list.size(); index++)
        {
            // check workout names
            if(list.get(index).getWorkout().getName().toLowerCase().equals(workoutName))
            {
                // get the volume in the workout
                int volume = 0;
                for(int numLifts =0; numLifts < list.get(index).getWorkout().getNumLifts(); numLifts++)
                {
                    Lift temp = list.get(index).getWorkout().getLift(numLifts);

                    for (int numSets = 0; numSets < temp.getNumSets(); numSets++) {
                        volume += (int) (temp.getSet(numSets).getReps() * temp.getSet(numSets).getWeight());
                    }

                }
                //add the volume if the day already has a workout otherwise add a new date and new volume
                int[] dateToAdd = list.get(index).getDate();
                Date date = new GregorianCalendar(dateToAdd[0], dateToAdd[1] - 1, dateToAdd[2]).getTime();
                if(dates.contains(date)){
                    int pos = dates.indexOf(date);
                    volumes.set(pos, volumes.get(pos) + volume);
                }
                else
                {
                    dates.add(date);
                    volumes.add(volume);
                }
            }
        }
        return volumes;
    }

    /**
     * returns the volumes of each lift completed per day
     * @param liftName: name of the lift
     * @param dates: dates of lifts completed
     * @return: list of volumes
     */
    public ArrayList<Integer> getLiftVolumes(String liftName, ArrayList<Date> dates){
        // read in workouts
        ArrayList<WorkoutNode> list = WorkoutFileHandler.readAllLifts(this);
        // holds volumes
        ArrayList<Integer> volumes = new ArrayList<>();
        // error check
        if(list == null) return null;
        // go through workouts
        for(int index = 0; index < list.size(); index++){
            int volume = 0;
            // go through lifts
            for(int numLifts =0; numLifts < list.get(index).getWorkout().getNumLifts(); numLifts++)
            {
                // check if lift name matches
                Lift temp = list.get(index).getWorkout().getLift(numLifts);
                if(temp.getName().toLowerCase().equals(liftName.toLowerCase())) {
                    // sum lift volume
                    for (int numSets = 0; numSets < temp.getNumSets(); numSets++) {
                        volume += (int) (temp.getSet(numSets).getReps() * temp.getSet(numSets).getWeight());
                    }
                }
            }
            // if the lift was found
            if(volume!=0) {

                // add already existing date volumes
                int[] dateToAdd = list.get(index).getDate();
                Date date = new GregorianCalendar(dateToAdd[0], dateToAdd[1] - 1, dateToAdd[2]).getTime();
                if(dates.contains(date)){
                    int pos = dates.indexOf(date);
                    volumes.set(pos, volumes.get(pos) + volume);

                }
                else {
                    // add a new date and volume
                    dates.add(date);
                    volumes.add(volume);
                }
            }
        }
        return volumes;
    }

    /**
     * returns the estimated one rep max for a given lift in an array list corresponding with dates
     * performed
     * @param liftName: name of lift
     * @param dates: list of dates
     * @return: list of estimated ORM's
     */
    public ArrayList<Integer> getEstimatedORM(String liftName, ArrayList<Date> dates){
        // read in workouts
        ArrayList<WorkoutNode> list = WorkoutFileHandler.readAllLifts(this);
        // holds ORMS
        ArrayList<Integer> ORMS = new ArrayList<>();
        // error check
        if(list == null) return null;
        // go through workouts
        for(int index = 0; index < list.size(); index++){
            int orm = 0;
            // go through lifts
            for(int numLifts =0; numLifts < list.get(index).getWorkout().getNumLifts(); numLifts++)
            {
                // check if lift name matches
                Lift temp = list.get(index).getWorkout().getLift(numLifts);
                // get max orm
                if(temp.getName().toLowerCase().equals(liftName.toLowerCase())) {
                    for (int numSets = 0; numSets < temp.getNumSets(); numSets++) {
                        int tempORM = getORM(temp.getSet(numSets).getWeight(), temp.getSet(numSets).getReps());
                        if(tempORM > orm) orm = tempORM;
                    }
                }
            }
            // if lift was found
            if(orm>0) {
                // set new orm if this orm is greater than currently existing orm on the date
                int[] dateToAdd = list.get(index).getDate();
                Date date = new GregorianCalendar(dateToAdd[0], dateToAdd[1] - 1, dateToAdd[2]).getTime();
                if(dates.contains(date)){

                    int pos = dates.indexOf(date);
                    if(orm > ORMS.get(pos)) {
                        ORMS.set(pos, orm);
                    }

                }
                else {
                    // if the date is not already in the list add it and add a new orm
                    dates.add(date);
                    ORMS.add(orm);
                }
            }
        }
        return ORMS;
    }

    /**
     * creates a data point array given the dates and values
     * @param dates: date arraylist
     * @param values: y value arraylist
     * @return: data point array
     */
    public DataPoint[] createDataPointArray(ArrayList<Date> dates, ArrayList<Integer> values){
        // array to be returned
        DataPoint[] points = new DataPoint[dates.size()];
        for(int index = 0; index<dates.size(); index++)
        {
            // create data points
            points[index] = new DataPoint(dates.get(index), (double)(values.get(index)));
        }
        return points;
    }

    /**
     * returns min value in list
     * @param values: list of values
     * @return: min value
     */
    public int getMin(ArrayList<Integer> values){
        int min = values.get(0);
        for(int index = 1; index< values.size(); index++ )
        {
            if(values.get(index) < min) min = values.get(index);
        }
        return min;
    }

    /**
     * returns max value in list
     * @param values: list of values
     * @return: max value
     */
    public int getMax(ArrayList<Integer> values){
        int max = values.get(0);
        for(int index = 1; index< values.size(); index++ )
        {
            if(values.get(index) > max) max = values.get(index);
        }
        return max;
    }

    /**
     * calculates orm given weight and reps performed
     * @param weight: weight lifted
     * @param reps: reps performed
     * @return: estimated ORM
     */
    private int getORM(double weight, int reps){
        if( reps > 12 || reps < 1) return -1;
        else if( reps > 11) return (int)(weight / .7);
        else if ( reps > 10) return (int)(weight / .72);
        else if ( reps > 9) return (int)(weight / .75);
        else if ( reps > 8) return (int)(weight / .76);
        else if ( reps > 7) return (int)(weight / .78);
        else if ( reps > 6) return (int)(weight / .8);
        else if ( reps > 5) return (int)(weight / .83);
        else if ( reps > 4) return (int)(weight / .86);
        else if ( reps > 3) return (int)(weight / .88);
        else if ( reps > 2) return (int)(weight / .9);
        else if ( reps > 1) return (int)(weight / .95);
        else return (int)weight;
    }
}
