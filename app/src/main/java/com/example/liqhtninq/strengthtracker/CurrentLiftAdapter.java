package com.example.liqhtninq.strengthtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 7/6/2017.
 */

/**
 * class CurrentLiftAdapter
 *      handles the adapter for the list view on running workout
 *
 *      list: the list of sets to be displayed
 *      ctx: contex called from
 */

public class CurrentLiftAdapter extends ArrayAdapter<Set>{

    // variable declarations
    private ArrayList<Set> list = new ArrayList<>();
    private Context ctx;

    // constructor initializes fields
    public CurrentLiftAdapter(Context context, int textViewResourceId, ArrayList<Set> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
        ctx = context;
    }

    // returns the number of items in the list
    @Override
    public int getCount() {
        return super.getCount();
    }

    /**
     * sets up the view for each item in the list
     * @param position: position in the list
     * @param convertView: this items view
     * @param parent: parent view
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //declare edittexts
        EditText weight;
        EditText reps;

        //get the selected set
        Set set = list.get(position);

        // set up some variables
        View v = convertView;
        LayoutInflater inflater;

        // if the view has not yet been made make it
        if(convertView == null) {
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.add_lift_child_view, null);
            weight = (EditText)v.findViewById(R.id.etweight);
            reps = (EditText)v.findViewById(R.id.etreps);
            if(weight!=null)
                v.setTag(R.id.weight, weight);
            if(reps!=null)
                v.setTag(R.id.reps, reps);
        }
        else
        {
            //other wise initialize prexisting edit texts
            reps = (EditText)v.getTag(R.id.reps);
            weight = (EditText)v.getTag(R.id.weight);
        }



        // handles weight edit text
        if(weight!=null) {
            weight.setId(position);

            weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int pos = v.getId();
                        Set s  = (Set) list.get(pos);
                        if (s != null) {
                            // save inputted weight
                            s.setWeight(Double.parseDouble(((EditText) v).getText().toString()));
                        }
                    }
                }
            });
        }

        // handles reps edit text
        if(reps!=null) {
            reps.setId(position);

            reps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int pos = v.getId();
                        Set s  = (Set) list.get(pos);
                        if (s != null)
                            // save inputted reps
                            s.setReps(Integer.parseInt(((EditText) v).getText().toString()));
                    }
                }
            });
        }

        // set edit text values
        if(weight!=null)
            weight.setText(String.valueOf(list.get(position).getWeight()));
        if(reps!=null)
            reps.setText(String.valueOf(list.get(position).getReps()));


        return v;

    }
}
