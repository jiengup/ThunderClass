package com.example.thunderclass;

public class DiscussArray {
    int id;//话题编号
    String title;//标题
    String content;//内容
    String discussTime;//发布时间

    DiscussArray( int id,String title,String content,String discussTime){
        this.id=id;
        this.title=title;
        this.content=content;
        this.discussTime=discussTime;

    }

    String getTitle(){return title;}
    int getId(){return id;}
    String getDiscussTime(){return discussTime;}
    String getContent(){return content;}


}
