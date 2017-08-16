package com.example.liqhtninq.strengthtracker;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Kailash Jyaram on 8/5/2017.
 */

/**
 * class ScheduleItem
 *      holds a entry in the list of repeated future wrokouts
 */
public class ScheduleItem {
    //class vars
    private String name;
    private int number;

    // default initialization
    public ScheduleItem(){
        name = "";
        number = 0;
    }
    // overloaded constructor
    public ScheduleItem(String name, int number)
    {
        this.name = name;
        this.number = number;
    }

    // getters
    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }


    // setters
    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    // file handling

    /**
     * writes object ot file
     * @param fio: file pointer
     */
    public void writeToFile(RandomAccessFile fio) {
        try {
            fio.writeInt(number);
            fio.writeUTF(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * read the object from file
     * @param fio: file pointer
     * @param pos: position of the object in file
     * @return: the item found
     */
    public static ScheduleItem readFromFile(RandomAccessFile fio, long pos){
        try {
            fio.seek(pos);
            int number = fio.readInt();
            String name = fio.readUTF();
            return new ScheduleItem(name, number);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
