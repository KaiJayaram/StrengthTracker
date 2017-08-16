package com.example.liqhtninq.strengthtracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Kailash Jayaram on 6/30/2017.
 */

/**
 * class CalanderAdapter
 *
 * sets up the adapter for the calander view
 *
 * mContext: context called from
 * selectedDate: date that was chosen
 * items: the list of dates to display
 * previousView: gives parent view
 *
 */
public class CalanderAdapter extends BaseAdapter {
    private Context mContext;

    // Variabble Declarations
    private java.util.Calendar month;
    public GregorianCalendar pmonth;
    public GregorianCalendar pmonthmaxset;
    private GregorianCalendar selectedDate;
    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int lastWeekDay;
    int leftDays;
    int mnthlength;
    String itemvalue, curentDateString;
    DateFormat df;

    private ArrayList<String> items;
    public static List<String> dayString;
    private View previousView;

    /**
     * constructor initializes the calendarAdapter
     * @param c: context that it is called from
     * @param monthCalendar: gives the calander for the current month
     */
    public CalanderAdapter(Context c, GregorianCalendar monthCalendar) {
        // initialize fields
        CalanderAdapter.dayString = new ArrayList<String>();
        Locale.setDefault( Locale.US );
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        mContext = c;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<String>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    /**
     * sets up the list of calander items
     * @param items: list of calander items
     */
    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }

    // returns the number of days
    public int getCount() {
        return dayString.size();
    }

    // returns the calendar item
    public Object getItem(int position) {
        return dayString.get(position);
    }

    // unused method
    public long getItemId(int position) {
        return 0;
    }

    /**
     * create a new view for each item referenced by the Adapter
     * @param position: the position of this view in the list
     * @param convertView: this items view
     * @param parent: the parent of this items view
     * @return this items view
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // set up variables
        View v = convertView;
        TextView dayView;
        TextView event;
        // if it's not recycled, initialize some attributes
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calander_item, null);

        }

        event = (TextView)v.findViewById(R.id.Event);
        event.setText("");
        dayView = (TextView) v.findViewById(R.id.date);
        // separates daystring into parts.
        String[] separatedTime = dayString.get(position).split("-");
        // taking last part of date. ie; 2 from 2012-12-02
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        // checking whether the day is in current month or not.

        //gets the selected date
        GregorianCalendar thisDay = new GregorianCalendar(Integer.parseInt(separatedTime[0]), Integer.parseInt(separatedTime[1]) -1, Integer.parseInt(separatedTime[2]));
        Date thisDayDate = thisDay.getTime();

        // reads in all workouts
        ArrayList<WorkoutNode> workoutList = WorkoutFileHandler.readAllLifts(mContext);
        // reads in all events
        ArrayList<Event> eventList = EventFileHandler.readAllEventsFromFile();
        // gets the date in an int array for comparison purposes
        int[] time = {Integer.parseInt(separatedTime[0]), Integer.parseInt(separatedTime[1]),Integer.parseInt(separatedTime[2])};
        // gets all workouts on the given date
        ArrayList<WorkoutNode> multiWorkoutList = WorkoutFileHandler.readLifts(time, new PosHolder(0));
        if(workoutList!=null) {
            // check if the date in this view is in the future or the past
            if (thisDayDate.before(new GregorianCalendar().getTime())) {

                // list previous workouts
                if(multiWorkoutList != null) {
                    if (multiWorkoutList.size() == 1) {
                        String text = multiWorkoutList.get(0).getWorkout().getName() + " (completed)";
                        event.setText(text);
                    } else if(multiWorkoutList.size() > 1){
                        String text = "multiple workouts completed";
                        event.setText(text);
                    }
                }
            }
            else {
                // get the recurring schedule
                ArrayList<ScheduleItem> schedule = ScheduleListFileHandler.readAllItemsFromFile();

                //list recurring schedule if it edists
                if(schedule!=null && schedule.size() > 0)
                {
                    int[] date = ScheduleListFileHandler.readStartDate();
                    if(date!=null && date.length == 3) {
                        GregorianCalendar testDate = new GregorianCalendar(date[0], date[1] - 1, date[2]);
                        long diff = thisDay.getTime().getTime()- testDate.getTime().getTime();
                        int days = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        int scheduleDay = (days % schedule.size());
                        if(!schedule.get(scheduleDay).getName().equals("rest day"))
                            event.setText(schedule.get(scheduleDay).getName());

                    }

                }
                // get future singular events and list them if present on given day
                if(eventList!=null) {
                    for (int index = 0; index < eventList.size(); index++) {
                        int[] thisDate = eventList.get(index).getDate();
                        GregorianCalendar testDate = new GregorianCalendar(thisDate[0], thisDate[1] - 1, thisDate[2]);
                        String testerDateFormatted = dayString.get(position);
                        String ThisDayDateFormatted = df.format(testDate.getTime());
                        if (testerDateFormatted.equals(ThisDayDateFormatted)) {
                            event.setText(eventList.get(index).getName());
                        }
                    }
                }
            }
        }


        // set up style of calendar
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            // setting offdays to white color.
            dayView.setTextColor(Color.WHITE);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            dayView.setTextColor(Color.WHITE);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else {
            // setting curent month's days in blue color.
            dayView.setTextColor(Color.BLUE);
        }

        //set up the view
        if (dayString.get(position).equals(curentDateString)) {
            setSelected(v);
            previousView = v;
        } else {
            v.setBackgroundResource(R.drawable.list_item_background);
        }
        dayView.setText(gridvalue);

        // create date string for comparison
        String date = dayString.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        // show icon if date is not empty and it exists in the items array
        ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
        if (date.length() > 0 && items != null && items.contains(date)) {
            iw.setVisibility(View.VISIBLE);
        } else {
            iw.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    /**
     * gets teh view that has been selected and sets it up
     * @param view: selected view
     * @return: selected view
     */
    public View setSelected(View view) {
        if (previousView != null) {
            previousView.setBackgroundResource(R.drawable.list_item_background);
        }
        previousView = view;
        view.setBackgroundResource(R.drawable.calendar_cel_selectl);
        return view;
    }

    /**
     * resets the calendar
     */
    public void refreshDays() {
        // clear items
        items.clear();
        dayString.clear();
        Locale.setDefault( Locale.US );
        pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current month.
        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;
        maxP = getMaxP(); // previous month maximum day 31,30....
        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        /**
         * Calendar instance for getting a complete gridview including the three
         * month's (previous,current,next) dates.
         */
        pmonthmaxset = (GregorianCalendar) pmonth.clone();
        /**
         * setting the start date as previous month's required date.
         */
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        /**
         * filling calendar gridview.
         */
        for (int n = 0; n < mnthlength; n++) {

            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            dayString.add(itemvalue);

        }
    }

    /**
     * gets maximum number of days in previous  month
     * @return maximum number of days in previous  month
     */
    private int getMaxP() {
        int maxP;
        if ((int)month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }

}
