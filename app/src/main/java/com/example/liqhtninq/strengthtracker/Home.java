package com.example.liqhtninq.strengthtracker;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import static android.R.id.list;
/**
 * Created by Kailash Jayaram on 7/3/2017.
 */

/**
 * class Home
 *      handles the home screen of the app. Runs buttons and workout list
 */
public class Home extends AppCompatActivity {

    // list of template names
    ArrayList<String> titles;
    @Override

    /**
     * runs on start and initialzes buttons and template list
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // initialize data folder if non existant
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "StrengthData");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // read in user data if not present require it to be inputted
        UserData ud = UserDataFileHandler.readFile();
        if(ud == null || !ud.isHasViewed())
        {
            startActivity(new Intent(this, EditUserInfo.class));
        }

        // read in all templates
        ArrayList<Workout> list= WorkoutTemplateHandler.readAllWorkoutTemplates();
        titles = new ArrayList<String>();

        // put template names into list
        ListView lv = (ListView)findViewById(R.id.WorkoutList);
        if(list != null) {
            for (int index = 0; index < list.size(); index++) {
                if(list.get(index).getName()!=null)
                    titles.add(list.get(index).getName());
            }
        }

        // create list view
        DisplayLiftsAdapater adapter = new DisplayLiftsAdapater(this, R.layout.display_lifts_list_view, titles);
        lv.setAdapter(adapter);

    }

    // opens calendar activity
    public void openCalender(View view){
        startActivity(new Intent(this, CalanderView.class));
    }

    // opens progress activity
    public void openProgress(View view){
        startActivity(new Intent(this, ProgressSelector.class));
    }

    // opens user data activity
    public void openInfo(View view) {
        startActivity( new Intent(this, UserInfo.class));
    }

    // opens add workout activity
    public void addWorkout(View view){
        startActivity(new Intent(this, AddWorkout.class));
    }

    /**
     * initializes workout to be run
     * @param view: button pressed
     */
    public void OnStart(View view)
    {
        // get the name of the template
        int position = Integer.parseInt((String)view.getTag());
        String name = titles.get(position);

        // start lift with correct template
        Intent in = new Intent(this, SelectLift.class);
        in.putExtra("name", name);
        LiftFileHandler.delete();
        startActivity(in);
    }

    /**
     * allows user to edit templates
     * @param view: button pressed
     */
    public void OnEdit(View view) {
        //get template name
        int position = Integer.parseInt((String)view.getTag());
        String name = titles.get(position);
        // launch editor
        Intent in = new Intent(this, EditWorkout.class);
        in.putExtra("TemplateName", name);
        startActivity(in);
    }
}
