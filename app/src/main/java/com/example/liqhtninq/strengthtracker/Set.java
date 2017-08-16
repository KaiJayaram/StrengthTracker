package com.example.liqhtninq.strengthtracker;

import android.icu.util.Output;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Created by Kailash Jayaram on 7/3/2017.
 */

/**
 * class Set
 *      holds reps and weight information for each set
 */
public class Set {

    // class vars
    private int reps;
    private double weight;

    // default constructor
    public Set(){
        reps = 0;
        weight = 0.0;
    }

    // overlaoded constructor
    public Set(int reps, double weight){
        this.reps = reps;
        this.weight = weight;
    }

    // getters
    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    // setters
    public void setWeight(double weight){
        this.weight = weight;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    /**
     * writes object to file
     * @param fio: file pointer
     * @param pos: position in file
     */
    public void writeToFile(RandomAccessFile fio, long pos) {
        try {
            fio.seek(pos);
            fio.writeInt(reps);
            fio.writeDouble(weight);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads object from file
     * @param fio: file pointer
     * @param pos: position in file
     * @return: Set read in from file
     */
    public static Set readFromFile(RandomAccessFile fio, long pos) {
        Set ret = new Set();
        try {
            fio.seek(pos);
            ret.setReps(fio.readInt());
            ret.setWeight(fio.readDouble());

            return ret;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
