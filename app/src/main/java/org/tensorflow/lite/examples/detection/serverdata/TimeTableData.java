package org.tensorflow.lite.examples.detection.serverdata;

public class TimeTableData {
    private String day;
    private int numOfClass;

    public TimeTableData(String day, int numOfClass) {
        this.day = day;
        this.numOfClass = numOfClass;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getNumOfClass() {
        return numOfClass;
    }

    public void setNumOfClass(int numOfClass) {
        this.numOfClass = numOfClass;
    }
}
