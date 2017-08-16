package com.example.liqhtninq.strengthtracker;

/**
 * Created by Kaiilash Jayaram on 8/5/2017.
 */

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * class Register
 *      Handles user registration activity
 */
public class Register extends AppCompatActivity {

    // user information text boxes
    EditText name, surname, email, username, password, passwordCheck;

    /**
     * initializes the text box variables
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        name = (EditText)findViewById(R.id.etWorkoutName);
        surname = (EditText)findViewById(R.id.etsurname);
        email = (EditText)findViewById(R.id.etemail);
        username = (EditText)findViewById(R.id.etusername);
        password = (EditText)findViewById(R.id.etpass);
        passwordCheck = (EditText)findViewById(R.id.etpasscheck);
    }

    /**
     * takes the information out of the edit texts and registers the user in the database
     * @param view: button pressed
     */
    public void OnReg(View view) {


        // getts information
        String str_name = name.getText().toString().trim();
        String str_surname = surname.getText().toString().trim();
        String str_email = email.getText().toString().trim();
        String str_username = username.getText().toString().trim();
        String str_password = Integer.toString(password.getText().toString().trim().hashCode());
        String str_passwordCheck = Integer.toString(passwordCheck.getText().toString().trim().hashCode());

        //check password
        if(str_password.equals(str_passwordCheck))
        {
            // send info to database
            String type = "register";
            BackgroundWorker bw = new BackgroundWorker(this);
            bw.execute(type, str_name,str_surname,str_email,str_username,str_password);
        }
        else
        {
            // display error message
            TextView error = (TextView)findViewById(R.id.ErrorText);
            error.setText("Passwords Don't Match");
            error.setTextColor(Color.rgb(255,0,0));
        }
    }
}

