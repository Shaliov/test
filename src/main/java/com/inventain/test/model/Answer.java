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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;
        if (!dateOfMeeting.equals(answer.dateOfMeeting)) return false;
        if (answerMeetingList.size() != answer.answerMeetingList.size()) return false;
        for (int i = 0; i < answerMeetingList.size(); i++) {
            if (!answerMeetingList.get(i).equals(answer.getAnswerMeetingList().get(i))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = dateOfMeeting != null ? dateOfMeeting.hashCode() : 0;
        result = 31 * result + (answerMeetingList != null ? answerMeetingList.hashCode() : 0);
        result = 31 * result + (dateOfSendingRequest != null ? dateOfSendingRequest.hashCode() : 0);
        return result;
    }
}
