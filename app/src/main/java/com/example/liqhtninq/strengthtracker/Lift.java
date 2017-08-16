package com.example.liqhtninq.strengthtracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Kailash Jayaram on 7/3/2017.
 */

/**
 * class Lift
 *      stores lift data
 */
public class Lift {

    // class vars
    private int numSets;
    private Set[] sets;
    private String name;

    // default constructor initializes data to 0
    public Lift() {
        numSets = 1;
        sets = new Set[15];
        name = "";
        for(int index = 0; index < numSets; index++) {
            sets[index] = new Set();
        }
    }

    // overloaded constructor initializes data given
    public Lift(int numSets, String name) {
        this.numSets = numSets;
        this.name = name;
        sets = new Set[15];
        for(int index = 0; index < numSets; index++) {
            sets[index] = new Set();
        }
    }

    // getters
    public Set getSet(int i){
        return sets[i];
    }

    public int getNumSets() {
        return numSets;
    }

    public String getName(){
        return name;
    }

    //setters
    public void addSet(int i, Set in){

        sets[i] = in;
        numSets++;
    }
    public void setSet(int i, Set in){
        sets[i] = in;
    }

    public void setName(String name) {
        this.name = name;
    }

    // file handling

    /**
     * writes object to file
     * @param fio: file accessor
     * @param pos: position in file
     */
    public void writeToFile(RandomAccessFile fio, long pos)
    {
        try {
            // seek to position and write object data
            fio.seek(pos);
            fio.writeUTF(name);
            fio.writeInt(numSets);

            for(int index = 0; index < numSets; index++)
            {
                if(sets[index] != null)
                {
                    sets[index].writeToFile(fio, fio.getFilePointer());
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads an object in from file
     * @param fio: file accessor
     * @param pos: position in file
     * @return Lift that was read in
     */
    public static Lift readFromFile(RandomAccessFile fio, long pos){
        try {
            // seek to position and read in lift to new object
            fio.seek(pos);
            String name = fio.readUTF();
            int numSets = fio.readInt();

            Lift ret = new Lift(numSets, name);
            for(int index = 0; index < numSets; index++)
            {
                ret.setSet(index, Set.readFromFile(fio, fio.getFilePointer()));
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
