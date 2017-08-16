package com.example.liqhtninq.strengthtracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Kailash Jayaram on 6/30/2017.
 */

/**
 * class BackgroundWorker
 *
 * runs background connection to local server to handle login and register processes
 *
 * context: context called from
 *
 */
public class BackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;

    // constructor sets context
    BackgroundWorker (Context ctx) {
        context = ctx;
    }

    /**
     * checks login status
     * @param params: login/register information
     * @return a string dictating whether or not login/register was accepted
     */
    @Override
    protected String doInBackground(String... params) {
        // whether login or register
        String type = params[0];

        // connects to local server
        String login_url = "http://192.168.10.102:8080/STLogin.php";
        String register_url = "http://192.168.10.102:8080/STRegister.php";

        // on login
        if(type.equals("login"))
        {
            try {
                // get username and password
                String user = params[1];
                String pass = params[2];

                // open and set up connection
                URL login = new URL(login_url);
                HttpURLConnection con = (HttpURLConnection)login.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);

                // send the username and password to the database
                OutputStream os = con.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String post_data = URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(user,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
                bw.write(post_data);

                // clean up
                bw.flush();
                bw.close();
                os.close();

                // read in result
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
                String result ="";
                String line ="";
                while((line = br.readLine()) != null) {
                    result += line;
                }

                // clean up
                br.close();
                is.close();
                con.disconnect();

                return result;
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // on register
        else if(type.equals("register")){
            try {

                // get registration info
                String name = params[1];
                String surname = params[2];
                String email = params[3];
                String username = params[4];
                String password = params[5];

                // open and set up connection
                URL login = new URL(register_url);
                HttpURLConnection con = (HttpURLConnection)login.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);

                // send info to data base
                OutputStream os = con.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String post_data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                        +URLEncoder.encode("surname","UTF-8")+"="+URLEncoder.encode(surname,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        +URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bw.write(post_data);

                // clean up
                bw.flush();
                bw.close();
                os.close();

                // read in result
                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
                String result ="";
                String line ="";
                while((line = br.readLine()) != null) {
                    result += line;
                }

                // clean up
                br.close();
                is.close();
                con.disconnect();

                return result;
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * handles next action after login attempt
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {

        // on Successful register
        if(result.equals("Insert Successful"))
        {
            context.startActivity(new Intent(context, Login.class));
        }
        // on Successful login
        else if(result.equals("login success")){
            context.startActivity(new Intent(context, Home.class));
        }
        // on failed login
        else if(result.equals("login fail"))
        {
            TextView error = (TextView)((Login)context).getErrorText();
            error.setText("Login Failed, Incorrect Username or Password");
            error.setTextColor(Color.rgb(255,0,0));
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}