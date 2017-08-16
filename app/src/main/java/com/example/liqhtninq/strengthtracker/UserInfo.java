package com.example.liqhtninq.strengthtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Kailash Jayaram on 8/3/2017.
 */

/**
 * class UserInfo
 *      handles the activity for viewing user information
 */
public class UserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // raed in user data
        UserData ud = UserDataFileHandler.readFile();
        if(ud == null){
            Toast.makeText(this, "User Data File unavailable", Toast.LENGTH_LONG).show();
            finish();
        }

        // find the text views
        TextView heightFeet = (TextView)findViewById(R.id.etHeightFeet);
        TextView heightInches = (TextView)findViewById(R.id.tvHeightInch);
        TextView weight = (TextView)findViewById(R.id.tvWeight);
        TextView age = (TextView)findViewById(R.id.tvAge);
        TextView name = (TextView)findViewById(R.id.etName);
        TextView bodyFat = (TextView)findViewById(R.id.tvBF);
        TextView gender = (TextView)findViewById(R.id.tvGender);

        // put in data
        heightFeet.setText(String.valueOf((int)(ud.getHeight()/12)));
        heightInches.setText(String.valueOf((int)(ud.getHeight()%12)));
        weight.setText(String.valueOf(ud.getWeight()));
        bodyFat.setText(String.valueOf(ud.getBodyFat()));
        name.setText(ud.getName());
        age.setText(String.valueOf(ud.getAge()));

        // read gender and set the text view
        String male = "Male";
        String female = "Female";
        if(ud.isMale()){
            gender.setText(male);
        }
        else{
            gender.setText(female);
        }


    }

    // finish activity
    public void onBack(View v){
        finish();
    }

    // launch edit activity
    public void onEdit(View v){
        startActivity(new Intent(this, EditUserInfo.class));
    }
}
