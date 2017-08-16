package com.example.liqhtninq.strengthtracker;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Kailash Jayaram on 8/4/2017.
 */

/**
 * class UserDataFileHandler
 *      handles the file for storing and reading user data
 */
public class UserDataFileHandler {
    // constants
    private static final File f = new File(Environment.getExternalStorageDirectory() +
            File.separator + "StrengthData" + File.separator + "userData.datafile");

    /**
     * updates the file
     * @param ud: user data object to be updated
     */
    public static void updateFile(UserData ud){
        // if the file doesnt exist create it
        if(!f.exists())
        {
            try{
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // overwrite file
            RandomAccessFile fio = new RandomAccessFile(f, "rw");
            ud.writeToFile(fio);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * read in the object from the file
     * @return: user data object read in from file
     */
    public static UserData readFile(){
        if(!f.exists()) return null;
        try {
            // read in object
            RandomAccessFile fio = new RandomAccessFile(f, "rw");
            UserData ud = UserData.readFromFile(fio);

            return ud;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // check if the file exists
    public static boolean exists(){
        if(f.exists())return true;
        else return false;
    }
}
