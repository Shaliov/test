package com.inventain.test.controller;

import com.inventain.test.model.*;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey
 */
public class PreparingAnswerController {

    private final String FORM_OF_TIME_yyyyMMdd = "yyyy-MM-dd";
    private final String FORM_OF_TIME_HHmm = "HH:mm";

    public List<Answer> getAnswer(Company company) {
        List<Answer> answerList = new ArrayList<>();
        List<RequestMeeting> requestMeetings = company.getRequestMeetings();
        temp:
        for (RequestMeeting requestMeeting : requestMeetings) {
            Meeting meeting = requestMeeting.getMeeting();
            for (Answer answer : answerList) {
                String dateAsString = meeting.getStartTime().toString(DateTimeFormat.forPattern(FORM_OF_TIME_yyyyMMdd));
                if (answer.getDateOfMeeting().equals(dateAsString)) {
                    if (answer.getDateOfSendingRequest().isAfter(requestMeeting.getTimeOfRequestSending())) {
                        updateAnswer(answer, requestMeeting, meeting);
                    } else {
                        addAnswer(answer, requestMeeting, meeting);
                    }
                    continue temp;
                }
            }
            Answer answer = new Answer();
            String dateAsString = meeting.getStartTime().toString(DateTimeFormat.forPattern(FORM_OF_TIME_yyyyMMdd));
            answer.setDateOfMeeting(dateAsString);
            addAnswer(answer, requestMeeting, meeting);
            answerList.add(answer);

        }

        return answerList;
    }



    private void updateAnswer(Answer answer, RequestMeeting requestMeeting, Meeting meeting) {
        for (AnswerMeeting answerMeeting : answer.getAnswerMeetingList()) {
            String startMeetingTimeAsString = meeting.getStartTime().toString(DateTimeFormat.forPattern(FORM_OF_TIME_HHmm));
            String endMeetingTimeAsString = meeting.getEndTime().toString(DateTimeFormat.forPattern(FORM_OF_TIME_HHmm));
            if (answerMeeting.getStartMeetingTime().equals(startMeetingTimeAsString)) {
                answerMeeting.setEmployerId(requestMeeting.getEmployerId());
                answerMeeting.setStartMeetingTime(startMeetingTimeAsString);
                answerMeeting.setEndMeetingTime(endMeetingTimeAsString);
                return;
            }
        }
    }

    private void addAnswer(Answer answer, RequestMeeting requestMeeting, Meeting meeting) {
        AnswerMeeting answerMeeting = new AnswerMeeting();
        answerMeeting.setEmployerId(requestMeeting.getEmployerId());
        String startTimeAsString = meeting.getStartTime().toString(DateTimeFormat.forPattern(FORM_OF_TIME_HHmm));
        answerMeeting.setStartMeetingTime(startTimeAsString);
        String endTimeAsString = meeting.getEndTime().toString(DateTimeFormat.forPattern(FORM_OF_TIME_HHmm));
        answerMeeting.setEndMeetingTime(endTimeAsString);
        answer.setDateOfSendingRequest(requestMeeting.getTimeOfRequestSending());
        answer.getAnswerMeetingList().add(answerMeeting);
    }

}
