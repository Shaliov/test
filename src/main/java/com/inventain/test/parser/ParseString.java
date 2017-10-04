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
/**
 * A class which allows you to parse a String
 */
public class ParseString {

    private static Logger LOG = Logger.getLogger(ParseString.class);

    private final Pattern PATTERN_START_WORKING_TIME = Pattern.compile("[0-2][0-9][0-5][0-9]");
    private final Pattern PATTERN_YEAR_MONTH_DAY = Pattern.compile("[0-9]{4}-[0-1][0-9]-[0-3][0-9]");
    private final Pattern PATTERN_TIME = Pattern.compile("(([0-2][0-9]:[0-5][0-9]:[0-5][0-9])|([0-2][0-9]:[0-5][0-9]))");
    private final Pattern PATTERN_HOUR = Pattern.compile("[0-9]");
    private final Pattern PATTERN_EMPLOYEE_ID = Pattern.compile("EMP[0-9]{3}");

    private final String FORM_OF_TIME_HHmm = "HHmm";

    /**
     * method that starts the parsing procedure
     * @param message - Message to be parsed
     * @return company
     */
    public Company parse(String message) throws NullPointerException {
        Company company = new Company();
        String[] arrayStrings = StringUtils.delimitedListToStringArray(message, " ");

        try {
            setStartAndEndWorkingHoursOfCompany(company, arrayStrings);
        } catch (NumberFormatException e) {
            LOG.error(e);
            throw new NullPointerException("it's impossible to create the \"Company\" object");
        }

        List<RequestMeeting> requestMeetings = company.getRequestMeetings();
        for (int i = 2; i < arrayStrings.length; i++) {
            if (arrayStrings[i].equals("")) {
                try {
                    RequestMeeting requestMeeting = findRequestMeeting(i + 1, arrayStrings, company);
                    requestMeetings.add(requestMeeting);
                } catch (NullPointerException | NumberFormatException e) {
                    LOG.error(e);
                }
                i += 6;
            }
        }

        return company;
    }

    /**
     * sets the start and end time of working for the company
     * @param company - company for which time is set
     * @param arrayStrings - array of String from which time will be taken
     * @exception NumberFormatException when the meeting time format is incorrect
     */
    private void setStartAndEndWorkingHoursOfCompany(Company company, String[] arrayStrings) throws NumberFormatException {
        if (doMatcher(PATTERN_START_WORKING_TIME, arrayStrings[0]) && doMatcher(PATTERN_START_WORKING_TIME, arrayStrings[1])) {
            company.setStartWorkingTime(arrayStrings[0]);
            company.setEndWorkingTime(arrayStrings[1]);
        } else {
            throw new NumberFormatException("Invalid time format for working time");
        }
    }

    /**
     * looking for the request of meeting in stringArray
     * @param company - company for which time is set
     * @param startIndex - indicates the starting index for stringArray
     * @param stringArray - array of String from
     * @param company - company for which request will find
     * @exception NumberFormatException when the time of request sending of meeting is incorrect
     * @exception NullPointerException when can not set the time of request sending or when something wrong with meeting
     * @return requestMeeting
     */
    private RequestMeeting findRequestMeeting(Integer startIndex, String[] stringArray, Company company) throws NullPointerException, NumberFormatException {
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

    /**
     * looking for the meeting in stringArray
     * @param startIndex - indicates the starting index for stringArray
     * @param stringArray - array of String from
     * @param company - company for which request will find
     * @exception NumberFormatException when invalid time format
     * @exception NullPointerException when can not set the time oof start time of the meeting or when the end of the meeting went beyond the working time of the company
     * @return meeting
     */
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
            String endTimeOFMeeting = endTime.toString(DateTimeFormat.forPattern(FORM_OF_TIME_HHmm));
            if (endTime.getHourOfDay() > Integer.parseInt(endWorkingTime.substring(0, 2)) ||
                    (endTime.getHourOfDay() == Integer.parseInt(endWorkingTime.substring(0, 2))
                            && Integer.parseInt(endTimeOFMeeting.substring(2)) > Integer.parseInt(endWorkingTime.substring(2)))) {
                throw new NullPointerException("The end of the meeting went beyond the working time of the company");
            } else {
                meeting.setEndTime(startTime.plusHours(Integer.parseInt(s)));
            }
        } else {
            throw new NumberFormatException("Invalid time format" + s);
        }

        return meeting;
    }

    /**
     * for getting time from stringArray
     * @param startIndex - indicates the starting index for stringArray
     * @param stringArray - array of String from
     * @exception NumberFormatException when the time of request sending is incorrect or the date of request sending is incorrect
     * @return dateTime
     */
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