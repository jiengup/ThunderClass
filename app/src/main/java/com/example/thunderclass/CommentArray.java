package com.example.thunderclass;

public class CommentArray {
    int id;//评论
    String content;//评论内容
    String usename;//评论者用户名
    String nickname;//评论者昵称
    String commentTime;//评论时间
    String portrait_url;
    int starNumber;//点赞数

    CommentArray(int id,String usename,String nickname,String commentTime,String content,int starNumber, String portrait_url){
        this.id=id;
        this.content=content;
        this.usename=usename;
        this.nickname=nickname;
        this.commentTime=commentTime;
        this.starNumber=starNumber;
        this.portrait_url = portrait_url;
    }

    int getId(){return id;}
    String getContent(){return content;}
    String getUsename(){return usename;}
    String getNickname(){return nickname;}
    int getStarNumber(){return starNumber;}
    String getCommentTime(){return commentTime;}
    String getPortrait_url() {return portrait_url;}
}
