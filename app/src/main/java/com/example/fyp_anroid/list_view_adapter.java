package com.example.fyp_anroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

public class list_view_adapter extends BaseAdapter {
    private LinkedList<doc_list> mData;
    private Context mContext;

    public list_view_adapter(LinkedList<doc_list> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_list_view_adapter,parent,false);


        TextView tv_ID=(TextView) convertView.findViewById(R.id.tvID);
        TextView tv_Name_eng = (TextView) convertView.findViewById(R.id.tvName_eng);
        TextView tv_Name_chi = (TextView) convertView.findViewById(R.id.tvName_chi);
        TextView tv_location = (TextView) convertView.findViewById(R.id.tvlocation);
        TextView tv_mark     = (TextView) convertView.findViewById(R.id.tvmark);
        //tv_ID.setText("_id : "+mData.get(position).getId());
        tv_Name_eng.setText(mContext.getResources().getString(R.string.DoctorNameTv)+mData.get(position).getName_eng());
        tv_Name_chi.setText(mContext.getResources().getString(R.string.DoctorChiName)+mData.get(position).getName_chi());
        tv_location.setText(mContext.getResources().getString(R.string.location)+" : "+mData.get(position).getLocation());
        tv_mark.setText("Mark : "+mData.get(position).getMark());



        return convertView;
    }
}

