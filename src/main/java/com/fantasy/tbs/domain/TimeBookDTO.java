package com.fantasy.tbs.domain;

import java.time.ZonedDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TimeBookDTO
{
    @NotNull
    private ZonedDateTime timeStamp;
    @Min(value = 0, message = "Value cannot be less then 0")
    private double workHours;
    @NotEmpty
    private String personalNumber;

    public ZonedDateTime getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(ZonedDateTime timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getPersonalNumber()
    {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber)
    {
        this.personalNumber = personalNumber;
    }

    public double getWorkHours()
    {
        return workHours;
    }

    public void setWorkHours(double workHours)
    {
        this.workHours = workHours;
    }
}
