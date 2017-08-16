package com.example.liqhtninq.strengthtracker;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Kailash Jayaram on 7/7/2017.
 */

/**
 * class WorkoutNode
 *      stores information on completed workouts
 */
public class WorkoutNode {
    // workout completed
    private Workout workout;
    // date completed
    private int[] date = new int[3];
    // time it took to complete ( not yet implemented)
    private long time;

    // overlaoded constructor
    public WorkoutNode(Workout workout, int[] date, long time) {
        this.workout = workout;
        for (int index = 0; index < date.length; index++) {
            this.date[index] = date[index];
        }
        this.time = time;


    }

    // getters
    public long getTime() {
        return time;
    }

    public int[] getDate() {
        return date;
    }

    public Workout getWorkout() {
        return workout;
    }

    /**
     * writes object to file
     * @param fio: file pointer
     * @param pos: position in file
     */
    public void writeToFile(RandomAccessFile fio, long pos) {

        try {
            // go to position
            fio.seek(pos);

            //write object
            for(int index = 0; index< date.length; index++)
            fio.writeInt(date[index]);

            fio.writeLong(time);
            workout.writeToFile(fio,fio.getFilePointer());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * read an object from the file
     * @param fio: file pointer
     * @return: object read in
     */
    public static WorkoutNode readFromFile(RandomAccessFile fio){
        // holds the date
        int[] date = new int[3];

            try {
                //read in object
                for(int index = 0; index < date.length; index++)
                date[index] = fio.readInt();

                long time = fio.readLong();
                Workout workout = Workout.readFromFile(fio, fio.getFilePointer());

                return new WorkoutNode(workout, date, time);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    /**
     * reads in an object from file in specified position
     * @param fio: file pointer
     * @param pos: position in file
     * @return: object read in
     */
    public static WorkoutNode readFromFile(RandomAccessFile fio, long pos){
        // stores date
        int[] date = new int[3];

        try {
            // error check
            if(pos < fio.length()) {
                // go to position
                fio.seek(pos);

                //read in object
                for (int index = 0; index < date.length; index++)
                    date[index] = fio.readInt();

                long time = fio.readLong();
                Workout workout = Workout.readFromFile(fio, fio.getFilePointer());

                return new WorkoutNode(workout, date, time);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
