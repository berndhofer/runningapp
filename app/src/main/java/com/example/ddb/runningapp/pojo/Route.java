package com.example.ddb.runningapp.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Route {
    private Date date, duration;
    private int no;
    private float distance;


    public Route(Date date, int no, float distance, Date duration) {
        this.date = date;
        this.no = no;
        this.distance=distance;
        this.duration = duration;

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getFormattedDate(Date d){
        return new SimpleDateFormat("hh:mm dd.MM.yyyy ").format(d);
    }

    public String getDur(){
        if(duration!=null && date!=null) {
            long diff = duration.getTime() - date.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            return diffDays + ":" + diffHours + ":" + diffMinutes + ":" + diffSeconds;
        } else return "";
    }

    @Override
    public String toString() {
        System.out.println(getDur());
        return
                new SimpleDateFormat("HH:mm dd.MM.yyyy ").format(date) + " Distance: " + distance
                + " Duration: " + getDur();

    }


}
