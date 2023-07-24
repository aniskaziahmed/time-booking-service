package com.fantasy.tbs.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fantasy.tbs.domain.EmployeeDTO;
import com.fantasy.tbs.domain.TimeBooking;
import com.fantasy.tbs.exception.EmployeeException;
import com.fantasy.tbs.service.impl.TimeBookingServiceImpl;
import com.fantasy.tbs.util.DateTimeUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimeBookingCalculationServiceTest
{
    private static final String PERSONAL_NUMBER = "TEST_EMPLOYEE_123";
    @InjectMocks
    private TimeBookingCalculationService timeBookingCalculationService;

    @Mock
    private TimeBookingService timeBookingService;
    @Mock
    private EmployeeService employeeService;

    @Test
    public void shouldGet0WorkingHours_WhenEmployeeBookingIsNotPresent()
    {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        when(timeBookingService.findTimeBookingsByEmployeeAndBetweenRange(eq(PERSONAL_NUMBER), any(), any())).thenReturn(Collections.emptySet());
        double totalWorkingHours = timeBookingCalculationService.calculateWorkingHoursForEmployee(PERSONAL_NUMBER, startDate, endDate);

        verify(timeBookingService).findTimeBookingsByEmployeeAndBetweenRange(eq(PERSONAL_NUMBER), any(), any());
        assertEquals(0, totalWorkingHours);
    }

    @Test
    public void shouldGetTotalWorkingHours_WhenEmployeeBookingIsPresent()
    {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        TimeBooking timeBooking = buildTimeBookings();
        when(timeBookingService.findTimeBookingsByEmployeeAndBetweenRange(eq(PERSONAL_NUMBER), any(), any())).thenReturn(
            Collections.singleton(timeBooking));
        double totalWorkingHours = timeBookingCalculationService.calculateWorkingHoursForEmployee(PERSONAL_NUMBER, startDate, endDate);

        verify(timeBookingService).findTimeBookingsByEmployeeAndBetweenRange(eq(PERSONAL_NUMBER), any(), any());
        assertEquals(8, totalWorkingHours);
    }

    @Test
    public void shouldGetTotalWorkingHoursForAllEmployees_WhenEmployeeBookingsArePresent()
    {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);
        TimeBooking timeBooking = buildTimeBookings();
        TimeBooking timeBooking2 = buildTimeBookings();
        timeBooking2.setWorkHours(6d);
        Set<TimeBooking> timeBookings = Set.of(timeBooking, timeBooking2);
        EmployeeDTO employeeDTO = buildEmployeeDTO();

        when(timeBookingService.findTimeBookingsByEmployeeAndBetweenRange(eq(PERSONAL_NUMBER), any(), any())).thenReturn(timeBookings);
        double totalWorkingHours = timeBookingCalculationService.calculateWorkingHoursForEmployee(PERSONAL_NUMBER, startDate, endDate);

        verify(timeBookingService).findTimeBookingsByEmployeeAndBetweenRange(eq(PERSONAL_NUMBER), any(), any());
        assertEquals(timeBooking.getWorkHours() + timeBooking2.getWorkHours(), totalWorkingHours);
    }

    @Test
    public void shouldThrowException_WhenFailedToGetEmployees()
    {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().minusDays(1);

        when(employeeService.getAllEmployees()).thenReturn(null);

        verify(timeBookingService, never()).findTimeBookingsByEmployeeAndBetweenRange(eq(PERSONAL_NUMBER), any(), any());
        assertThrows(EmployeeException.class,
            () -> timeBookingCalculationService.calculateWorkingHours(startDate, endDate));
    }

    private TimeBooking buildTimeBookings()
    {
        TimeBooking timeBooking = new TimeBooking();
        timeBooking.setBooking(DateTimeUtils.getZonedDateTimeEndOfDayXDaysAgo(1));
        timeBooking.setPersonalNumber(PERSONAL_NUMBER);
        timeBooking.setWorkHours(8d);
        return timeBooking;
    }

    private EmployeeDTO buildEmployeeDTO()
    {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("TEST_EMPLOYEE");
        employeeDTO.setPersonalNumber(PERSONAL_NUMBER);
        return employeeDTO;
    }

}
