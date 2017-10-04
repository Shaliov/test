package com.inventain.test.parser;

import com.inventain.test.model.Company;
import com.inventain.test.model.Meeting;
import com.inventain.test.model.RequestMeeting;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andrey
 */
// Написать коменты и пояснить зачем магические числа!!!
public class ParseString {

    private static Logger log = Logger.getLogger(ParseString.class);
    private Pattern patternStartEndWorkingTime = Pattern.compile("[0-2][0-9][0-5][0-9]");
    private Pattern yearMonthDay = Pattern.compile("[0-9]{4}-[0-1][0-9]-[0-3][0-9]");
    private Pattern time = Pattern.compile("(([0-2][0-9]:[0-5][0-9]:[0-5][0-9])|([0-2][0-9]:[0-5][0-9]))");
    private Pattern hour = Pattern.compile("[0-9]");

    public Company parse(String message) {
        Company company = new Company();
        String[] temp = StringUtils.delimitedListToStringArray(message, " ");


        if (doMatcher(patternStartEndWorkingTime, temp[0]) && doMatcher(patternStartEndWorkingTime, temp[1])) {
            company.setStartWorkingTime(temp[0]);
            company.setEndWorkingTime(temp[1]);
        } else {
            log.error("the company office hours are incorrect"); // ошибка
        }

        List<RequestMeeting> requestMeetings = company.getRequestMeetings();
        for (int i = 2; i < temp.length; i++) {
            if (temp[i].equals("")) {
                RequestMeeting requestMeeting = findRequestMeeting(i + 1, temp, company);
                if (requestMeeting != null) {
                    requestMeetings.add(requestMeeting);
                }
                i += 6;
            }
        }

        return company;
    }

    private RequestMeeting findRequestMeeting(Integer startIndex, String[] stringArray, Company company) {
        RequestMeeting requestMeeting = new RequestMeeting();
        DateTime timeOfRequestSending;

        Pattern employId = Pattern.compile("EMP[0-9]{3}");
        requestMeeting.setTimeOfRequestSending(getDateTime(startIndex, stringArray));
        if (doMatcher(employId, stringArray[startIndex + 2])) {
            requestMeeting.setEmployerId(stringArray[startIndex + 2]);
        }

        Meeting meeting = findMeeting(startIndex + 4, stringArray, company);
        if (meeting != null) {
            requestMeeting.setMeeting(meeting);
        }  else {
            return null;
        }
        return requestMeeting;
    }


    private Meeting findMeeting(Integer startIndex, String[] stringArray, Company company) {
        Meeting meeting = new Meeting();
        meeting.setStartTime(getDateTime(startIndex, stringArray));
        String s = stringArray[startIndex + 2];
        if (doMatcher(hour, s)) {
            DateTime startTime = meeting.getStartTime();
            DateTime endTime = startTime.plusHours(Integer.parseInt(s));
            String endWorkingTime = company.getEndWorkingTime();
            String endMinuteTimeOFMeeting = endTime.toString(DateTimeFormat.forPattern("HHmm"));
            if (endTime.getHourOfDay() > Integer.parseInt(endWorkingTime.substring(0,2)) ||
                    (endTime.getHourOfDay() == Integer.parseInt(endWorkingTime.substring(0,2))
                            && Integer.parseInt(endMinuteTimeOFMeeting.substring(2)) > Integer.parseInt(endWorkingTime.substring(2))) ) {
                return null; // ошибка
            } else {
                meeting.setEndTime(startTime.plusHours(Integer.parseInt(s)));
            }
        } else {
            /// ошибка
        }

        return meeting;
    }

    private DateTime getDateTime(Integer startIndex, String[] stringArray) {
        DateTime dateTime = new DateTime();
        if (doMatcher(yearMonthDay, stringArray[startIndex])) {
            if (doMatcher(time, stringArray[startIndex + 1])) {
                int year = Integer.parseInt(stringArray[startIndex].substring(0, 4));
                int month = Integer.parseInt(stringArray[startIndex].substring(5, 7));
                int day = Integer.parseInt(stringArray[startIndex].substring(8, 10));
                int hour = Integer.parseInt(stringArray[startIndex + 1].substring(0, 2));
                int minutes = Integer.parseInt(stringArray[startIndex + 1].substring(3, 5));
                if (stringArray[startIndex + 1].length() > 6) {
                    int seconds = Integer.parseInt(stringArray[startIndex + 1].substring(6, 8));
                    dateTime = new DateTime(year, month, day, hour, minutes, seconds);
                } else {
                    dateTime = new DateTime(year, month, day, hour, minutes, 0);
                }
            } else {
                log.error("the time of request sending is incorrect"); // тут просто будет переход на ошибку

            }

        } else {
            log.error("the date of request sending is incorrect"); // тут просто будет переход на ошибку

        }
        return dateTime;
    }

    private boolean doMatcher(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
