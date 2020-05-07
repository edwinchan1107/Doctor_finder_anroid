package com.example.fyp_anroid;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editPassword, editName;
    Button btnSignIn;
    Button btnPhone;


   String URL= "https://ivefypnodejsbackned.herokuapp.com/users/login";
    JSONParser jsonParser=new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName=(EditText)findViewById(R.id.editName);
        editPassword=(EditText)findViewById(R.id.editPassword);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptLogin attemptLogin= new AttemptLogin(MainActivity.this);
                if(editName.getText().toString().equals("")||editPassword.getText().toString().equals("")){
                    Toast toast=Toast.makeText(MainActivity.this, "Please Input Account/Password", Toast.LENGTH_LONG);
                    toast.show();
                }else {
                    attemptLogin.execute(editName.getText().toString(), editPassword.getText().toString(), "");
                }


            }
        });




    }


    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {
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
        AttemptLogin(Context context){
            this.context = context;
        }
        @Override

        protected JSONObject doInBackground(String... args) {

            Log.d("In","Doinbackground");
            Log.d("args0",args[0]);
            Log.d("args1",args[1]);
            String password = args[1];
            String name= args[0];

            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", name));
            params.add(new BasicNameValuePair("password", password));

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
                String LoginUserId = result.getString("userid");
                Log.d("result message", message);


                
                if (message.equals("Auth successful")) {
                    //-save the user id in session/share-//
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("LoginUserId",LoginUserId);
                    editor.apply();
                    //go to home page
                    mDialog.dismiss();
                    logined();

                } else {
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            }catch(Exception ex){

            }


        }



    }

    public void logined(){
           Intent intent = new Intent(this, HomePage.class);

         startActivity(intent);
    }

}


