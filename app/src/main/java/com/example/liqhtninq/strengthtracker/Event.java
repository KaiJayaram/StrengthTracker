package com.example.liqhtninq.strengthtracker;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 8/3/2017.
 */

/**
 * class Event
 *      holds a singular future workout event
 */

public class Event {
    // class vars
    private String name;
    private int[] date;

    // constructor initializes class vars
    public Event(String name, int[] date){
        this.name = name;
        this.date = date;
    }

    //getters
    public String getName(){
        return name;
    }

    public int[] getDate(){
        return date;
    }

    // setters
    public void setDate(int[] date){this.date = date;}

    public void setName(String name){
        this.name = name;
    }

    // file handling

    /**
     * writes the object to the given file
     * @param fio: file accessor
     * @param pos: position in file to write to
     */
    public void writeToFile(RandomAccessFile fio, long pos){
        try {
            // seek to position and write info
            fio.seek(pos);
            fio.writeUTF(name);
            fio.writeInt(date[0]);
            fio.writeInt(date[1]);
            fio.writeInt(date[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads object from given file
     * @param fio: file accessor
     * @param pos: position in file
     * @return
     */
    public static Event readFromFile(RandomAccessFile fio, long pos) {
        try {
            // seek to position and read in object from file
            fio.seek(pos);
            String name = fio.readUTF();
            int[] date = new int[3];
            date[0] = fio.readInt();
            date[1] = fio.readInt();
            date[2] = fio.readInt();
            return new Event(name, date);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * comparison method
     * @param e: object to be compared
     * @return: whether or not objects are equal
     */
    public boolean equals(Event e){

        boolean isEqual = true;

        // check if dates are equal
        int[] date2 = e.getDate();
        for(int index = 0; index < date.length; index++){
            if(date[index] != date2[index])
            {
                isEqual = false;
            }
        }
        return isEqual;
    }

}
