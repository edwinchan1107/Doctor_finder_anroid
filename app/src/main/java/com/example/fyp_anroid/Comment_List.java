package com.example.fyp_anroid;

public class Comment_List {
    private String CommentId;

    private String Comment;
    private String CreateTime;
    private String CreateUserName;
    private String Commentmark;
    public Comment_List(String CommentId, String Comment, String CreateTime, String CreateUserName, String Commentmark) {
        this.CommentId = CommentId;
        this.Comment = Comment;
        this.CreateTime = CreateTime;
        this.CreateUserName = CreateUserName;
        this.Commentmark = Commentmark;
    }

    public String getCommentId() {
        return CommentId;
    }

    public void setCommentId(String id) {
        this.CommentId = id;
    }

    public String getComment() {
        return Comment;
    }

    public void setCreateTime(String createTime) {
        this.CreateTime = createTime;
    }
    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateUserName(String createUserName) {
        this.CreateUserName = createUserName;
    }
    public String getCreateUserName() {
        return CreateUserName;
    }

    public void setComment(String comment) {
        this.Comment = comment;
    }
    public String getCommentmark(){
        return this.Commentmark;
    }

    public void setCommentmark(String commentmark) {
        this.Commentmark = commentmark;
    }
}
