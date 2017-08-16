package com.example.liqhtninq.strengthtracker;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Kailash Jayaram on 7/3/2017.
 */

/**
 * class Workout
 *      stores workout information
 */
public class Workout {
    //class vars
    private int numLifts;
    private String name;
    private Lift[] lifts;

    // default constructor
    public Workout() {
        numLifts = 0;
        name = "";
        lifts = new Lift[15];

    }
    // overlaoded constructor
    public Workout(int numLifts, String name)
    {
        this.numLifts = numLifts;
        this.name = name;
        lifts = new Lift[15];
        for(int index = 0; index< numLifts; index ++)
        {
            lifts[index] = new Lift();
        }

    }

    // getters
    public int getNumLifts() {
        return numLifts;
    }
    public String getName() {
        return name;
    }
    public Lift getLift(int pos){
        return lifts[pos];
    }

    // setters
    public void setNumLifts(int numLifts){ this.numLifts = numLifts;}
    public void setLift(int pos, Lift lift) {
        lifts[pos] = lift;
    }
    public void addLift(int pos, Lift lift) {

        lifts[pos] = lift;
        numLifts++;
    }
    public void setName(String name){
        this.name = name;
    }

    /**
     * writes the lift to a file
     * @param fio: file pointer
     * @param pos: position in file
     */
    public void writeToFile(RandomAccessFile fio,  long pos) {
        try {
            // go to position
            fio.seek(pos);

            // write object
            fio.writeUTF(name);
            fio.writeInt(numLifts);

            for(int index = 0; index < numLifts; index++) {
                if(lifts[index] != null)
                {
                    lifts[index].writeToFile(fio, fio.getFilePointer());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * reads a workout in from the file
     * @param fio: file pointer
     * @param pos: position in file
     * @return
     */
    public static Workout readFromFile(RandomAccessFile fio, long pos) {
        try {
            // go to position
            fio.seek(pos);

            // read in object
            String name = fio.readUTF();
            int numLifts = fio.readInt();

            Workout ret = new Workout(numLifts, name);

            for(int index = 0; index < numLifts; index++) {
                ret.setLift(index, Lift.readFromFile(fio, fio.getFilePointer()));
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}