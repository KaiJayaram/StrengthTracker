package com.example.liqhtninq.strengthtracker;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 7/6/2017.
 */

/**
 * class WorkoutFileHandler
 *      Handles the completed workout stoarage
 */
public class WorkoutFileHandler {

    //context called from
    Context ctx;

    // constants
    private static final String FILENAME = "/sdcard" +
            File.separator + "StrengthData" + File.separator + "workoutFile";
    private static final long OFFSET = 3000;
    private static final long START = 12;

    /**
     * writes a workout to the file
     * @param context: context called from
     * @param node: workout to be written
     */
    public static void WriteLiftToFile(Context context, WorkoutNode node){
        File file;
        try {
            file = new File(FILENAME);
            // if the file doesnt exist creat it
            if(!file.exists())
            {
                try{
                    file.createNewFile();
                    RandomAccessFile fio = new RandomAccessFile(file, "rw");
                    int numWorkouts = 0;
                    long head = START;
                    fio.seek(0);
                    fio.writeInt(numWorkouts);
                    fio.writeLong(head);
                    fio.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                // go to start
                RandomAccessFile fio = new RandomAccessFile(file, "rw");
                fio.seek(0);

                // get the file length and number of lifts
                int numLifts = fio.readInt();
                long fileLength = START + OFFSET * numLifts;
                numLifts++;
                fio.seek(0);
                fio.writeInt(numLifts);

                // set the file length
                fio.setLength(fileLength);


                // write to file
                fio.seek(fio.length());
                node.writeToFile(fio, fio.length());
                fio.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {

        }
    }

    /**
     * updates an existing workout in the file
     * @param node: workout to be overwritten with
     */
    public static void updateWorkoutInFile(WorkoutNode node)
    {
        File file = new File(FILENAME);
        try {

            RandomAccessFile fio = new RandomAccessFile(file, "rw");

            // read in the number of workouts
            fio.seek(0);
            int numWorkouts = fio.readInt();
            fio.seek(START);

            // search for desired node
            WorkoutNode checker = WorkoutNode.readFromFile(fio);
            long index = 1;
            while(true) {

                if(index > numWorkouts) {
                    // node not found
                    fio.close();
                    break;
                }
                boolean datesMatch = true;
                for(int iterator =0; iterator < checker.getDate().length; iterator ++)
                {
                    if(checker.getDate()[iterator] != node.getDate()[iterator]) datesMatch = false;
                }
                if(datesMatch && checker.getWorkout().getName().equals(node.getWorkout().getName()))
                {
                    // node found
                    long pos = START + (OFFSET * (index-1));
                    fio.seek(pos);

                    // overwrite existing node
                    node.writeToFile(fio, pos);
                    fio.close();
                }
                else
                {
                    // go to next node
                    long pos = START + (index*OFFSET);

                    fio.seek(pos);

                    checker = WorkoutNode.readFromFile(fio, pos);
                    index++;
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * reads in all workouts on a given date
     * @param date: date of the workouts to read in
     * @param position: output variable holding position in file
     * @return: list of workouts on given day
     */
    public static ArrayList<WorkoutNode> readLifts(int[] date, PosHolder position){
        File file = new File(FILENAME);
        // initialize position
        position.setPosition(-1);

        // error check
        if(!file.exists())
        {
            return null;
        }

        //read in workouts
        ArrayList<WorkoutNode> list = readAllLifts(null);
        // holds the workouts positions that are not on the correct date
        ArrayList<Integer> removeList = new ArrayList<>();

        // error heck
        if(list!=null)
        for(int index =0; index < list.size(); index ++)
        {
            // check if correcct date
            boolean datesMatch = true;
            for(int iterator =0; iterator < date.length; iterator ++)
            {
                if(date[iterator] != list.get(index).getDate()[iterator]) datesMatch = false;
            }
            if(!datesMatch) {
                // remove items not on the correct date
                removeList.add(index-removeList.size());
            }
            else {
                // set the position if it has not already been set
                if(position.getPosition()==-1)
                    position.setPosition(index);
            }
        }
        // remove unwanted workouts
        if(removeList.size()>0)
        for(int index =0; index < removeList.size(); index++){
            if(list!=null)
            list.remove((int)removeList.get(index));
        }
        return list;

    }

    /**
     * reads in all previous workouts
     * @param context: context called from
     * @return: ArrayList containing all preveious workouts
     */
    public static ArrayList<WorkoutNode> readAllLifts(Context context){
        File file = new File(FILENAME);
        // list to hold workouts
        ArrayList<WorkoutNode> list = new ArrayList<>();

        // error check
        if(!file.exists())
        {
            return null;
        }
        try {

            RandomAccessFile fio = new RandomAccessFile(file, "rw");

            // get number of lifts
            fio.seek(0);
            int numLifts = fio.readInt();
            fio.seek(START);

            // read in each node and add it
            WorkoutNode checker = WorkoutNode.readFromFile(fio,START);
            long index = 1;
            while(true) {

                if(index > numLifts) {
                    // finished
                    fio.close();
                    break;
                }
                else
                {
                    //add to list
                    list.add(checker);

                    // go to next one
                    long pos = START + (index*OFFSET);
                    fio.seek(pos);
                    checker = WorkoutNode.readFromFile(fio,pos);

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

    // deletes the file
    public static void deleteFile(){
        File file = new File(FILENAME);
        if(file.exists())file.delete();
    }

    /**
     * delete a workout at a specific position
     * @param position: position of workout to delete
     */
    public static void deleteWorkout(int position){
        // read in all lifts
        ArrayList<WorkoutNode> list = readAllLifts(null);
        // remove the correct item and rewrite file
        if(list!=null && position >=0 && position < list.size()) {
            list.remove(position);

            deleteFile();
            for (int index = 0; index < list.size(); index++) {
                WriteLiftToFile(null, list.get(index));
            }
        }
    }

}