package com.example.liqhtninq.strengthtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 8/3/2017.
 */

/**
 * class SelectTemplateAdapter
 *      handles list for selecting a future workout template
 */
public class SelectTemplateAdapter extends ArrayAdapter<String> {
    // class vars
    ArrayList<String> list = new ArrayList<>();
    Context ctx;

    // constructor initializes class vars
    public SelectTemplateAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
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
     * gets the view for the desired item in the list
     * @param position: position of item in list
     * @param convertView: items view
     * @param parent: items parents view
     * @return: items view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //inflate the view
        final int p = position;
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.select_template_list_view, null);

        // get the text view and set a listener
        TextView tv = (TextView) v.findViewById(R.id.templateName);
        tv.setText(list.get(position));
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update the list
                ((SelectTemplateForEvent)getContext()).updateEvent(list.get(p));
            }
        });
        return v;

    }

}
