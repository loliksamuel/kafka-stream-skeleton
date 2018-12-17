package com.kafka_stream_skeleton.model;

public class LoginCount {

    private String userName;
    private Long count;
    private Long windowStart;
    private Long windowEnd  ;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(Long windowStart) {
        this.windowStart = windowStart;
    }

    public Long getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Long windowEnd) {
        this.windowEnd = windowEnd;
    }

    public LoginCount(String userName, Long count, Long windowStart, Long windowEnd) {
        this.userName = userName;
        this.count = count;
        this.windowStart = windowStart;
        this.windowEnd = windowEnd;
    }

    public LoginCount() {
    }

    @Override
    public String toString() {
        return "LoginCount{" +
                "userName='" + userName + '\'' +
                ", count=" + count +
                ", windowStart=" + windowStart +
                ", windowEnd=" + windowEnd +
                '}';
    }
}
