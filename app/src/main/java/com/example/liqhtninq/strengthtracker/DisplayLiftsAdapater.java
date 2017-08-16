package com.example.liqhtninq.strengthtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 7/4/2017.
 */

/**
 * class DisplayLiftsAdapter
 *      adapter for list that shows the workout templates on homescreen
 *
 *      ctx: context called from
 *      list: the list to be displayed
 */

public class DisplayLiftsAdapater extends ArrayAdapter<String> {

    // Variable declarations
    ArrayList<String> list = new ArrayList<>();
    Context ctx;

    // initializes classs vars in constructor
    public DisplayLiftsAdapater(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        list = objects;
        ctx = context;
    }

    // returns the number of objects in list
    @Override
    public int getCount() {
        return super.getCount();
    }

    /**
     * sets up the view for each list item
     * @param position: position in list
     * @param convertView: this items view
     * @param parent: parent view
     * @return: this items view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // set up some vars and inflate the view
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.display_lifts_list_view, null);

        // set the text
        TextView tv = (TextView) v.findViewById(R.id.tvWorkoutName);
        tv.setText(list.get(position));

        //tag the buttons
        Button btnStart = (Button)v.findViewById(R.id.btnStart);
        btnStart.setTag(Integer.toString(position));
        Button btnEdit = (Button)v.findViewById(R.id.btnEdit);
        btnEdit.setTag(Integer.toString(position));

        return v;

    }


}
