package com.example.fyp_anroid;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchPanel extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
    public static int REQUEST_LOCATION = 1;
    protected LocationRequest mLocationRequest;
    static public Location mLastLocation;
    protected Geocoder mGeocoder;
    Double CurrentLatitudeText;
    Double CurrentLongitudeText;
    Button btn_searchDoctor,btn_back;
    EditText et_doctorName, et_doctorchiName, et_location;
    TextView tv_doctorName ,tv_doctorChiName,tv_location,tv_subject;
    Spinner subject, regionDropDown;
    String URL= "https://ivefypnodejsbackned.herokuapp.com/Doctor_info/search";
    String RegionURL= "https://ivefypnodejsbackned.herokuapp.com/Doctor_info/getRegionByLocation";
    JSONParser jsonParser=new JSONParser();
    String [] regionList = {"","中西區","灣仔區","東區","南區","黃大仙區","觀塘區","深水埗區","油尖旺區","九龍城區","北區","大埔區","沙田區","西貢區","葵青區","荃灣區","屯門區","元朗區","離島區"};
    String RegionString = "";
    String status;
    protected GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_panel);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        btn_searchDoctor = (Button)findViewById(R.id.Search);
        et_doctorName = (EditText)findViewById(R.id.ET_DoctorName);
        tv_doctorName = (TextView)findViewById(R.id.TV_DoctorName);
        et_doctorchiName = (EditText)findViewById(R.id.ET_DoctorChiName);
        tv_doctorChiName = (TextView)findViewById(R.id.TV_DoctorChiName);
        et_location = (EditText)findViewById(R.id.ET_location);
        tv_location = (TextView) findViewById(R.id.TV_location);
        tv_subject = (TextView)findViewById(R.id.TV_Subject);
        subject = (Spinner)findViewById(R.id.sp_subject);
        regionDropDown = (Spinner)findViewById(R.id.sp_region);
        btn_back = (Button)findViewById(R.id.SearchPanelBackButton);
        //--set up dropdown list
        String[] items = new String[]{this.getResources().getString(R.string.OnG)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        subject.setAdapter(adapter);
        getCurrentLocation();
        // region
        String [] locationItems = new String[]{
                "",
                this.getResources().getString(R.string.LocationD1),
                this.getResources().getString(R.string.LocationD2),
                this.getResources().getString(R.string.LocationD3),
                this.getResources().getString(R.string.LocationD4),
                this.getResources().getString(R.string.LocationD5),
                this.getResources().getString(R.string.LocationD6),
                this.getResources().getString(R.string.LocationD7),
                this.getResources().getString(R.string.LocationD8),
                this.getResources().getString(R.string.LocationD9),
                this.getResources().getString(R.string.LocationD10),
                this.getResources().getString(R.string.LocationD11),
                this.getResources().getString(R.string.LocationD12),
                this.getResources().getString(R.string.LocationD13),
                this.getResources().getString(R.string.LocationD14),
                this.getResources().getString(R.string.LocationD15),
                this.getResources().getString(R.string.LocationD16),
                this.getResources().getString(R.string.LocationD17),
                this.getResources().getString(R.string.LocationD18),
        };

         adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locationItems);
        regionDropDown.setAdapter(adapter);
        regionDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             RegionString = regionList[position];

           }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

           }
                                         });
                //--set up dropdown list
                //---do search
                btn_searchDoctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String DoctorEngName = et_doctorName.getText().toString();
                        String DoctorChiName = et_doctorchiName.getText().toString();
                        String location = et_location.getText().toString();
                        Log.d("CHIName", DoctorChiName);
                        GetAllDoctorInfo getAllDoctorInfo = new GetAllDoctorInfo(SearchPanel.this);
                        getAllDoctorInfo.execute(DoctorEngName, DoctorChiName, location,RegionString);


                    }
                });


        btn_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                GoToHomePage();
            }
        });



    }

    public void getCurrentLocation() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
             status = "GoogleApiClient has started. You can see the location icon in status bar";
        }
    }
    public void onConnected(@Nullable Bundle connectionHint) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d("xxxx", "93");

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.d("xxxx", "99");

            if (mLastLocation != null) {
                CurrentLatitudeText = mLastLocation.getLatitude();
                CurrentLongitudeText = mLastLocation.getLongitude();
                Toast.makeText(getApplicationContext(), "LL ="+CurrentLatitudeText+CurrentLongitudeText, Toast.LENGTH_LONG).show();
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            getLocationString();
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onConnected(null);
            }
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void onLocationChanged(Location location) {

    }
    public void getLocationString() {
        mGeocoder = new Geocoder(this);
        try {
            List<Address> addresses = mGeocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);

//            if (addresses.size() == 1) {
                Address address = addresses.get(0);
                StringBuilder addressLines = new StringBuilder();
                //see herehttps://stackoverflow
                // .com/questions/44983507/android-getmaxaddresslineindex-returns-0-for-line-1
                if (address.getMaxAddressLineIndex() > 0) {
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        addressLines.append(address.getAddressLine(i) + "\n");
                    }
                } else {
                    addressLines.append(address.getAddressLine(0));
                }
                Toast.makeText(getApplicationContext(), addressLines, Toast.LENGTH_LONG).show();
                //tv_doctorName.setText(addressLines);
            GetRegion getRegion = new GetRegion(SearchPanel.this);
            Log.d("AddressLines",addressLines.toString());
            getRegion.execute(addressLines.toString());
 //           }
//            else {
//                Toast.makeText(getApplicationContext(), "WARNING! Geocoder returned more than 1 addresses!", Toast.LENGTH_LONG).show();
//            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "WARNING! Geocoder.getFromLocation() didn't work!", Toast.LENGTH_LONG).show();
        }
    }

    public void  GoToHomePage(){
        Intent intent = new Intent(this, HomePage.class);

        startActivity(intent);
    }
    private class GetAllDoctorInfo extends AsyncTask<String, String, JSONObject> {
        Context context;
        ProgressDialog mDialog;
        GetAllDoctorInfo(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait...");
            mDialog.show();

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SearchPanel.this);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("BackPage","SearchPanel");
            editor.apply();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            Log.d("In","Doinbackground");
            String DoctorEngName = args[0];
            String DoctorChiName = args[1];
            String location = args[2];
            String region = args[3];
            Log.d("DoctorChiName", DoctorChiName);
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name_chi", DoctorChiName));
            params.add(new BasicNameValuePair("name_eng", DoctorEngName));
            params.add(new BasicNameValuePair("location", location));
            params.add(new BasicNameValuePair("region", region));
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
            try {
                Log.d("result json", result+"");
              JSONArray ja = result.getJSONArray("Doctor_info");

                for (int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    _id.add(jo.getString("_id"));
                    name_chi.add(jo.getString("name_chi"));
                    name_eng.add(jo.getString("name_eng"));
                    price.add(jo.getString("price"));
                    location.add(jo.getString("location"));
                    location_region.add(jo.getString("location_region"));
                    mark.add(jo.getString("mark"));
                    qualification.add(jo.getString("qualification"));
                //    Log.d("name_eng",jo.getString("name_eng"));
                //    Log.d("qualification",jo.getString("qualification"));
                }
                Intent intent = new Intent(SearchPanel.this, SearchResult.class);
                intent.putStringArrayListExtra("_id", _id);
                intent.putStringArrayListExtra("name_eng", name_eng);
                intent.putStringArrayListExtra("name_chi", name_chi);
                intent.putStringArrayListExtra("location", location);
                intent.putStringArrayListExtra("mark", mark);
                startActivity(intent);
                mDialog.dismiss();
            }catch(Exception ex){
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            }


        }
    }

    private class GetRegion extends AsyncTask<String, String, JSONObject> {
        Context context;
        ProgressDialog mDialog;
        GetRegion(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait...");
            mDialog.show();
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            Log.d("In","Doinbackground");

            String location = args[0];


            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("location", location));

            JSONObject json = jsonParser.makeHttpRequest(RegionURL, "POST",params);
            return json;
        }
        protected void onPostExecute(JSONObject result) {//JSONARRAY
            Log.d("In", "onPostExecute: ");
            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            try {
                Log.d("result json", result+"");
                JSONObject json = result;
                String region = json.getString("message");
                Log.d("Region",region);
                int position =Arrays.asList(regionList).indexOf(region);
                regionDropDown.setSelection(position);




                mDialog.dismiss();
            }catch(Exception ex){
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            }


        }
    }
}
