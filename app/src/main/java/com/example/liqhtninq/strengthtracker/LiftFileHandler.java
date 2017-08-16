package com.example.liqhtninq.strengthtracker;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 7/6/2017.
 */

/**
 * class LiftFileHandler
 *      handles the file to store temp lift data durring workouts
 */
public class LiftFileHandler {

    // context called from
    Context ctx;

    // constants
    private static final String FILENAME = Environment.getExternalStorageDirectory() +
            File.separator + "StrengthData" + File.separator + "liftFile";
    private static final long OFFSET = 200;
    private static final long START = 4;

    /**
     * writes a lift to the end of the file
     * @param context: context called from
     * @param lift: the lift to be written
     */
    public static void WriteLiftToFile(Context context, Lift lift){
        File file;
        try {
            // if the file doesnt exist make it
            file = new File(FILENAME);
            if(!file.exists())
            {
                try{
                    file.createNewFile();
                    RandomAccessFile fio = new RandomAccessFile(file, "rw");
                    int numLifts = 0;
                    fio.seek(0);
                    fio.writeInt(numLifts);
                    fio.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                // go to the start
                RandomAccessFile fio = new RandomAccessFile(file, "rw");
                fio.seek(0);

                // read in number f lifts
                int numLifts = fio.readInt();
                long fileLength = START + OFFSET * numLifts;
                numLifts++;
                fio.seek(0);
                // update numLifts
                fio.writeInt(numLifts);
                // set file length to proper length
                fio.setLength(fileLength);

                // write lift to file
                fio.seek(fio.length());
                lift.writeToFile(fio, fio.length());
                fio.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {

        }
    }

    /**
     * updates an existing lift in the file
     * @param name: name of lift in file
     * @param context: context called from
     * @param lift: lift to overwirte
     * @return: whether or not lift was found
     */
    public static boolean WriteLiftToFile(String name, Context context, Lift lift){
        File file;
        try {
            file = new File(FILENAME);
            // error check
            if(!file.exists())
            {
               return false;
            }
            try {
                // go to beginning
                RandomAccessFile fio = new RandomAccessFile(file, "rw");

                fio.seek(0);
                int numLifts = fio.readInt();
                fio.seek(START);

                // look for the correct lift
                Lift checker = Lift.readFromFile(fio, START);
                long index = 1;
                while(true) {

                    // not found
                    if(index > numLifts) {
                        fio.close();
                        return false;
                    }
                    if(checker.getName().equals(name))
                    {
                        // overwrite lift
                        fio.seek(START + ((index - 1) * OFFSET));
                        lift.writeToFile(fio, START + ((index - 1) * OFFSET));
                        fio.close();
                        return true;
                    }
                    else
                    {
                        // go to next lift in file
                        long pos = START + (index*OFFSET);

                        fio.seek(pos);

                        checker = Lift.readFromFile(fio, pos);
                        index++;
                    }

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {

        }
        return false;
    }

    /**
     * reads a lift from the file
     * @param name: name of lift to read
     * @param context: context called from
     * @return: lift found
     */
    public static Lift readLift(String name, Context context){
        File file = new File(FILENAME);
        if(!file.exists())
        {
            return null;
        }
        try {
            // go to start
            RandomAccessFile fio = new RandomAccessFile(file, "rw");

            fio.seek(0);
            int numLifts = fio.readInt();
            fio.seek(START);

            // look for correct lift
            Lift checker = Lift.readFromFile(fio, START);
            long index = 1;
            while(true) {

                // not found
                if(index > numLifts) {
                    fio.close();
                    return null;
                }
                if(checker.getName().equals(name))
                {
                    // return lift if found
                    fio.close();
                    return checker;
                }
                else
                {
                    // go to next lift
                    long pos = START + (index*OFFSET);

                    fio.seek(pos);

                    checker = Lift.readFromFile(fio, pos);
                    index++;
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns an array list of all lifts in file
     * @param context: context called from
     * @return: the array list containing the lifts
     */
    public static ArrayList<Lift> readAllLifts(Context context){
        File file = new File(FILENAME);
        // holds lifts to return
        ArrayList<Lift> list = new ArrayList();

        // error check
        if(!file.exists())
        {
            return null;
        }
        try {
            // go to start
            RandomAccessFile fio = new RandomAccessFile(file, "rw");

            fio.seek(0);
            int numLifts = fio.readInt();
            fio.seek(START);

            // read in each lift
            Lift checker = Lift.readFromFile(fio, START);
            long index = 1;
            while(true) {

                if(index > numLifts) {
                    // done
                    fio.close();
                    break;
                }
                else
                {
                    // add lift
                    list.add(checker);
                    long pos = START + (index*OFFSET);

                    fio.seek(pos);

                    // read next lift
                    checker = Lift.readFromFile(fio, pos);
                    index++;
                }

            }
            return list;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // deletes the file (clears list)
    public static void delete(){
        File file = new File(FILENAME);
        if(file.exists()) file.delete();
    }

}
