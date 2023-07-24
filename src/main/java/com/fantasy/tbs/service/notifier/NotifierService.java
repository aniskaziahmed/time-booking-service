package com.fantasy.tbs.service.notifier;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fantasy.tbs.domain.EmployeeDTO;
import com.fantasy.tbs.domain.TimeBooking;
import com.fantasy.tbs.exception.EmployeeException;
import com.fantasy.tbs.service.EmployeeService;
import com.fantasy.tbs.service.TimeBookingService;
import com.fantasy.tbs.util.DateTimeUtils;

@Service
public class NotifierService
{
    private static final int LAST_X_DAYS_BOOKINGS = 1;

    private EmployeeService employeeService;
    private TimeBookingService timeBookingService;
    private BookingNotifier bookingNotifier;

    public NotifierService(EmployeeService employeeService, TimeBookingService timeBookingService, BookingNotifier bookingNotifier)
    {
        this.employeeService = employeeService;
        this.timeBookingService = timeBookingService;
        this.bookingNotifier = bookingNotifier;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Execute at midnight (00:00:00) every day
    public void notifyMissingBookings() throws EmployeeException
    {
        List<EmployeeDTO> allEmployees = employeeService.getAllEmployees();
        if(CollectionUtils.isEmpty(allEmployees))
        {
            throw new EmployeeException("Could not get employee data");
        }
        Set<EmployeeDTO> employeesWithMissingBookings = new HashSet<>();
        Set<TimeBooking> yesterdaysBookings = getTimeBookingsForXDays();

        if (CollectionUtils.isEmpty(yesterdaysBookings))
        {
            employeesWithMissingBookings.addAll(allEmployees);
        }
        else
        {
            Set<String> employeesWithBookings = yesterdaysBookings.stream().map(TimeBooking::getPersonalNumber).collect(Collectors.toSet());
            allEmployees.stream()
                .filter(employee -> !employeesWithBookings.contains(employee.getPersonalNumber()))
                .forEach(employeesWithMissingBookings::add);
        }
        bookingNotifier.notifyMissingBookings(employeesWithMissingBookings);
    }

    private Set<TimeBooking> getTimeBookingsForXDays()
    {
        ZonedDateTime previousDayStart = DateTimeUtils.getZonedDateTimeStartOfDayXDaysAgo(LAST_X_DAYS_BOOKINGS);
        ZonedDateTime previousDayEnd =   DateTimeUtils.getZonedDateTimeEndOfDayXDaysAgo(LAST_X_DAYS_BOOKINGS).minusNanos(LAST_X_DAYS_BOOKINGS);
        Set<TimeBooking> yesterdaysBookings = timeBookingService.findTimeBookingsBetweenRange(previousDayStart, previousDayEnd);

        return CollectionUtils.isEmpty(yesterdaysBookings) ? Collections.emptySet() : yesterdaysBookings;

    }
}
