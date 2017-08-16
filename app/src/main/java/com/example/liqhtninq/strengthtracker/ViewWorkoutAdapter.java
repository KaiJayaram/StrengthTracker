package com.example.liqhtninq.strengthtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 8/2/2017.
 */

/**
 * class ViewWorkoutAdapter
 *      handles the list for viewing a completed workout
 */
public class ViewWorkoutAdapter extends BaseExpandableListAdapter {
    // context called from
    private Context context;
    // list of lifts completed
    private ArrayList<Lift> lifts;
    // whether or not this is being run from EditWorkout
    private boolean fromEditWorkout;

    // constructor
    public ViewWorkoutAdapter(Context context, ArrayList<Lift> lifts) {
        this.context = context;
        this.lifts = lifts;
    }

    // set from edit workout to true
    public void fromEditWorkout(){
        fromEditWorkout = true;
    }

    // get the child at the given positions
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return lifts.get(groupPosition).getSet(childPosition);
    }

    // necesary method
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * returns desired child view
     * @param groupPosition: parent position
     * @param childPosition: child position
     * @param isLastChild: if it is the last child
     * @param view: items view
     * @param parent: parent items view
     * @return: items view
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        // get textviews and set
        Set set  = (Set) getChild(groupPosition, childPosition);
        TextView weight;
        TextView reps;

        // inflate view if not already infalted
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.view_workout_child_view, null);
            weight = (TextView)view.findViewById(R.id.etweight);
            reps = (TextView)view.findViewById(R.id.etreps);

            // set tags
            if(weight!=null)
                view.setTag(R.id.weight, weight);
            if(reps!=null)
                view.setTag(R.id.reps, reps);
        }
        else
        {
            // find textviews on layout
            reps = (TextView)view.getTag(R.id.reps);
            weight = (TextView)view.getTag(R.id.weight);
        }

        // put in the set numbers
        TextView setNum = (TextView) view.findViewById(R.id.tvSetnum);
        setNum.setText("Set " + (childPosition +1));


        if(reps!=null) {
            reps.setId(childPosition);
            ((View)reps.getParent()).setId(groupPosition);

            // reset number of reps
            reps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int groupPos = ((View)v.getParent()).getId();
                        int childPos = v.getId();
                        Set s  = (Set) getChild(groupPos, childPos);
                        if (s != null)
                            s.setReps(Integer.parseInt(((TextView) v).getText().toString()));
                    }
                }
            });
        }


        if(weight!=null) {
            weight.setId(childPosition);
            ((View)weight.getParent()).setId(groupPosition);

            // reset weight
            weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int groupPos = ((View)v.getParent()).getId();
                        int childPos = v.getId();
                        Set s  = (Set) getChild(groupPos, childPos);
                        if (s != null) {
                            s.setWeight(Double.parseDouble(((TextView) v).getText().toString()));
                        }
                    }
                }
            });
        }

        // if from editworkout set the textviews
        if(fromEditWorkout){
            weight.setText(String.valueOf(((Set)getChild(groupPosition, childPosition)).getWeight()));
            reps.setText(String.valueOf(((Set)getChild(groupPosition, childPosition)).getReps()));
        }


        return view;
    }

    // returns total number of children
    @Override
    public int getChildrenCount(int groupPosition) {

        Lift lift = lifts.get(groupPosition);
        return lift.getNumSets();

    }

    /// returns group at position
    @Override
    public Object getGroup(int groupPosition) {
        return lifts.get(groupPosition);
    }

    // returns number of parent items in list
    @Override
    public int getGroupCount() {
        return lifts.size();
    }

    // neccesary method
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * returns the desired view
     * @param groupPosition: item position
     * @param isLastChild: whether or not last item in list
     * @param view: items view
     * @param parent: parent items view
     * @return: this items view
     */
    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        // get the lift and edit text
        Lift lift = (Lift) getGroup(groupPosition);
        TextView et;

        // inflate view if not already done
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.view_workout_list_view, null);
            et = (TextView)view.findViewById(R.id.etLift);
            if(et!=null)
                view.setTag(et);
        }
        else{
            // find edit text
            et = (TextView)view.getTag();
        }



        if(et!=null) {
            et.setId(groupPosition);

            // set lift name
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int pos = v.getId();
                        Lift l = (Lift) getGroup(pos);
                        if (l != null)
                            l.setName(((TextView) v).getText().toString());
                    }
                }
            });
        }

        // set edit text if from edit workout
        if(fromEditWorkout) {
            et.setText(lifts.get(groupPosition).getName());
        }

        return view;
    }

    // neccessary method
    @Override
    public boolean hasStableIds() {
        return true;
    }

    // check if the child is selectable
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
