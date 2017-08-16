package com.example.liqhtninq.strengthtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
/**
 * Created by Kailash Jayaram on 8/11/2017.
 */

/**
 * class ProgressSelector
 *      handles the list selection for choosing graph types to view progress
 */
public class ProgressSelector extends AppCompatActivity {
    // constant options
    private static final String OPTION1 = "Total Volume Per Workout";
    private static final String OPTION2 = "Total Volume Per Lift";
    private static final String OPTION3 = "By Highest Estimated One Rep Max Per Lift";
    // list for view
    private ArrayList<String> options;

    /**
     * intializes list view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_selector);

        // add options to array list
        options = new ArrayList<>();
        options.add(OPTION1);
        options.add(OPTION2);
        options.add(OPTION3);

        // set up list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , options);
        ListView listView = (ListView)findViewById(R.id.progressSelect);
        listView.setAdapter(adapter);

        // on click launch progres graphs with correct option
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventName = options.get(position);
                switch (eventName) {
                    case OPTION1:
                        Intent in = new Intent(getBaseContext(), Progress.class);
                        in.putExtra("name", OPTION1);
                        finish();
                        startActivity(in);
                        break;
                    case OPTION2:
                        Intent in1 = new Intent(getBaseContext(), Progress.class);
                        in1.putExtra("name", OPTION2);
                        finish();
                        startActivity(in1);
                        break;
                    case OPTION3:
                        Intent in2 = new Intent(getBaseContext(), Progress.class);
                        in2.putExtra("name", OPTION3);
                        finish();
                        startActivity(in2);
                        break;
                }
            }
        });
    }
}
