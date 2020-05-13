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
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CommentPage extends AppCompatActivity {
    //----CommentListView
    ListView CommentList;
    private Context mContext;
    private List<Comment_List> mData = null;
    private Comment_list_view_adapter mAdapter = null;
    //----
    JSONParser jsonParser=new JSONParser();
    EditText ed_Comment;
    Button add_Comment,btn_back;
    String doctorId = "";
    String userId ="";

    String URL= "https://ivefypnodejsbackned.herokuapp.com/Comment/getallbybelongto";
    String CreateCommentURL= "https://ivefypnodejsbackned.herokuapp.com/Comment/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_page);
        //definition
        ed_Comment = (EditText) findViewById(R.id.ed_comment);
        add_Comment = (Button) findViewById(R.id.add_comment);
        CommentList = (ListView)findViewById(R.id.CommentList);
        btn_back = (Button) findViewById(R.id.BackDoctorPage);
        //get doctor id first
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        doctorId = preferences.getString("_id", null);
        //get user id
        userId = preferences.getString("LoginUserId", null);
        //set the ListView
        mContext = CommentPage.this;

        //call api get the data and set it into comment list
        GetAllCommentByDoctor getAllCommentByDoctor= new GetAllCommentByDoctor(this);
        getAllCommentByDoctor.execute(doctorId);


        //--set button event
        add_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //--call api add comment first
                CreateComment createComment = new CreateComment(CommentPage.this);
                String ed_Comment_string = ed_Comment.getText().toString();
                createComment.execute(ed_Comment_string);
                //--Refresh the Comment list view
                GetAllCommentByDoctor getAllCommentByDoctor= new GetAllCommentByDoctor(CommentPage.this);
                getAllCommentByDoctor.execute(doctorId);
            }
        });

        //--btn back
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentPage.this, DoctorPage.class);
                startActivity(intent);
            }
        });
    }

    private class GetAllCommentByDoctor extends AsyncTask<String, String, JSONObject> {
        Context context;
        ProgressDialog mDialog;
        GetAllCommentByDoctor(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait...");
            mDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            Log.d("In","Doinbackground");
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Belongto", args[0]));
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST",params);
            return json;
        }
        protected void onPostExecute(JSONObject result) {//JSONARRAY
            Log.d("In", "onPostExecute: ");
            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            ArrayList Comment_id = new ArrayList();
            ArrayList Comment = new ArrayList();
            ArrayList CreateTime = new ArrayList();
            ArrayList CreateByUserName = new ArrayList();
            ArrayList CommentMark = new ArrayList();
            mData = new LinkedList<Comment_List>();
            try {
                Log.d("result json", result+"");
                JSONArray ja = result.getJSONArray("comments");

                for (int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    Comment_id.add(jo.getString("_id"));
                    Comment.add(jo.getString("Comment"));
                    CreateTime.add(jo.getString("CreateTime"));
                    CommentMark.add(jo.getString("Mark"));
                    JSONObject CreateBy = jo.getJSONObject("Createby");
                    CreateByUserName.add(CreateBy.getString("username"));
                }
                Log.d("Comment_id", Comment_id.toString());
                Log.d("Comment_id", Comment.toString());
                Log.d("Comment_id", CreateTime.toString());
                Log.d("Comment_id", CommentMark.toString());
                Log.d("Comment_id", CreateByUserName.toString());

                //
                for (int gg = 0; gg < Comment_id.size(); gg++) {
                    mData.add(new Comment_List((String) Comment_id.get(gg), (String) Comment.get(gg),(String) CreateTime.get(gg),(String) CreateByUserName.get(gg),(String)CommentMark.get(gg) ));
                }
                Log.d("MDATA", mData.toString());
                mAdapter = new Comment_list_view_adapter((LinkedList<Comment_List>) mData, mContext);
                CommentList.setAdapter(mAdapter);
                mDialog.dismiss();

            }catch(Exception ex){
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            }


        }
    }


    private class CreateComment extends AsyncTask<String, String, JSONObject> {
        Context context;
        ProgressDialog mDialog;
        CreateComment(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait...");
            mDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            Log.d("In","Doinbackground");
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            String Comment = args[0];

            params.add(new BasicNameValuePair("Belongto", doctorId));
            params.add(new BasicNameValuePair("Createby", userId));
            params.add(new BasicNameValuePair("Comment", Comment));
            JSONObject json = jsonParser.makeHttpRequest(CreateCommentURL, "POST",params);
            return json;
        }
        protected void onPostExecute(JSONObject result) {//JSONARRAY
            Log.d("In", "onPostExecute: ");

            try {
                String message = result.getString("message");
                if(message=="Comment created"){
                    Toast toast=Toast.makeText(CommentPage.this, message, Toast.LENGTH_LONG);
                    toast.show();
                }
                mDialog.dismiss();

            }catch(Exception ex){
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            }


        }
    }

    public void GoToDoctorPage(){
        Intent intent = new Intent(this, DoctorPage.class);

        startActivity(intent);
    }
}
