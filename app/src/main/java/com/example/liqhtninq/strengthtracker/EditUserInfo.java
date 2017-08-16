package com.example.liqhtninq.strengthtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Kailash Jayaram on 6/30/2017.
 */

/**
 * class EditUserInfo
 *      Handles Activity to Update User Information
 *
 *      name,wiehgt,heightInch,heightFeet,gender,bodyFat,age: EditTexts holding user information
 *      on screen.
 *
 */

public class EditUserInfo extends AppCompatActivity {

    // variable declarations
    EditText name;
    EditText weight;
    EditText heightInch;
    EditText heightFeet;
    Spinner gender;
    EditText bodyFat;
    EditText age;

    /**
     * runs on start and sets the class variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        // disable cancel if this is the first time putting in info
        Button cancelBtn = (Button)findViewById(R.id.cancelBTN);
        if(getIntent().getBooleanExtra("fromHome", false))
        {
            cancelBtn.setEnabled(false);
        }

        //find edit texts in layout
        name = (EditText)findViewById(R.id.etName);
        weight = (EditText)findViewById(R.id.etWeight);
        heightInch = (EditText)findViewById(R.id.etHeightInch);
        heightFeet = (EditText)findViewById(R.id.etHeightFeet);
        gender = (Spinner)findViewById(R.id.spinGender);
        bodyFat = (EditText)findViewById(R.id.etBF);
        age = (EditText)findViewById(R.id.etAge);

        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");

        //set prexisting data if userdata already present
        if(UserDataFileHandler.exists()){
            UserData ud = UserDataFileHandler.readFile();
            if(ud!=null) {
                name.setText(String.valueOf(ud.getName()));
                weight.setText(String.valueOf(ud.getWeight()));
                int hInch = (int)ud.getHeight() % 12;
                int hFeet = (int)ud.getHeight()/12;
                heightInch.setText(String.valueOf(hInch));
                heightFeet.setText(String.valueOf(hFeet));
                if(!ud.isMale()) {
                    genderList.set(0, "Female");
                    genderList.set(1, "Male");
                }
                bodyFat.setText(String.valueOf(ud.getBodyFat()));
                age.setText(String.valueOf(ud.getAge()));

            }
        }

        // set gender spinner
        ArrayAdapter<String> genAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genderList);
        gender.setAdapter(genAdapter);
    }


    /**
     * creates a user data object and saves it to a new file.
     * @param view: button pressed
     */
    public void onSave(View view){
        // new user data object to hold user data
        UserData ud = new UserData();

        // set the user data
        ud.setName(name.getText().toString());
        ud.setWeight(Double.parseDouble(weight.getText().toString()));
        ud.setHeight((Integer.parseInt(heightFeet.getText().toString()) * 12) + Integer.parseInt(heightInch.getText().toString()));
        ud.setHasViewed(true);
        String gen = gender.getSelectedItem().toString();
        if(gen.equals("Male"))
            ud.setMale(true);
        else
            ud.setMale(false);

        ud.setBodyFat(Double.parseDouble(bodyFat.getText().toString()));
        ud.setAge(Integer.parseInt(age.getText().toString()));

        // update user data file
        UserDataFileHandler.updateFile(ud);

        // launch proper activity
        if(getIntent().getBooleanExtra("fromHome", false))
        {
            finish();
            startActivity(new Intent(this, UserInfo.class));
        }
        else {
            finish();
            startActivity(new Intent(this, Home.class));
        }
    }
}
