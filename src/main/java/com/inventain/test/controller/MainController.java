package com.inventain.test.controller;

import com.inventain.test.model.*;
import com.inventain.test.parser.ParseString;
import org.joda.time.format.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey
 */
@RestController
@RequestMapping(value = "/booking/")
public class MainController {

    @RequestMapping(value = "/{message}", method = RequestMethod.GET)
    public List<Answer> getBookingCalendarInJSON(@PathVariable String message) {

        ParseString parseString = new ParseString();
        Company company = parseString.parse(message);
        List<Answer> answerList = new ArrayList<>();
        List<RequestMeeting> requestMeetings = company.getRequestMeetings();
        temp : for (RequestMeeting requestMeeting : requestMeetings) {
            Meeting meeting = requestMeeting.getMeeting();
            for (Answer answer : answerList) {
               String dateAsString = meeting.getStartTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd"));
                if (answer.getDateOfMeeting().equals(dateAsString)) {
                    addAnswer(answer, requestMeeting, meeting);
                    continue temp;
                }
            }
            Answer answer = new Answer();
            String dateAsString = meeting.getStartTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd"));
            answer.setDateOfMeeting(dateAsString);
            addAnswer(answer, requestMeeting, meeting);
            answerList.add(answer);

        }
        return answerList;
    }

    private void addAnswer(Answer answer, RequestMeeting requestMeeting, Meeting meeting){
        AnswerMeeting answerMeeting = new AnswerMeeting();
        answerMeeting.setEmployerId(requestMeeting.getEmployerId());
        String startTimeAsString = meeting.getStartTime().toString(DateTimeFormat.forPattern("HH:mm"));
        answerMeeting.setStartMeetingTime(startTimeAsString);
        String endTimeAsString = meeting.getEndTime().toString(DateTimeFormat.forPattern("HH:mm"));
        answerMeeting.setEndMeetingTime(endTimeAsString);
        answer.getAnswerMeetingList().add(answerMeeting);
    }




}
