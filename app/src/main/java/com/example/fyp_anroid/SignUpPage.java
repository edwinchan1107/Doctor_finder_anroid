package com.example.fyp_anroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.PasswordAuthentication;
import java.util.ArrayList;

public class SignUpPage extends AppCompatActivity {
    Button btnBack,btnSend;
    EditText UserName , Email , Password, ConfirmPassword;
    String URL= "https://ivefypnodejsbackned.herokuapp.com/users/signup";
    JSONParser jsonParser=new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        UserName=(EditText)findViewById(R.id.SignUp_UserName);
        Email=(EditText)findViewById(R.id.SignUp_Email);
        Password =(EditText)findViewById(R.id.SignUp_Password);
        ConfirmPassword =(EditText)findViewById(R.id.SignUp_ConfirmPassword);
        btnBack = (Button)findViewById(R.id.SignUp_Back);
        btnSend = (Button)findViewById(R.id.SignUp_Submit);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToMain();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordString = Password.getText().toString();
                String c_passwordString = ConfirmPassword.getText().toString();
                if(!passwordString.equals(c_passwordString)||passwordString.length()==0){
                    Toast toast=Toast.makeText(SignUpPage.this, "password must be same and not empty", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    //register ac
                    SignUpAccount signUpAccount= new SignUpAccount(SignUpPage.this);
                    signUpAccount.execute(Email.getText().toString(),passwordString,UserName.getText().toString());
                }
            }
        });
    }

    public void BackToMain(){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }


    private class SignUpAccount extends AsyncTask<String, String, JSONObject> {
        Context context;
        ProgressDialog mDialog;
        @Override

        protected void onPreExecute() {
            Log.d("In","onPreExecute");
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait...");
            mDialog.show();
            super.onPreExecute();
            super.onPreExecute();

        }
        SignUpAccount(Context context){
            this.context = context;
        }
        @Override

        protected JSONObject doInBackground(String... args) {

            Log.d("In","Doinbackground");
            Log.d("args0",args[0]);
            Log.d("args1",args[1]);
            String password = args[1];
            String email= args[0];
            String username = args[2];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("usertype", "customer"));
            params.add(new BasicNameValuePair("username", username));
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {//JSONARRAY
            Log.d("In", "onPostExecute: ");
            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                Log.d("result json", result+"");

                String message = result.getString("message");
                Log.d("result message", message);
                if(message.equals("Created user succesfully")){
                    Toast toast=Toast.makeText(SignUpPage.this, message, Toast.LENGTH_LONG);
                    toast.show();
                    mDialog.dismiss();
                    BackToMain();
                }else{
                    Toast toast=Toast.makeText(SignUpPage.this, message, Toast.LENGTH_LONG);
                    toast.show();
                    mDialog.dismiss();
                }

            }catch(Exception ex){
                mDialog.dismiss();
             //   Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                Toast toast=Toast.makeText(SignUpPage.this, "Unable to retrieve any data from server", Toast.LENGTH_LONG);
                toast.show();
            }


        }



    }

}
