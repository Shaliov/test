package com.inventain.test.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

/**
 * @author Andrey
 */
@Getter
@Setter
@ToString
public class RequestMeeting {

    private DateTime timeOfRequestSending;
    private String employerId;
    private Meeting meeting;

    public RequestMeeting() {
    }

    public RequestMeeting(DateTime timeOfRequestSending, String employerId, Meeting meeting) {
        this.timeOfRequestSending = timeOfRequestSending;
        this.employerId = employerId;
        this.meeting = meeting;
    }
}
