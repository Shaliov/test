import com.inventain.test.controller.MainController;
import com.inventain.test.model.Answer;
import com.inventain.test.model.AnswerMeeting;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrey
 */
public class MainControllerTest extends Assert {

    private MainController mainController;
    private String testMessage;
    private List<Answer> result;

    @Before
    public void init() {
        mainController = new MainController();
        testMessage = "0900 1730 " +
                " 2011-03-17 10:17:06 EMP001 " +
                " 2011-03-21 09:00 2 " +
                " 2011-03-16 12:34:56 EMP002 " +
                " 2011-03-21 09:00 2 " +
                " 2011-03-16 09:28:23 EMP003 " +
                " 2011-03-22 14:00 2 " +
                " 2011-03-17 11:23:45 EMP004 " +
                " 2011-03-22 16:00 1 " +
                " 2011-03-15 17:29:12 EMP005 " +
                " 2011-03-21 16:00 3";
        result = initAnswerList();
    }

    private List<Answer> initAnswerList() {
        List<Answer> answerList = new ArrayList<>();
        Answer answer = initAnswer("2011-03-21");

        List<AnswerMeeting> answerMeetingList = new ArrayList<>();

        answerMeetingList.add(initAnswerMeeting( "09:00", "11:00", "EMP002"));
        answer.setAnswerMeetingList(answerMeetingList);

        Answer answer1 = initAnswer("2011-03-22");
        answerMeetingList = new ArrayList<>();

        answerMeetingList.add(initAnswerMeeting( "14:00", "16:00", "EMP003"));
        answerMeetingList.add(initAnswerMeeting( "16:00", "17:00", "EMP004"));
        answer1.setAnswerMeetingList(answerMeetingList);

        answerList.add(answer);
        answerList.add(answer1);

        return answerList;
    }

    private Answer initAnswer(String dateOfMeeting) {
        Answer answer = new Answer();
        answer.setDateOfMeeting(dateOfMeeting);



        return answer;
    }

    private AnswerMeeting initAnswerMeeting(String startMeetingTime, String endMeetingTime, String employerId) {
        AnswerMeeting answerMeeting = new AnswerMeeting();
        answerMeeting.setStartMeetingTime(startMeetingTime);
        answerMeeting.setEndMeetingTime(endMeetingTime);
        answerMeeting.setEmployerId(employerId);

        return answerMeeting;
    }

    @After
    public void destroy() {
    }

    @Test
    public void parseTest() {
        List<Answer> answerList = mainController.getBookingCalendarInJSON(testMessage);
        assertEquals(answerList,  result);
    }
}
