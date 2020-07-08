package com.example.fyp_anroid;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadDoctoeListPage extends AppCompatActivity {
    Button uploadFileBtn;
    int FILE_PICKER_REQUEST_CODE = 1;
    JSONParser jsonParser=new JSONParser();
    String URL= "https://ivefypnodejsbackned.herokuapp.com/medical_list/ByName";
    String path = Environment.getExternalStorageDirectory().getPath();
    String userId;
    ArrayList<String> DoctorIdList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString("LoginUserId", null);
        //---
        setContentView(R.layout.activity_upload_doctoe_list_page);
        uploadFileBtn = findViewById(R.id.btn_uploadFile);
        String filepath = Environment.getExternalStorageDirectory().getPath();
        String path = Environment.DIRECTORY_DOWNLOADS;
        File test = Environment.getExternalStoragePublicDirectory("Download");
        String test2 = this.getFilesDir().getAbsolutePath();
        uploadFileBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

               new MaterialFilePicker()
                        .withActivity(UploadDoctoeListPage.this)
                        .withHiddenFiles(true)
                        .withRequestCode(FILE_PICKER_REQUEST_CODE)
                        .start();
                //--new
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
           // Toast.makeText(getApplicationContext(), filePath, Toast.LENGTH_LONG).show();
            readExcel(filePath);
        }
    }

    private void readExcel(String filePath) {
        try {
            ArrayList<String> DoctorNameList = new ArrayList<>();
            Workbook workbook = Workbook.getWorkbook(new File(filePath));
            //獲取第一個工作表的對象
            Sheet sheet = workbook.getSheet(0);
            for (int row = 1; row<sheet.getRows();row++){
                Cell cell = sheet.getCell(0, row);
                String DoctorName = cell.getContents();
                DoctorNameList.add(DoctorName);
            }
            ArrayList<String> DoctorIdList = new ArrayList<>();
            //----do search use name to get the id
            UploadDoctoeListPage.AddToMedicalList AddToMedicalList = new UploadDoctoeListPage.AddToMedicalList(UploadDoctoeListPage.this);

            for (int i=0;i<DoctorNameList.size();i++){
                Log.d("TAG", DoctorNameList.get(i));
                //AddToMedicalList.execute(DoctorNameList.get(i),userId);
                new AddToMedicalList(UploadDoctoeListPage.this).execute(DoctorNameList.get(i),userId);
            }


            //讀取數據關閉
            workbook.close();

            Toast toast=Toast.makeText(UploadDoctoeListPage.this, "Uploaded", Toast.LENGTH_LONG);
            toast.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1001:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_LONG).show();
                    uploadFileBtn.setEnabled(false);
                }
            }
        }

    }

    private class AddToMedicalList extends AsyncTask<String, String, JSONObject> {
        Context context;
        ProgressDialog mDialog;
        AddToMedicalList(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Please wait...");
            mDialog.show();

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(UploadDoctoeListPage.this);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("BackPage","SearchPanel");
            editor.apply();
        }
        @Override
        protected JSONObject doInBackground(String... args) {

            Log.d("In","Doinbackground");
            String DoctorEngName = args[0];
            String UserId = args[1];
            Log.d("Handle Doctor Name :", DoctorEngName);
            Log.d("Handle Doctor Name :", UserId);
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("DoctorName", DoctorEngName));
            params.add(new BasicNameValuePair("UserId", UserId));
            JSONObject json = jsonParser.makeHttpRequest(URL, "POST",params);
            return json;
        }
        protected void onPostExecute(JSONObject result) {//JSONARRAY
            Log.d("In", "onPostExecute: ");

            try {
                Log.d("result json", result+"");
                JSONArray ja = result.getJSONArray("Doctor_info");


                mDialog.dismiss();

            }catch(Exception ex){
                mDialog.dismiss();
            //    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            }


        }
    }



}

