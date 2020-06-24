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
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    JSONParser jsonParser=new JSONParser();
    Button btnSearch,btnFavorite,btnlogout, btnUpload;
    String userId ="";
    String URL= "https://ivefypnodejsbackned.herokuapp.com/favorites/getallbyuserid";
    String MedicalListURL = "https://ivefypnodejsbackned.herokuapp.com/medical_list/getallbyuser";
    ArrayList MedicalList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        btnSearch = (Button)findViewById(R.id.btn_search);
        btnFavorite = (Button)findViewById(R.id.btn_favorite);
        btnlogout= (Button)findViewById(R.id.btn_logout);
        btnUpload = (Button)findViewById(R.id.btn_upload);
        //---get user id
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString("LoginUserId", null);

        GetAllMedicalListByUserId getAllMedicalListByUserId = new GetAllMedicalListByUserId(HomePage.this);
        getAllMedicalListByUserId.execute(userId);
        //Button event
        btnSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                GoToSearchPanel();
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                logout();
            }
        });
        btnFavorite.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                GetAllFavoriteByUserId getAllFavoriteByUserId = new GetAllFavoriteByUserId(HomePage.this);
                getAllFavoriteByUserId.execute(userId);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                GoToUploadFilePage();
            }
        });
    }

    public void GoToUploadFilePage(){
        Intent intent = new Intent(this, UploadDoctoeListPage.class);

        startActivity(intent);
    }
    public void GoToSearchPanel(){
        Intent intent = new Intent(this, SearchPanel.class);

        startActivity(intent);
    }
    public void logout(){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    private class GetAllFavoriteByUserId extends AsyncTask<String, String, JSONObject> {
        Context context;
        ProgressDialog mDialog;
        GetAllFavoriteByUserId(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait...");
            mDialog.show();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(HomePage.this);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("BackPage","HomePage");
            editor.apply();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            Log.d("In","Doinbackground");
            String useridd = args[0];
            Log.d("ARGS0", useridd);
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", userId));
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST",params);
            return json;
        }
        protected void onPostExecute(JSONObject result) {//JSONARRAY
            Log.d("In", "onPostExecute: ");
            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            ArrayList _id = new ArrayList();
            ArrayList name_chi = new ArrayList();
            ArrayList name_eng = new ArrayList();
            ArrayList price = new ArrayList();
            ArrayList location = new ArrayList();
            ArrayList location_region = new ArrayList();
            ArrayList mark = new ArrayList();
            ArrayList image = new ArrayList();
            ArrayList gender = new ArrayList();
            ArrayList qualification = new ArrayList();
            ArrayList openTime = new ArrayList();
            ArrayList website = new ArrayList();
            ArrayList subjectList = new ArrayList();
            ArrayList infopath = new ArrayList();
            ArrayList PassMedicalList = new ArrayList();
            Boolean isMedicalList =false;
            try {
                Log.d("result json", result+"");
                JSONArray ja = result.getJSONArray("Recode");

                for (int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    JSONObject doctorinfo = jo.getJSONObject("Doctor_info");
                    for (Object item :
                            MedicalList) {
                        Log.d("item", item.toString());
                        Log.d("Doctor_id", doctorinfo.getString("_id"));
                        if(item.toString().equals(doctorinfo.getString("_id"))){
                            isMedicalList = true;
                            break;
                        }
                    }

                    Log.d("doctorinfo", doctorinfo+"");
                    _id.add(doctorinfo.getString("_id"));
                    name_chi.add(doctorinfo.getString("name_chi"));
                    name_eng.add(doctorinfo.getString("name_eng"));
                    location.add(doctorinfo.getString("location"));
                    mark.add(doctorinfo.getString("mark"));
                    if(isMedicalList){
                        PassMedicalList.add(true);
                    }else{
                        PassMedicalList.add(false);
                    }
                    isMedicalList =false;
                }
                Intent intent = new Intent(HomePage.this, SearchResult.class);
                intent.putStringArrayListExtra("_id", _id);
                intent.putStringArrayListExtra("name_eng", name_eng);
                intent.putStringArrayListExtra("name_chi", name_chi);
                intent.putStringArrayListExtra("location", location);
                intent.putStringArrayListExtra("mark", mark);
                intent.putStringArrayListExtra("PassMedicalList", PassMedicalList);
                startActivity(intent);
                mDialog.dismiss();
            }catch(Exception ex){
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            }


        }
    }


    private class GetAllMedicalListByUserId extends AsyncTask<String, String, JSONObject> {
        Context context;
        ProgressDialog mDialog;
        GetAllMedicalListByUserId(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait...");
            mDialog.show();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(HomePage.this);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("BackPage","HomePage");
            editor.apply();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            Log.d("In","Doinbackground");
            String useridd = args[0];
            Log.d("ARGS0", useridd);
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            Log.d("YOYYOYO", userId);
            params.add(new BasicNameValuePair("UserId", userId));
            JSONObject json = jsonParser.makeHttpRequest(MedicalListURL, "POST",params);
            return json;
        }
        protected void onPostExecute(JSONObject result) {//JSONARRAY
            Log.d("In", "onPostExecute: ");
            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            ArrayList _id = new ArrayList();
            ArrayList name_chi = new ArrayList();
            ArrayList name_eng = new ArrayList();
            ArrayList price = new ArrayList();
            ArrayList location = new ArrayList();
            ArrayList location_region = new ArrayList();
            ArrayList mark = new ArrayList();
            ArrayList image = new ArrayList();
            ArrayList gender = new ArrayList();
            ArrayList qualification = new ArrayList();
            ArrayList openTime = new ArrayList();
            ArrayList website = new ArrayList();
            ArrayList subjectList = new ArrayList();
            ArrayList infopath = new ArrayList();
            try {
                Log.d("result json", result+"");
                JSONArray ja = result.getJSONArray("item");

                for (int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    Log.d("doctorinfo", jo+"");
                    String DoctorID = jo.getJSONObject("DoctorId").getString("_id");

                    _id.add(DoctorID);
                    MedicalList.add(DoctorID);
                }
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(HomePage.this);
                SharedPreferences.Editor editor = pref.edit();
                Gson gson = new Gson();
                String json = gson.toJson(_id);
                Log.d("MedicalList JSon :", json);
                editor.putString("MedicalListID",json );
                editor.commit();
                editor.apply();
                mDialog.dismiss();
            }catch(Exception ex){
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            }


        }
    }
}
