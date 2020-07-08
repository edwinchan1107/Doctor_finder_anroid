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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DoctorPage extends FragmentActivity implements OnMapReadyCallback {
    TextView name_eng,name_chi,mark,location, tv_EdrRank,tv_SeeDocRank,tv_MiningRank,tv_MiningAVGMark;
    JSONParser jsonParser=new JSONParser();
    Button AddDelFavorite,GoComment;
    GoogleMap map;
    String URL= "https://ivefypnodejsbackned.herokuapp.com/Doctor_info/getonebyid";
    String checkFavoriteURL= "https://ivefypnodejsbackned.herokuapp.com/favorites/getallbyuserid";
    String AddFavoriteURL= "https://ivefypnodejsbackned.herokuapp.com/favorites";
    String DelFavoriteURL= "https://ivefypnodejsbackned.herokuapp.com/favorites/delete";
    String userId ="";
    String doctorId = "";
    Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorpage);
        //---setup google map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //---setup google map
        tv_EdrRank = (TextView)findViewById(R.id.detail_tv_EdrRank);
        tv_SeeDocRank = (TextView)findViewById(R.id.detail_tv_SeeDocRank);
        tv_MiningRank = (TextView)findViewById(R.id.detail_tv_MiningRank);
        tv_MiningAVGMark = (TextView)findViewById(R.id.detail_tv_MiningAVGMark);
        name_eng = (TextView)findViewById(R.id.detail_tv_name_eng);
        name_chi = (TextView)findViewById(R.id.detail_tv_name_chi);
        mark = (TextView)findViewById(R.id.detail_tv_mark);
        location = (TextView)findViewById(R.id.detail_tv_location);
        AddDelFavorite = (Button) findViewById(R.id.btn_Add_favorite);
        GoComment= (Button) findViewById(R.id.btn_go_comment);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        doctorId = preferences.getString("_id", null);
        userId = preferences.getString("LoginUserId", null);
        if(doctorId!=null){
            Log.d("doctorId",doctorId);
            GetOneDocterById getOneDocterById= new GetOneDocterById(this);
            getOneDocterById.execute(doctorId,"");
        }
        //---check is favorite?
        GetAllFavoriteByUserId getAllFavoriteByUserId = new GetAllFavoriteByUserId(this);
        getAllFavoriteByUserId.execute();


        GoComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass the doctor id and got o comment page
                GoCommentPage();
            }
        });
        AddDelFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //add to favorite / delete favorite
                AddOrDelFavorite addOrDelFavorite = new AddOrDelFavorite(DoctorPage.this);
                addOrDelFavorite.execute();
            }
        });
    }

    public void GoCommentPage(){
        Intent intent = new Intent(this, CommentPage.class);

        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // test laglng 22.281135,114.156816
        LatLng latLng = new LatLng(22.281135,114.156816);
        float zoomLevel = 16.0f;
        map.addMarker(new MarkerOptions().position(latLng).title("Test location"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));

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
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            Log.d("In","Doinbackground");
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            Log.d("USERID",userId);
            params.add(new BasicNameValuePair("user_id", userId));
            JSONObject json = jsonParser.makeHttpRequest(checkFavoriteURL, "POST",params);
            return json;
        }
        protected void onPostExecute(JSONObject result) {//JSONARRAY

            Log.d("In", "onPostExecute: ");
            Log.d("DOCTORID", doctorId);
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
                JSONArray ja = result.getJSONArray("Recode");
                Log.d("JSON ARRAY JA RECODE", ja+"");
                Log.d("JSON ARRAY JA LENGTH", ja.length()+"");

                for (int i=0;i<ja.length();i++){

                    JSONObject jo = ja.getJSONObject(i);
                    JSONObject doctorinfo = jo.getJSONObject("Doctor_info");

                    String docid = doctorinfo.getString("_id");

                    if(docid.equals(doctorId)){
                        Log.d("INNNNNN", "YOYOYOYO ");
                        isFavorite = true;
                    }

                }

                if(isFavorite){
                   // AddDelFavorite.setText("DelFavorite");
                    Log.d("INNNNNN", "YOYOYOYO ");
                    AddDelFavorite.setBackgroundResource(R.drawable.baseline_star_black_18dp);
                }else{
                   // AddDelFavorite.setText("AddFavorite");
                    Log.d("INNNNNN", "AAAAAAAAAAA ");
                    AddDelFavorite.setBackgroundResource(R.drawable.baseline_star_border_black_18dp);
                }


                mDialog.dismiss();
            }catch(Exception ex){
                Log.d("ERROR", ex.getMessage());
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            }


        }
    }

    private class GetOneDocterById extends AsyncTask<String,String,JSONObject> {
        Context context;
        ProgressDialog mDialog;
        @Override

        protected void onPreExecute() {
            Log.d("In","onPreExecute");
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait...");
            mDialog.show();
            super.onPreExecute();

        }
        GetOneDocterById(Context context){
            this.context = context;
        }

        @Override

        protected JSONObject doInBackground(String... args) {

            Log.d("In","doInBackground");
            String doctorId = args[0];
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", doctorId));
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);
            return json;

        }

        protected void onPostExecute(JSONObject result) {//JSONARRAY
           // super.onPreExecute();
            Log.d("In", "onPostExecute: ");

            try {
                JSONObject Doctor = result.getJSONObject("Doctor");
                String getname_chi = Doctor.getString("name_chi");
                String getname_eng = Doctor.getString("name_eng");
                String getlocation = Doctor.getString("location");
                String getmark = Doctor.getString("mark");
                Log.d("TTTSSSSSSS", Doctor.toString());
                String EdrRank = Doctor.getString("edrRank");
                String SeeDocRank = Doctor.getString("seeDocRank");
                String MiningRank = Doctor.getString("miningRank");
                String MiningCount = Doctor.getString("miningCount");
                String miningAVGMark = Doctor.getString("miningAVGMark");
                Log.d("MMMMMMM",MiningCount);
                Log.d("MMMMMMM",miningAVGMark);
                Log.d("TTTSSSSSSS", Doctor.getString("edrRank"));
                tv_MiningAVGMark.setText(miningAVGMark);
                name_eng.setText(getname_eng);
                location.setText(getlocation);
                name_chi.setText(getname_chi);
                Log.d("Check ", Doctor.getString("edrRank"));
                Log.d("Check ", Doctor.getString("miningRank"));
                Log.d("Check ", Doctor.getString("edrRank"));
                tv_EdrRank.setText("Edr RK: "+EdrRank);
                tv_SeeDocRank.setText("SeeDoc RK: "+SeeDocRank);
                tv_MiningRank.setText("Mining RK: "+MiningRank);
               // mark.setText("Mark : "+getmark);
                mark.setText("");
                Log.d("SETALLTV","ALLLLLLL");
                mDialog.dismiss();
            }catch(Exception ex){

            }


        }



    }

    private class AddOrDelFavorite extends AsyncTask<String, String, JSONObject> {
        Context context;
        ProgressDialog mDialog;
        AddOrDelFavorite(Context context){
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
            Log.d("USERID",userId);
            Log.d("DoctorID",userId);
            params.add(new BasicNameValuePair("user_id", userId));
            params.add(new BasicNameValuePair("doctor_id", doctorId));
            String passURL;
            if(isFavorite){
                passURL = DelFavoriteURL;
            }else{
                passURL = AddFavoriteURL;
            }
            JSONObject json = jsonParser.makeHttpRequest(passURL, "POST",params);
            return json;
        }
        protected void onPostExecute(JSONObject result) {//JSONARRAY

            Log.d("In", "onPostExecute: ");

            try {
                Log.d("JSONObject", result+"");
                String message = result.getString("message");
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.d("message", message);
                if(isFavorite){
                   // AddDelFavorite.setText("AddFavorite");
                   // Drawable d = getResources().getDrawable(R.drawable.baseline_star_black_18dp);
                    AddDelFavorite.setBackgroundResource(R.drawable.baseline_star_border_black_18dp);
                    isFavorite = false;
                }else{
                   // AddDelFavorite.setText("DelFavorite");
                    AddDelFavorite.setBackgroundResource(R.drawable.baseline_star_black_18dp);

                    isFavorite = true;
                }

                mDialog.dismiss();
            }catch(Exception ex){
                Log.d("ERROR", ex.getMessage());
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            }


        }
    }

}
