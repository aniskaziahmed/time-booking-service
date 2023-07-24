package com.fantasy.tbs.service.notifier.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.fantasy.tbs.domain.EmployeeDTO;
import com.fantasy.tbs.service.notifier.BookingNotifier;

@Component
public class BookingNotifierImplByEmail implements BookingNotifier
{

    @Override
    public void notifyMissingBookings(Set<EmployeeDTO> employees)
    {
        //TODO email implementation to send email to the HR about employees who forgot to book their working hours
    }

}
