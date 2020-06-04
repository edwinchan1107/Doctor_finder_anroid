package com.example.fyp_anroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

public class UploadDoctoeListPage extends AppCompatActivity {
    Button uploadFileBtn;
    int FILE_PICKER_REQUEST_CODE = 1;
    String path = Environment.getExternalStorageDirectory().getPath();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
        }
        //---
        setContentView(R.layout.activity_upload_doctoe_list_page);
        uploadFileBtn = findViewById(R.id.btn_uploadFile);
        uploadFileBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
               //---open file chooser

//                Intent intent = new Intent( Intent.ACTION_PICK );
//                Intent destIntent = Intent.createChooser( intent, "選擇檔案" );
//                startActivityForResult( destIntent, 0 );

                //---new
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
            Toast.makeText(getApplicationContext(), filePath, Toast.LENGTH_LONG).show();
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

//        @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // 有選擇檔案
//        if ( resultCode == RESULT_OK )
//        {
//            // 取得檔案的 Uri
//            Uri uri = data.getData();
//            if( uri != null )
//            {
//                // 利用 Uri 顯示 ImageView 圖片
////                ImageView iv = (ImageView)this.findViewById(R.id.imageViewObj);
////                iv.setImageURI( uri );
////
////                setTitle( uri.toString() );
//            }
//            else
//            {
//                setTitle("無效的檔案路徑 !!");
//            }
//        }
//        else
//        {
//            setTitle("取消選擇檔案 !!");
//        }
//    }

}

