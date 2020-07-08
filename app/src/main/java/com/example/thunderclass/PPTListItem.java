package com.example.thunderclass;

public class PPTListItem {
    private int id;
    private String ppt_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPpt_name() {
        return ppt_name;
    }

    public void setPpt_name(String ppt_name) {
        this.ppt_name = ppt_name;
    }

    public String getPub_time() {
        return pub_time;
    }

    public void setPub_time(String pub_time) {
        this.pub_time = pub_time;
    }

    public String getPage1URL() {
        return page1URL;
    }

    public void setPage1URL(String page1URL) {
        this.page1URL = page1URL;
    }

    private String pub_time;
    private String page1URL;
}
