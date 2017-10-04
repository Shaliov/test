package com.inventain.test.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.inventain.test.interfaces.Output;
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

    @JsonView(Output.class)
    private String startMeetingTime;
    @JsonView(Output.class)
    private String endMeetingTime;
    @JsonView(Output.class)
    private String employerId;

    public AnswerMeeting() {
    }

    public AnswerMeeting(String startMeetingTime, String endMeetingTime, String employerId) {
        this.startMeetingTime = startMeetingTime;
        this.endMeetingTime = endMeetingTime;
        this.employerId = employerId;
    }
}
