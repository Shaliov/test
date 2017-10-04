package com.inventain.test.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Andrey
 */
@Getter
@Setter
@ToString
public class AnswerMeeting implements Serializable {

    private String startMeetingTime;
    private String endMeetingTime;
    private String employerId;

    public AnswerMeeting() {
    }

    public AnswerMeeting(String startMeetingTime, String endMeetingTime, String employerId) {
        this.startMeetingTime = startMeetingTime;
        this.endMeetingTime = endMeetingTime;
        this.employerId = employerId;
    }
}
