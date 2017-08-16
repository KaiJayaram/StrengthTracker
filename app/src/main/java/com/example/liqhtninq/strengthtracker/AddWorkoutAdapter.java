package com.example.liqhtninq.strengthtracker;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kailash Jayaram on 7/4/2017.
 */

/**
 * Class AddWorkoutAdapterd
 *     Handles the list used to display user input to the add workout activity
 *
 *     context: the activity class is created from
 *     lifts: the list of lifts used to set up the groups
 *     fromEditWorkout: tells the class if the Edit Workout acitivyt called this
 *
 *
 */
public class AddWorkoutAdapter extends BaseExpandableListAdapter {

    // Variable Declarations
    private Context context;
    private ArrayList<Lift> lifts;
    private boolean fromEditWorkout;

    /**
     * Constructor initializes class variables
     * @param context: the context this class is called from
     * @param lifts: the list to display
     */
    public AddWorkoutAdapter(Context context, ArrayList<Lift> lifts) {
        this.context = context;
        this.lifts = lifts;
        fromEditWorkout = false;
    }

    // sets the from edit workout variable to true
    public void fromEditWorkout(){
        fromEditWorkout = true;
    }

    /**
     * returns the child object at the given positions
     * @param groupPosition: position of parent
     * @param childPosition: position of child
     * @return the child
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lifts.get(groupPosition).getSet(childPosition);
    }

    // returns the position of the child
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Creates the template for displaying child list views
     * @param groupPosition: parent position
     * @param childPosition: child position
     * @param isLastChild: wheter or not it is the last child in teh list
     * @param view: the list item
     * @param parent: parent of the list item
     * @return returns the view of the list item
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        // gets the child from the list
        Set set  = (Set) getChild(groupPosition, childPosition);

        // Declar variables
        EditText weight;
        EditText reps;

        // check if it has already been initialized or not
        if (view == null) {
            //inflate the view and set the tags on the EditTexts
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.add_lift_child_view, null);
            weight = (EditText)view.findViewById(R.id.etweight);
            reps = (EditText)view.findViewById(R.id.etreps);
            if(weight!=null)
                view.setTag(R.id.weight, weight);
            if(reps!=null)
                view.setTag(R.id.reps, reps);
        }
        else
        {
            //set the edit text variables
            reps = (EditText)view.getTag(R.id.reps);
            weight = (EditText)view.getTag(R.id.weight);
        }

        // label the set number
        TextView setNum = (TextView) view.findViewById(R.id.tvSetnum);
        setNum.setText("Set " + (childPosition +1));


        // handles the reps edit text
        if(reps!=null) {
            // set the id on the editText and its parent
            reps.setId(childPosition);
            ((View)reps.getParent()).setId(groupPosition);

            //when the user inputs something to the edit text, updates the list
            reps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int groupPos = ((View)v.getParent()).getId();
                        int childPos = v.getId();
                        Set s  = (Set) getChild(groupPos, childPos);
                        if (s != null)
                            s.setReps(Integer.parseInt(((EditText) v).getText().toString()));
                    }
                }
            });
        }

        // handles the weight edit text
        if(weight!=null) {
            // sets id of edit text and parent
            weight.setId(childPosition);
            ((View)weight.getParent()).setId(groupPosition);

            //when the user inputs something to the edit text, updates the list
            weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int groupPos = ((View)v.getParent()).getId();
                        int childPos = v.getId();
                        Set s  = (Set) getChild(groupPos, childPos);
                        if (s != null) {
                            s.setWeight(Double.parseDouble(((EditText) v).getText().toString()));
                        }
                    }
                }
            });
        }

        // set the Edit Texts to the inputted data
        if(weight!=null) weight.setText(String.valueOf(((Set)getChild(groupPosition, childPosition)).getWeight()));
        if(reps!=null) reps.setText(String.valueOf(((Set)getChild(groupPosition, childPosition)).getReps()));

        return view;
    }

    /**
     * returns the number of children
     * @param groupPosition: position of parent
     * @return the number of children
     */
    @Override
    public int getChildrenCount(int groupPosition) {

        Lift lift = lifts.get(groupPosition);
        return lift.getNumSets();

    }

    // gets position in list of an object
    @Override
    public Object getGroup(int groupPosition) {
        return lifts.get(groupPosition);
    }

    // gets number of lifts
    @Override
    public int getGroupCount() {
        return lifts.size();
    }

    // gets id of a lift in the list
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * sets up the view for the parent list view objects
     * @param groupPosition: position of object
     * @param isLastChild: whether or not it is the last object
     * @param view: the view for this object
     * @param parent: the parent of this objects view
     * @return this object
     */
    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        //get the lift that is being made
        Lift lift = (Lift) getGroup(groupPosition);
        EditText et;

        // check whether or not the view should be initialized
        if (view == null) {
            // initializes the view
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.add_lift_list_view, null);
            et = (EditText)view.findViewById(R.id.etLift);
            if(et!=null)
            view.setTag(et);
        }
        else{
            // finds the already existing exit text
            et = (EditText)view.getTag();
        }

        //finds the button for addSet
        Button Button1= (Button)view.findViewById(R.id.btnaddset);
        Button1.setTag(Integer.toString(groupPosition));


        // sets up the edit text
        if(et!=null) {
            et.setId(groupPosition);

            // if the user inputs data update the lift
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int pos = v.getId();
                        Lift l = (Lift) getGroup(pos);
                        if (l != null)
                            l.setName(((EditText) v).getText().toString());
                    }
                }
            });
        }

        et.setText(lifts.get(groupPosition).getName());

        return view;
    }

    // method necessary for inheritance
    @Override
    public boolean hasStableIds() {
        return true;
    }

    // method necessary for inheritance
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
