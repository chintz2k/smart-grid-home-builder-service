package com.homebuilder.exception;

import com.homebuilder.entity.SmartConsumerTimeslot;

import java.util.List;

/**
 * @author André Heinen
 */
public class TimeslotOverlapException extends RuntimeException {
    private final List<SmartConsumerTimeslot> overlappingTimeslots;

    public TimeslotOverlapException(String message, List<SmartConsumerTimeslot> overlappingTimeslots) {
        super(message);
        this.overlappingTimeslots = overlappingTimeslots;
    }

    public List<SmartConsumerTimeslot> getOverlappingTimeslots() {
        return overlappingTimeslots;
    }
}
