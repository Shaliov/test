package com.inventain.test.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.inventain.test.interfaces.Output;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

import javax.sound.sampled.Line;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey
 */
@Getter
@Setter
@ToString
public class Answer implements Serializable {

    @JsonView(Output.class)
    private String dateOfMeeting;
    @JsonView(Output.class)
    private List<AnswerMeeting> answerMeetingList;

    private DateTime dateOfSendingRequest;

    public Answer() {
        answerMeetingList = new ArrayList<>();
    }

    public Answer(String dateOfMeeting, List<AnswerMeeting> answerMeetingList, DateTime dateOfSendingRequest) {
        this.dateOfMeeting = dateOfMeeting;
        this.answerMeetingList = answerMeetingList;
        this.dateOfSendingRequest = dateOfSendingRequest;
    }
}
