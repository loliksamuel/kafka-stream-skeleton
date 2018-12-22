package com.kafka_stream_skeleton.model;

public class LoginData {
    String userName;
    String userPassword;
    String ip;
    Long  date;

    public LoginData() {
    }

    public LoginData(String userName, String userPassword, String ip, Long date) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.ip = ip;
        this.date = date;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", ip='" + ip + '\'' +
                ", date=" + date +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
