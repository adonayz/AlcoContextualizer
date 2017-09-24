package edu.wpi.alcoholcontextualizer.model;

/**
 * Created by tupac on 1/24/2017.
 */

public class Time {

    private int timeId;

    /* month name : "January" ..  "December" */
    private String month;

    /* day in a month : 1,2 .. 31 */
    private int day;

    /* hour : 0 .. 24 */
    private int hour;

    /* Amount of standard drinks */
    private int drinkAmount;

    public Time(int id, String month, int day, int hour, int amountOfDrinks) {
        this.timeId = id;
        this.drinkAmount = amountOfDrinks;
        this.day = day;
        this.hour = hour;
        this.month = month;
    }

    public Time() {
        this.month = "";
        this.day = 0;
        this.hour = 0;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public int getDrinkAmount() {
        return drinkAmount;
    }

    public void setDrinkAmount(int drinkAmount) {
        this.drinkAmount = drinkAmount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
