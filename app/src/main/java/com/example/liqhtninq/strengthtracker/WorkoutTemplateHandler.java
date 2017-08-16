package com.example.liqhtninq.strengthtracker;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kailash Jayaram on 7/3/2017.
 */

/**
 * class WorkoutTemplateHandler
 *      handles file for storing workout templates
 */
public class WorkoutTemplateHandler {

    // constants
    private static final long OFFSET = 3000;
    private static final long START = 4;
    private static final String FILENAME = Environment.getExternalStorageDirectory() +
            File.separator + "StrengthData" + File.separator + "template.datafile";

    /**
     * reads in a workout form file
     * @param name: name of workout to read in
     * @return: workout read in
     */
    public static Workout readWorkoutTemplate(String name){
        File file = new File(FILENAME);
        // erro check
        if(!file.exists())
        {
            return null;
        }
        try {
            RandomAccessFile fio = new RandomAccessFile(file, "rw");

            // go to start and read in number of workouts
            fio.seek(0);
            int numWorkouts = fio.readInt();
            fio.seek(START);

            // search for desired template
            Workout checker = Workout.readFromFile(fio, START);
            long index = 1;
            while(true) {

                if(index > numWorkouts) {
                    // not found
                    fio.close();
                    return null;
                }
                if(checker.getName().equals(name))
                {
                    // found
                    fio.close();
                    return checker;
                }
                else
                {
                    // go to next template
                    long pos = START + (index*OFFSET);

                    fio.seek(pos);

                    checker = Workout.readFromFile(fio, pos);
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
     * returns an array list of all workout templates
     * @return: array list of all workout templates
     */
    public static ArrayList<Workout> readAllWorkoutTemplates(){
        File file = new File(FILENAME);
        // holds templates to be returned
        ArrayList<Workout> list = new ArrayList();

        // error check
        if(!file.exists())
        {
            return null;
        }
        try {
            RandomAccessFile fio = new RandomAccessFile(file, "rw");

            // get the number of templates and go to start
            fio.seek(0);
            int numWorkouts = fio.readInt();
            fio.seek(START);

            // read in each template
            Workout checker = Workout.readFromFile(fio, START);
            long index = 1;
            while(true) {

                if(index > numWorkouts) {
                    //done
                    fio.close();
                    break;
                }
                else
                {
                    // add item to list
                    list.add(checker);

                    //go to next item
                    long pos = START + (index*OFFSET);
                    fio.seek(pos);
                    checker = Workout.readFromFile(fio, pos);

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


    /**
     * writes a template to the file
     * @param template: template to be written
     */
    public static void writeWorkoutTemplate(Workout template) {
        File file = new File(FILENAME);
        // if the file does'nt exist make it
        if(!file.exists())
        {
            try{
                file.createNewFile();
                RandomAccessFile fio = new RandomAccessFile(file, "rw");
                int numWorkouts = 0;
                fio.seek(0);
                fio.writeInt(numWorkouts);
                fio.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // go to beginning
            RandomAccessFile fio = new RandomAccessFile(file, "rw");
            fio.seek(0);

            // update number of workouts in file
            int numWorkouts = fio.readInt();
            long fileLength = START + OFFSET * numWorkouts;
            numWorkouts++;
            fio.seek(0);
            fio.writeInt(numWorkouts);
            // set file length to desired length
            fio.setLength(fileLength);


            // write object to file
            fio.seek(fio.length());
            template.writeToFile(fio, fio.length());
            fio.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * updates an existing template in the file
     * @param template: template to update
     */
    public static void updateWorkoutTemplate(Workout template) {
        File file = new File(FILENAME);
        try {

            RandomAccessFile fio = new RandomAccessFile(file, "rw");

            // read in number of workouts
            fio.seek(0);
            int numWorkouts = fio.readInt();
            fio.seek(START);

            // search for desired template
            Workout checker = Workout.readFromFile(fio, START);
            long index = 1;
            while(true) {

                if(index > numWorkouts) {
                    // not found
                    fio.close();
                    break;
                }
                if(checker.getName().equals(template.getName()))
                {
                    // found
                    long pos = START + (OFFSET * (index-1));

                    // overwrite template
                    fio.seek(pos);
                    template.writeToFile(fio, pos);
                    fio.close();
                }
                else
                {
                    // go to next template
                    long pos = START + (index*OFFSET);
                    fio.seek(pos);
                    checker = Workout.readFromFile(fio, pos);

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
     * returns the number of templates in the file
     * @return: integer holding number of workouts in file
     */
    public static int getNumWorkouts() {
        File file = new File(FILENAME);
        // error check
        if(!file.exists())
        {
            return 0;
        }
        try {
            //read in number of workouts
            RandomAccessFile fio = new RandomAccessFile(file, "rw");
            fio.seek(0);

            return fio.readInt();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // deletes the file
    public static void deleteFile(){
        File file = new File(FILENAME);
        if(file.exists()) file.delete();
    }

    // deletes a template at the given position
    public static void deleteTemplate(int position){
        ArrayList<Workout> list = readAllWorkoutTemplates();

        // remove position and overwrite file
        if(list!=null && position >=0 && position < list.size()) {
            list.remove(position);
            deleteFile();
            for (int index = 0; index < list.size(); index++) {
                writeWorkoutTemplate(list.get(index));
            }
        }
    }

    // deletes a template with the given name
    public static void deleteTemplate(String name){
        ArrayList<Workout> list = readAllWorkoutTemplates();
        if(list!=null) {
            // find correct name and remove it
            for(int index = 0; index< list.size(); index ++) {
                if(list.get(index).getName().equals(name))
                {
                    list.remove(index);
                    break;
                }
            }

            //overwrite file
            deleteFile();
            for (int index = 0; index < list.size(); index++) {
                writeWorkoutTemplate(list.get(index));
            }
        }
    }
}

