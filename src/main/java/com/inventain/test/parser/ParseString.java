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

    private static Logger LOG = Logger.getLogger(ParseString.class);

    private final Pattern PATTERN_START_WORKING_TIME = Pattern.compile("[0-2][0-9][0-5][0-9]");
    private final Pattern PATTERN_YEAR_MONTH_DAY = Pattern.compile("[0-9]{4}-[0-1][0-9]-[0-3][0-9]");
    private final Pattern PATTERN_TIME = Pattern.compile("(([0-2][0-9]:[0-5][0-9]:[0-5][0-9])|([0-2][0-9]:[0-5][0-9]))");
    private final Pattern PATTERN_HOUR = Pattern.compile("[0-9]");
    private final Pattern PATTERN_EMPLOYEE_ID = Pattern.compile("EMP[0-9]{3}");

    private final String FORM_OF_TIME_HHmm = "HHmm";

    public Company parse(String message) throws NullPointerException {
        Company company = new Company();
        String[] temp = StringUtils.delimitedListToStringArray(message, " ");

        try {
            setCompany(company, temp);
        } catch (NumberFormatException e) {
            LOG.error(e);
            throw new NullPointerException("it's impossible to create the \"Company\" object");
        }

        List<RequestMeeting> requestMeetings = company.getRequestMeetings();
        for (int i = 2; i < temp.length; i++) {
            if (temp[i].equals("")) {
                try {
                    RequestMeeting requestMeeting = findRequestMeeting(i + 1, temp, company);
                    requestMeetings.add(requestMeeting);
                } catch (NullPointerException e) {
                    LOG.error(e);
                }
                i += 6;
            }
        }

        return company;
    }

    private void setCompany(Company company, String[] temp) throws NumberFormatException {
        if (doMatcher(PATTERN_START_WORKING_TIME, temp[0]) && doMatcher(PATTERN_START_WORKING_TIME, temp[1])) {
            company.setStartWorkingTime(temp[0]);
            company.setEndWorkingTime(temp[1]);
        } else {
            throw new NumberFormatException("Invalid time format for working time");
        }
    }

    private RequestMeeting findRequestMeeting(Integer startIndex, String[] stringArray, Company company) throws NullPointerException {
        RequestMeeting requestMeeting = new RequestMeeting();
        DateTime timeOfRequestSending;

        try {
            requestMeeting.setTimeOfRequestSending(getDateTime(startIndex, stringArray));
        } catch (NumberFormatException e) {
            LOG.error(e);
            throw new NullPointerException("can not set the time of request sending");
        }
        if (doMatcher(PATTERN_EMPLOYEE_ID, stringArray[startIndex + 2])) {
            requestMeeting.setEmployerId(stringArray[startIndex + 2]);
        } else {
            throw new NumberFormatException("Invalid EMPLOYEE_ID = " + stringArray[startIndex + 2]);
        }

        try {
            Meeting meeting = findMeeting(startIndex + 4, stringArray, company);
            requestMeeting.setMeeting(meeting);
        } catch (NullPointerException | NumberFormatException e) {
            LOG.error(e);
            throw new NullPointerException("error when creating a meeting");
        }

        return requestMeeting;
    }


    private Meeting findMeeting(Integer startIndex, String[] stringArray, Company company) throws NullPointerException, NumberFormatException {
        Meeting meeting = new Meeting();
        try {
            meeting.setStartTime(getDateTime(startIndex, stringArray));
        } catch (NumberFormatException e) {
            LOG.error(e);
            throw new NullPointerException("can not set the time of start time of the meeting");
        }
        String s = stringArray[startIndex + 2];
        if (doMatcher(PATTERN_HOUR, s)) {
            DateTime startTime = meeting.getStartTime();
            DateTime endTime = startTime.plusHours(Integer.parseInt(s));
            String endWorkingTime = company.getEndWorkingTime();
            String endMinuteTimeOFMeeting = endTime.toString(DateTimeFormat.forPattern(FORM_OF_TIME_HHmm));
            if (endTime.getHourOfDay() > Integer.parseInt(endWorkingTime.substring(0, 2)) ||
                    (endTime.getHourOfDay() == Integer.parseInt(endWorkingTime.substring(0, 2))
                            && Integer.parseInt(endMinuteTimeOFMeeting.substring(2)) > Integer.parseInt(endWorkingTime.substring(2)))) {
                throw new NullPointerException("The end of the meeting went beyond the working time of the company");
            } else {
                meeting.setEndTime(startTime.plusHours(Integer.parseInt(s)));
            }
        } else {
            throw new NumberFormatException("Invalid time format" + s);
        }

        return meeting;
    }

    private DateTime getDateTime(Integer startIndex, String[] stringArray) throws NumberFormatException {
        DateTime dateTime = new DateTime();
        if (doMatcher(PATTERN_YEAR_MONTH_DAY, stringArray[startIndex])) {
            if (doMatcher(PATTERN_TIME, stringArray[startIndex + 1])) {
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
                throw new NumberFormatException("the time of request sending is incorrect " + stringArray[startIndex + 1]);

            }

        } else {
            throw new NumberFormatException("the date of request sending is incorrect " + stringArray[startIndex]);

        }
        return dateTime;
    }

    private boolean doMatcher(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
