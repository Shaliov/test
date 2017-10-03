package com.inventain.test.model;

/**
 * @author Andrey
 */
public class BookingCalendar {

    private String workingTime;
    private String meetingTime;
    private String meetingHour;
    private String employerId;

    public BookingCalendar(String workingTime, String meetingTime, String meetingHour, String employerId) {
        this.workingTime = workingTime;
        this.meetingTime = meetingTime;
        this.meetingHour = meetingHour;
        this.employerId = employerId;
    }

    public BookingCalendar() {
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingHour() {
        return meetingHour;
    }

    public void setMeetingHour(String meetingHour) {
        this.meetingHour = meetingHour;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingCalendar that = (BookingCalendar) o;

        if (workingTime != null ? !workingTime.equals(that.workingTime) : that.workingTime != null) return false;
        if (meetingTime != null ? !meetingTime.equals(that.meetingTime) : that.meetingTime != null) return false;
        if (meetingHour != null ? !meetingHour.equals(that.meetingHour) : that.meetingHour != null) return false;
        return employerId != null ? employerId.equals(that.employerId) : that.employerId == null;
    }

    @Override
    public int hashCode() {
        int result = workingTime != null ? workingTime.hashCode() : 0;
        result = 31 * result + (meetingTime != null ? meetingTime.hashCode() : 0);
        result = 31 * result + (meetingHour != null ? meetingHour.hashCode() : 0);
        result = 31 * result + (employerId != null ? employerId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BookingCalendar{" +
                "workingTime='" + workingTime + '\'' +
                ", meetingTime='" + meetingTime + '\'' +
                ", meetingHour='" + meetingHour + '\'' +
                ", employerId='" + employerId + '\'' +
                '}';
    }
}
