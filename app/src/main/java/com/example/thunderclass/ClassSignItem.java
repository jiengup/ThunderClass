package com.example.thunderclass;

public class ClassSignItem {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassOnTime() {
        return classOnTime;
    }

    public void setClassOnTime(String classOnTime) {
        this.classOnTime = classOnTime;
    }

    private int id;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private int num;
    private String classOnTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private String endTime;
}
