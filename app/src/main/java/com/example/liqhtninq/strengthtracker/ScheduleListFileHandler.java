package com.example.liqhtninq.strengthtracker;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 8/5/2017.
 */

/**
 * class ScheduleListFileHandler
 *      handles the file holding recurring schedule information
 */
public class ScheduleListFileHandler {
    // constants
    private static final String FILENAME = Environment.getExternalStorageDirectory() +
            File.separator + "StrengthData" + File.separator + "scheduleFile";
    private static final long OFFSET = 16;
    private static final long START = 16;

    /**
     * writes an item to the file
     * @param item: item to be written
     */
    public static void writeItemToFile(ScheduleItem item){
        File file = new File(FILENAME);
        // if the file doesnt exist make it
        if(!file.exists())
        {
            try {
                file.createNewFile();
                RandomAccessFile fio = new RandomAccessFile(file, "rw");
                fio.seek(0);
                fio.writeInt(0);
                fio.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        try {
            // set up the number of events in the file
            RandomAccessFile fio = new RandomAccessFile(file, "rw");
            fio.seek(0);
            int numEvents = fio.readInt();
            fio.seek(0);
            fio.writeInt(numEvents+1);

            //set length to proper length
            fio.setLength(START + (OFFSET * numEvents));

            // write item to file
            fio.seek(fio.length());
            item.writeToFile(fio);
            fio.close();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * reads in all items from the file
     * @return: a list of items in the file
     */
    public static ArrayList<ScheduleItem> readAllItemsFromFile(){
        File file = new File(FILENAME);
        // list of items in file
        ArrayList<ScheduleItem> list = new ArrayList<ScheduleItem>();
        // error check
        if(!file.exists())
        {
            return null;
        }
        try {
            // read in number of items in file
            RandomAccessFile fio = new RandomAccessFile(file, "rw");
            fio.seek(0);
            int numItems = fio.readInt();

            // run throough file
            ScheduleItem scheduleItem = ScheduleItem.readFromFile(fio, START);
            list.add(scheduleItem);
            long index = 1;
            while(index < numItems) {

                // go to next item
                long pos = START + (index*OFFSET);
                fio.seek(pos);
                scheduleItem = ScheduleItem.readFromFile(fio, pos);

                // add item
                list.add(scheduleItem);
                index++;
            }
            return list;

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    // check if the file exists
    public static boolean fileExists(){
        File file = new File(FILENAME);
        return file.exists();
    }

    /**
     * writes the first day of the schedule into teh file
     * @param day: day of month
     * @param month: month of year
     * @param year: year
     */
    public static void writeStartDate(int day, int month, int year){
        File file = new File(FILENAME);
        // error check
        if(file.exists()) {
            RandomAccessFile fio = null;
            try {
                // write date to file
                fio = new RandomAccessFile(file, "rw");
                fio.seek(4);
                fio.writeInt(day);
                fio.writeInt(month);
                fio.writeInt(year);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * read the date the schedule starts on from the file
     * @return the date as an int array
     */
    public static int[] readStartDate()
    {
        File file = new File(FILENAME);
        // error check
        if(!file.exists()) return null;

        RandomAccessFile fio = null;
        try {
            // read in date
            fio = new RandomAccessFile(file, "rw");
            fio.seek(4);
            int day = fio.readInt();
            int month = fio.readInt();
            int year = fio.readInt();
            // format
            int[] date = {day, month, year};

            return date;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // deletes the file
    public static void deleteFile(){
        File file = new File(FILENAME);
        if(file.exists()) file.delete();
    }
}
