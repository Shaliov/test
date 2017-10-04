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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerMeeting answerMeeting = (AnswerMeeting) o;
        if (!startMeetingTime.equals(answerMeeting.getStartMeetingTime())) return false;
        if (!endMeetingTime.equals(answerMeeting.getEndMeetingTime())) return false;
        if (!employerId.equals(answerMeeting.getEmployerId())) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = startMeetingTime != null ? startMeetingTime.hashCode() : 0;
        result = 31 * result + (endMeetingTime != null ? endMeetingTime.hashCode() : 0);
        result = 31 * result + (employerId != null ? employerId.hashCode() : 0);
        return result;
    }
}
