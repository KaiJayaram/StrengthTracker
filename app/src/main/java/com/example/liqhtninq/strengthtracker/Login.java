package com.example.liqhtninq.strengthtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Created by Kailash Jayaram on 8/11/2017.
 */
/**
 * class Login
 *      handles login activity
 */
public class Login extends AppCompatActivity {

    // user data
    private EditText username;
    private EditText pass;
    // buttons
    private Button login;
    private Button register;

    /**
     * runs on start and initializes class data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // find layout items
        username = (EditText)findViewById(R.id.etuser);
        pass = (EditText)findViewById(R.id.etpass);
        login = (Button)findViewById(R.id.btnlogin);
        register = (Button)findViewById(R.id.btnregister);
    }

    /**
     * runs on user login and checks if valid
     * @param view: button pressesd
     */
    public void onLogin(View view){
        String user = username.getText().toString().trim();
        String password = Integer.toString(pass.getText().toString().trim().hashCode());
        String type = "login";
        //BackgroundWorker bw = new BackgroundWorker(this);
        //bw.execute(type, user, password );
        finish();
        startActivity(new Intent(this, Home.class));
    }

    /**
     * runs on user registration
     * @param view: button pressed
     */
    public void onReg(View view) {
        startActivity(new Intent(this, Register.class));
    }

    // returns error textview
    public TextView getErrorText()
    {
        return (TextView)findViewById(R.id.errortext);
    }
}
