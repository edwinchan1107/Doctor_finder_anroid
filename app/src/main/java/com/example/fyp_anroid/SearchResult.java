package com.example.fyp_anroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchResult extends AppCompatActivity {
    private ListView listView;
    //    private ListAdapter listAdapter;
    private Context mContext;
    private List<doc_list> mData = null;
    private list_view_adapter mAdapter = null;
    ArrayList _id = new ArrayList();
    ArrayList name_chi = new ArrayList();
    ArrayList name_eng = new ArrayList();
    ArrayList price = new ArrayList();
    ArrayList location = new ArrayList();
    ArrayList location_region = new ArrayList();
    ArrayList  mark = new ArrayList();
    ArrayList image = new ArrayList();
    ArrayList gender = new ArrayList();
    ArrayList qualification = new ArrayList();
    ArrayList openTime = new ArrayList();
    ArrayList website = new ArrayList();
    ArrayList subjectList = new ArrayList();
    ArrayList infopath = new ArrayList();
    Button BackToSearchPanel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        listView = (ListView) findViewById(R.id.SearchResultList);
        BackToSearchPanel = (Button)findViewById(R.id.BackToSearchPanel);
        mContext = SearchResult.this;
        mData = new LinkedList<doc_list>();
        Intent i = getIntent();

        _id = i.getStringArrayListExtra("_id");
        name_eng = i.getStringArrayListExtra("name_eng");
        name_chi = i.getStringArrayListExtra("name_chi");
        location = i.getStringArrayListExtra("location");
        mark = i.getStringArrayListExtra("mark");

        for (int gg = 0; gg < name_eng.size(); gg++) {
            mData.add(new doc_list((String) _id.get(gg), (String) name_eng.get(gg),(String) name_chi.get(gg),(String) location.get(gg),(String)mark.get(gg) ));
        }
        mAdapter = new list_view_adapter((LinkedList<doc_list>) mData, mContext);
//        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, (ArrayList)contactList);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //----
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SearchResult.this);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("_id",(String)_id.get(position));
                editor.apply();
                Intent i = new Intent(SearchResult.this, DoctorPage.class);
                startActivity(i);
            }
        });

        BackToSearchPanel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                BackPage();
            }
        });

    }

    public void BackPage(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String backpage = preferences.getString("BackPage", null);
        if(backpage.equals("SearchPanel")){
            Intent intent = new Intent(this, SearchPanel.class);
            startActivity(intent);
        }else if(backpage.equals("HomePage")){
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }

    }
}
