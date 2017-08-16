package com.example.liqhtninq.strengthtracker;

/**
 * Created by Kailash Jayaram on 8/11/2017.
 */

/**
 * class PosHolder
 *      holds a position for use in calander events
 */
public class PosHolder {
    // position to be stored
    private int position;
    //constructor
    public PosHolder(int position){
        this.position = position;
    }

    //getter
    public int getPosition() {
        return position;
    }

    //setter
    public void setPosition(int position) {
        this.position = position;
    }
}
