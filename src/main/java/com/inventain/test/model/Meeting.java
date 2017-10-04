package com.inventain.test.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

/**
 * @author Andrey
 */
@Getter
@Setter
@ToString
public class Meeting {

    private DateTime startTime;
    private DateTime endTime;

    public Meeting() {
    }

    public Meeting(DateTime startTime, DateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
