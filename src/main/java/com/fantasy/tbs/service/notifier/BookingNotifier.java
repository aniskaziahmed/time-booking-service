package com.fantasy.tbs.service.notifier;

import java.util.Set;

import com.fantasy.tbs.domain.EmployeeDTO;

/**
 * Interface to provide different implementations for notifying of bookings
 */
public interface BookingNotifier
{
    void notifyMissingBookings(Set<EmployeeDTO> employees);
}
