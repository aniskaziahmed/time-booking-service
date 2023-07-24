package com.fantasy.tbs.service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fantasy.tbs.domain.EmployeeDTO;
import com.fantasy.tbs.domain.EmployeeWorkingHoursDTO;
import com.fantasy.tbs.domain.TimeBooking;
import com.fantasy.tbs.exception.EmployeeException;
import com.fantasy.tbs.util.DateTimeUtils;

@Service
public class TimeBookingCalculationService
{
    private static final Logger log = LoggerFactory.getLogger(TimeBookingCalculationService.class);

    private TimeBookingService timeBookingService;
    private EmployeeService employeeService;

    public TimeBookingCalculationService(TimeBookingService timeBookingService, EmployeeService employeeService)
    {
        this.timeBookingService = timeBookingService;
        this.employeeService = employeeService;
    }

    //TODO Depending on how many times this method is called, we can implement a cache for the employee.getAllEmployees to avoid calling the rest api multiple times.
    public List<EmployeeWorkingHoursDTO> calculateWorkingHours(LocalDateTime startDate, LocalDateTime endDate) throws EmployeeException
    {
        List<EmployeeWorkingHoursDTO> employeeWorkingHoursDTOS = new ArrayList<>();
        List<EmployeeDTO> allEmployees = employeeService.getAllEmployees();
        if (CollectionUtils.isEmpty(allEmployees))
        {
            throw new EmployeeException("Could not get employee data");
        }
        allEmployees.forEach(employee -> {
            double totalWorkingHours = calculateWorkingHoursForEmployee(employee.getPersonalNumber(), startDate, endDate);
            EmployeeWorkingHoursDTO employeeWorkingHoursDTO = new EmployeeWorkingHoursDTO();
            employeeWorkingHoursDTO.setEmployee(employee);
            employeeWorkingHoursDTO.setTotalWorkingHours(totalWorkingHours);
        });
        return employeeWorkingHoursDTOS;
    }

    public double calculateWorkingHoursForEmployee(String personalNumber, LocalDateTime startDate, LocalDateTime endDate)
    {
        //Convert to zonedDateTime to use same data types
        ZonedDateTime start = DateTimeUtils.convertToZonedDateTime(startDate);
        ZonedDateTime end = DateTimeUtils.convertToZonedDateTime(endDate);

        Set<TimeBooking> timeBookings = timeBookingService.findTimeBookingsByEmployeeAndBetweenRange(personalNumber, start, end);
        if (CollectionUtils.isEmpty(timeBookings))
        {
            log.info("No bookings found for employee {}", personalNumber);
            return 0d;
        }
        return timeBookings.stream().mapToDouble(TimeBooking::getWorkHours).sum();
    }

}
