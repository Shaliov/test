package com.inventain.test.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    private String dateOfMeeting;
    private List<AnswerMeeting> answerMeetingList;

    public Answer() {
        answerMeetingList = new ArrayList<>();
    }

    public Answer(String dateOfMeeting, List<AnswerMeeting> answerMeetingList) {
        this.dateOfMeeting = dateOfMeeting;
        this.answerMeetingList = answerMeetingList;
    }
}
