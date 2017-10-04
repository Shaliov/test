package com.inventain.test.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.inventain.test.interfaces.Output;
import com.inventain.test.model.Answer;
import com.inventain.test.model.Company;
import com.inventain.test.parser.ParseString;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Andrey
 */
@RestController
@RequestMapping(value = "/booking/")
public class MainController {

    private static Logger LOG = Logger.getLogger(MainController.class);


    @JsonView(value = Output.class)
    @RequestMapping(value = "/{message}", method = RequestMethod.GET)
    public List<Answer> getBookingCalendarInJSON(@PathVariable String message) {
        Company company = new Company();
        ParseString parseString = new ParseString();
        try {
             company = parseString.parse(message);

        } catch (NullPointerException e) {
            LOG.error("Something went wrong" + e);
        }
        PreparingAnswerController preparingAnswerController = new PreparingAnswerController();
        return preparingAnswerController.getAnswer(company);
    }



}
