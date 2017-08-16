package com.example.liqhtninq.strengthtracker;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by KailashJayaram on 8/4/2017.
 */

/**
 * class UserData
 *      holds user information
 */
public class UserData {

    // class vars
    private double height;
    private double weight;
    private boolean isMale;
    private double bodyFat;
    private int age;
    private String name;
    private boolean hasViewed;

    // default constructor
    public UserData(){}

    // overloaded constructor
    public UserData(double height, double weight, boolean isMale, double bodyFat, int age, String name) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.isMale = isMale;
        this.bodyFat = bodyFat;
        this.name = name;
        hasViewed = false;
    }


    // getters and setters
    public double getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(double bodyFat) {
        this.bodyFat = bodyFat;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasViewed() {
        return hasViewed;
    }

    public void setHasViewed(boolean hasViewed) {
        this.hasViewed = hasViewed;
    }

    // file handling

    /**
     * writes object to file
     * @param fio: file pointer
     */
    public void writeToFile(RandomAccessFile fio) {
        try {
            fio.seek(0);
            fio.writeBoolean(hasViewed);
            fio.writeUTF(name);
            fio.writeDouble(height);
            fio.writeDouble(weight);
            fio.writeInt(age);
            fio.writeBoolean(isMale);
            fio.writeDouble(bodyFat);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads object from file
     * @param fio: file pointer
     * @return: object read in
     */
    public static UserData readFromFile(RandomAccessFile fio){
        try{
            fio.seek(0);
            UserData ud = new UserData();
            ud.setHasViewed(fio.readBoolean());
            ud.setName(fio.readUTF());
            ud.setHeight(fio.readDouble());
            ud.setWeight(fio.readDouble());
            ud.setAge(fio.readInt());
            ud.setMale(fio.readBoolean());
            ud.setBodyFat(fio.readDouble());
            return ud;

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
