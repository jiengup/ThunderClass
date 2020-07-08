package com.example.thunderclass;

public class SignupTableItem {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(String deltaTime) {
        this.deltaTime = deltaTime;
    }

    private String nickname;
    private String enterTime;
    private String leaveTime;
    private String deltaTime;
}
