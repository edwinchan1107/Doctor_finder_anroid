package com.example.fyp_anroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

public class Comment_list_view_adapter extends BaseAdapter {
    private LinkedList<Comment_List> mData;
    private Context mContext;

    public Comment_list_view_adapter(LinkedList<Comment_List> mData, Context mContext) {
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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_comment_list_view_adapter,parent,false);


        TextView tv_Comment_ID=(TextView) convertView.findViewById(R.id.tv_CommentID);
        TextView tv_Comment = (TextView) convertView.findViewById(R.id.tv_Comment);
        TextView tv_Comment_CreateTime = (TextView) convertView.findViewById(R.id.tv_Comment_CreateTime);
        TextView tv_CreateUserName = (TextView) convertView.findViewById(R.id.tv_CreateUserName);
        TextView tv_CommentMark     = (TextView) convertView.findViewById(R.id.tv_CommentMark);
       // tv_Comment_ID.setText("CommentId : "+mData.get(position).getCommentId());
        //tv_Comment.setText("CommentDetail : "+mData.get(position).getComment());
        tv_Comment.setText(mData.get(position).getCreateUserName()+" : "+mData.get(position).getComment());
       // tv_Comment_CreateTime.setText("CreateTime : "+mData.get(position).getCreateTime());
        //tv_CreateUserName.setText("CreateUser : "+mData.get(position).getCreateUserName());
        tv_CommentMark.setText("Mark : "+mData.get(position).getCommentmark());



        return convertView;
    }
}

