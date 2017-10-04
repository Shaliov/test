package com.inventain.test.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey
 */
@Getter
@Setter
@ToString
public class Company implements Serializable {

    private String startWorkingTime;
    private String endWorkingTime;
    private List<RequestMeeting> requestMeetings;

    public Company() {
        requestMeetings = new ArrayList<>();
    }

    public Company(String startWorkingTime, String endWorkingTime, List<RequestMeeting> requestMeetings) {
        this.startWorkingTime = startWorkingTime;
        this.endWorkingTime = endWorkingTime;
        this.requestMeetings = requestMeetings;
    }

}
