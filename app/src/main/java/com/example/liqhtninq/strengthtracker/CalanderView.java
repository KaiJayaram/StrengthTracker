package com.example.liqhtninq.strengthtracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kailash Jayaram on 7/4/2017.
 */
/**
 * class CalanderView
 *     sets up the calendar for the user to view future and previous lifts
 *
 */
public class CalanderView extends Activity {
    private int[] innerTime = new int[3];// holds a time for comparison
    public Calendar month, itemmonth;    // calendar instances.

    public CalanderAdapter adapter;      // adapter instance
    public Handler handler;              // for grabbing some event values for showing the dot
    public ArrayList<String> items;      // container to store calendar items which

    /**
     * runs on creation of the activity, initializes the calander
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        // initialize variables
        month = Calendar.getInstance();
        itemmonth = (Calendar) month.clone();

        items = new ArrayList<String>();
        adapter = new CalanderAdapter(this, (java.util.GregorianCalendar)month);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);

        // set month title
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

        // set up next and previouse month buttons
        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        // when clicking an item
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                //get the selected date
                ((CalanderAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalanderAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalanderAdapter) parent.getAdapter()).setSelected(v);


                // get the date as a GregorianCalendar
                GregorianCalendar thisDay = new GregorianCalendar(Integer.parseInt(separatedTime[0]), Integer.parseInt(separatedTime[1]) -1, Integer.parseInt(separatedTime[2]));
                Date thisDayDate = thisDay.getTime();

                // set up an alert dialogue
                AlertDialog.Builder builder;

                // get the time for comparison purposes
                int[] time = new int[3];
                for(int index = 0; index < time.length; index++) {
                    time[index] = Integer.parseInt(separatedTime[index]);
                }

                // set time for inner methods
                innerTime[0] = time[0];
                innerTime[1] = time[1];
                innerTime[2] = time[2];

                //holds the posiiton in the list
                PosHolder pos = new PosHolder(0);
                //get all workouts on the given date
                final ArrayList<WorkoutNode> workoutList = WorkoutFileHandler.readLifts(time, pos);
                // whether or not there was a lift on that day
                boolean liftFound = false;

                //if lift was found
                if(workoutList != null && workoutList.size()>0) liftFound = true;

                builder = new AlertDialog.Builder(v.getContext());

                // Workout in past present
                if(thisDayDate.before(new GregorianCalendar().getTime())) {
                    if(liftFound) {
                        final PosHolder lp = pos;
                        if (workoutList.size() == 1) {

                            builder.setTitle("View or Edit Workout on " + selectedGridDate)
                                    // edit workout
                                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent in = new Intent(getBaseContext(), EditWorkout.class);
                                            String name = workoutList.get(0).getWorkout().getName();
                                            in.putExtra("WorkoutName", name);
                                            in.putExtra("ListPosition", lp.getPosition());
                                            in.putExtra("fromEvent", false);
                                            getBaseContext().startActivity(in);
                                        }
                                    })
                                    // view workout
                                    .setNegativeButton("View", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent in = new Intent(getBaseContext(), ViewWorkout.class);
                                            String name = workoutList.get(0).getWorkout().getName();
                                            in.putExtra("WorkoutName", name);
                                            in.putExtra("ListPosition", lp.getPosition());
                                            in.putExtra("fromEvent", false);
                                            getBaseContext().startActivity(in);
                                        }
                                    })
                                    .show();
                        }
                        // for multiple previous workouts
                        else if(workoutList.size() > 1)
                        {
                            builder.setTitle("View or Edit Workouts on " + selectedGridDate)
                                    // edit workout
                                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent in = new Intent(getBaseContext(), SelectEvent.class);
                                            in.putExtra("date", innerTime);
                                            in.putExtra("edit", true);
                                            finish();
                                            getBaseContext().startActivity(in);
                                        }
                                    })
                                    // view workout
                                    .setNegativeButton("View", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent in = new Intent(getBaseContext(), SelectEvent.class);
                                            in.putExtra("date", innerTime);
                                            in.putExtra("edit", false);
                                            finish();
                                            getBaseContext().startActivity(in);
                                        }
                                    })
                                    .show();
                        }
                    }
                    }
                    else // future events
                    {
                        final String[] separatedTimes = separatedTime;

                        boolean eventFound = false;
                        int eventPosition = 0;
                        Event e = null;
                        ArrayList<Event> eventList = EventFileHandler.readAllEventsFromFile();
                        if(eventList != null ) {
                            for (int index = 0; index < eventList.size(); index++) {
                                int[] thisDate = eventList.get(index).getDate();
                                GregorianCalendar testDate = new GregorianCalendar(thisDate[0], thisDate[1] - 1, thisDate[2]);
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                String ThisDayDateFormatted = df.format(testDate.getTime());
                                if (selectedGridDate.equals(ThisDayDateFormatted)) {
                                    eventFound = true;
                                    e = eventList.get(index);
                                    eventPosition = index;
                                }

                            }
                        }
                        TextView tv = (TextView)v.findViewById(R.id.Event);
                        //Schedule present and no event present
                        if(!tv.getText().toString().equals("") && !eventFound)
                        {
                            final String name = tv.getText().toString();
                            builder.setTitle("Scheduled Workout for " + selectedGridDate +" is " + name)
                                    .setPositiveButton("View Workout", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent in = new Intent(getBaseContext(), ViewWorkout.class);
                                            in.putExtra("WorkoutName", name);
                                            in.putExtra("fromSchedule", true);
                                            startActivity(in);
                                        }
                                    })
                                    .setNegativeButton("Edit Workout", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            int[] date = new int[3];
                                            date[0] = Integer.parseInt(separatedTimes[0]);
                                            date[1] = Integer.parseInt(separatedTimes[1]);
                                            date[2] = Integer.parseInt(separatedTimes[2]);
                                            Intent in = new Intent(getBaseContext(), SelectTemplateForEvent.class);
                                            in.putExtra("date",date);
                                            getBaseContext().startActivity(in);
                                        }
                                    })
                                    .show();
                        }
                        // Event Present
                        final Event event = e;
                        final int ep = eventPosition;
                        if(eventFound && e!=null) {
                            builder.setTitle("Scheduled Workout for " + selectedGridDate +" is " + e.getName())
                                    .setPositiveButton("View Workout", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent in = new Intent(getBaseContext(), ViewWorkout.class);
                                            String name = event.getName();
                                            in.putExtra("ListPosition", ep);
                                            in.putExtra("WorkoutName", name);
                                            in.putExtra("fromEvent", true);
                                            startActivity(in);
                                        }
                                    })
                                    .setNegativeButton("Edit Workout", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int[] date = event.getDate();
                                            Intent in = new Intent(getBaseContext(), SelectTemplateForEvent.class);
                                            in.putExtra("date",date);
                                            in.putExtra("pos", ep);
                                            getBaseContext().startActivity(in);
                                        }
                                    })
                                    .show();
                        }
                        // No Events Present
                        else{
                            if(tv.getText().toString().equals("")) {
                                builder.setTitle("No events Scheduled for " + selectedGridDate)
                                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("Add Event", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                int[] date = new int[3];
                                                date[0] = Integer.parseInt(separatedTimes[0]);
                                                date[1] = Integer.parseInt(separatedTimes[1]);
                                                date[2] = Integer.parseInt(separatedTimes[2]);
                                                Intent in = new Intent(getBaseContext(), SelectTemplateForEvent.class);
                                                in.putExtra("date", date);
                                                getBaseContext().startActivity(in);
                                            }
                                        })
                                        .show();
                            }
                        }
                    }


            }
        });
    }

    // launch home on cancel
    public void Cancel(View view) {

        startActivity(new Intent(this, Home.class));
    }

    // launch scheduler on schedule button
    public void setFutureSchedule(View view) {
        startActivity(new Intent(this, CreateSchedule.class));
    }

    // resets the schedule and restarts activity
    public void clearSchedule(View view) {
        EventFileHandler.deleteFile();
        ScheduleListFileHandler.deleteFile();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    // change month forward
    protected void setNextMonth() {
        if ((int)month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) + 1),
                    month.getActualMinimum(Calendar.MONTH), 1);
        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
        }

    }

    // change month backwards
    protected void setPreviousMonth() {
        if ((int)month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) - 1),
                    month.getActualMaximum(Calendar.MONTH), 1);
        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
        }

    }

    // reset the calendar
    public void refreshCalendar() {
        TextView title = (TextView) findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    // updates calendar
    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String itemvalue;
            for (int i = 0; i < 7; i++) {
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(Calendar.DATE, 1);
                items.add("2012-09-12");
                items.add("2012-10-07");
                items.add("2012-10-15");
                items.add("2012-10-20");
                items.add("2012-11-30");
                items.add("2012-11-28");
            }

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };
}