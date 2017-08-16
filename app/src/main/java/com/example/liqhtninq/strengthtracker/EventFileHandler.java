package com.example.liqhtninq.strengthtracker;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 8/3/2017.
 */

/**
 * class EventFileHandler
 *
 * handles file writing for future event objects
 */

public class EventFileHandler {

    // constants
    private static final String FILENAME = Environment.getExternalStorageDirectory() +
            File.separator + "StrengthData" + File.separator + "eventFile";
    private static final long OFFSET = 24;
    private static final long START = 4;

    /**
     * writes an event to end of file
     * @param e: event to be written
     */
    public static void writeEventToFile(Event e){
        File file = new File(FILENAME);
        if(!file.exists())
        {
            // create the file if it doesnt exist and then write the object
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
            //rewrite the number of events in file
            RandomAccessFile fio = new RandomAccessFile(file, "rw");
            fio.seek(0);
            int numEvents = fio.readInt();
            fio.seek(0);
            fio.writeInt(numEvents+1);
            // properly adjust file length
            fio.setLength(START + (OFFSET * numEvents));

            // write the object to the file
            fio.seek(fio.length());
            e.writeToFile(fio, fio.length());
            fio.close();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * updates an existing event in the file
     * @param e: event to be written to file
     * @return: whether or not it was found
     */
    public static boolean updateEventInFile(Event e){
        File file = new File(FILENAME);
        if(!file.exists())
        {
            // if the file doesnt exist create it and write object to beginning
            try {
                file.createNewFile();
                RandomAccessFile fio = new RandomAccessFile(file, "rw");
                fio.seek(0);
                fio.writeInt(1);
                e.writeToFile(fio, fio.getFilePointer());
                fio.close();
                return true;
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        try {
            // get number of events in file
            RandomAccessFile fio = new RandomAccessFile(file, "rw");
            fio.seek(0);
            int numEvents = fio.readInt();

            // search for inputted event
            Event event = Event.readFromFile(fio, fio.getFilePointer());
            long index = 1;
            while(true) {

                if(index > numEvents) {
                    // not found
                    fio.close();
                    return false;
                }
                if(e.equals(event))
                {
                    // if found over write
                    fio.seek(START + (index - 1) * OFFSET);
                    e.writeToFile(fio, START + (index - 1) * OFFSET);
                    fio.close();
                    return true;
                }
                else
                {
                    // go to next item in file
                    long pos = START + (index*OFFSET);

                    fio.seek(pos);

                    event = Event.readFromFile(fio, pos);
                    index++;
                }

            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    /**
     * reads an object with the given date from the file
     * @param date: date of event
     * @return: Desired event
     */
    public static Event readFromFile(int[] date){
        Event e = new Event("", date);
        File file = new File(FILENAME);
        // error check
        if(!file.exists())
        {
          return null;
        }
        try {
            // start from the beginning
            RandomAccessFile fio = new RandomAccessFile(file, "rw");
            fio.seek(0);
            int numEvents = fio.readInt();

            // search fro date
            Event event = Event.readFromFile(fio, fio.getFilePointer());
            long index = 1;
            while(true) {

                if(index > numEvents) {
                    // not found
                    fio.close();
                    break;
                }
                if(e.equals(event))
                {
                    // if found return item
                    return event;
                }
                else
                {
                    // go to next item
                    long pos = START + (index*OFFSET);

                    fio.seek(pos);

                    event = Event.readFromFile(fio, pos);
                    index++;
                }

            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }
    /**
     * reads all events on a given date from the file
     * @param date: the desired date
     */
    public static ArrayList<Event> readEventsFromFile(int[] date){
        // holds events to be returned
        ArrayList<Event> list = new ArrayList<Event>();
        Event e = new Event("", date);
        File file = new File(FILENAME);

        // error check
        if(!file.exists())
        {
            return null;
        }
        try {
            //start from beginning of file
            RandomAccessFile fio = new RandomAccessFile(file, "rw");
            fio.seek(0);
            int numEvents = fio.readInt();

            // search for date
            Event event = Event.readFromFile(fio, fio.getFilePointer());
            long index = 1;
            while(true) {

                if(index > numEvents) {
                    // no more events to be added
                    fio.close();
                    return list;
                }
                if(e.equals(event))
                {
                    // add event if on correct date
                    list.add(event);
                }
                else
                {
                    // go to next event
                    long pos = START + (index*OFFSET);

                    fio.seek(pos);

                    event = Event.readFromFile(fio, pos);
                    index++;
                }

            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * reads all events into an arraylist
     * @return: list of all events
     */
    public static ArrayList<Event> readAllEventsFromFile(){
        File file = new File(FILENAME);
        // list to be returned
        ArrayList<Event> list = new ArrayList<Event>();

        // error  check
        if(!file.exists())
        {
            return null;
        }
        try {
            // start from beginning
            RandomAccessFile fio = new RandomAccessFile(file, "rw");
            fio.seek(0);
            int numEvents = fio.readInt();

            // add all events to list
            Event event = Event.readFromFile(fio, fio.getFilePointer());
            list.add(event);
            long index = 1;
            while(index < numEvents) {

                // go to next event
                long pos = START + (index*OFFSET);

                fio.seek(pos);

                event = Event.readFromFile(fio, pos);
                list.add(event);
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

    // clears event list
    public static void deleteFile(){
        File file = new File(FILENAME);
        if(file.exists())file.delete();
    }

    /**
     * removes a specific event
     * @param position: position in list
     */
    public static void deleteEvent(int position) {
        // read in all events
        ArrayList<Event> list = readAllEventsFromFile();

        // remove correct event
        if(list!=null && position >=0 && position < list.size()) {
            list.remove(position);

            deleteFile();
            // recreate file
            for (int index = 0; index < list.size(); index++) {
                writeEventToFile(list.get(index));
            }
        }
    }

}

