package com.example.project;


import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Observable;

/**
 * Store information about a habit object
 */
public class Habit extends Observable{

    private String habitId;
    private String habitName;
    private String target;
    private String frequency;
    private Calendar startDate;
    private Calendar endDate;
    private int progress;
    private int maxProgress;

    public Habit(String id, String name,String target, Calendar start, Calendar end, String freq, int progress) {
        this.habitId = id;
        this.habitName = name;
        this.target=target;
        this.frequency = freq;
        this.startDate = start;
        this.endDate = end;
        this.progress = progress;
        this.setMaxProgress();
    }

    private void setMaxProgress() {
        this.maxProgress = 0;
//        long diffEpochMillis = Math.abs(this.endDate.getTime() - this.startDate.getTime());
//        long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
        int daysBetween = (int) ChronoUnit.DAYS.between(this.startDate.toInstant(), this.endDate.toInstant());
        if (this.frequency.equalsIgnoreCase("daily")) {
            this.maxProgress = daysBetween;
        } else if (this.frequency.equalsIgnoreCase("weekly")) {
            this.maxProgress = daysBetween / 7;
        } else {
            this.maxProgress = daysBetween / 28;
        }
    }

    public String getEndDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

// Convert the calendar to a string using the date format
        String dateString = dateFormat.format(this.endDate.getTime());
        return dateString;
    }

    public String getHabitId() {
        return this.habitId;
    }

    public String getHabitName() {
        return this.habitName;
    }

    public String getTarget() {
        return this.target;
    }

    public int getProgress() {
        return this.progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void incrementProgress() {
        this.progress += 1;
        setChanged();
        notifyObservers(this);
    }
    public void decrementProgress() {
        this.progress -= 1;
        setChanged();
        notifyObservers(this);
    }

}
