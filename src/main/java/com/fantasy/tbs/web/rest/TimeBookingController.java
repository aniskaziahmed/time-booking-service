package com.fantasy.tbs.web.rest;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fantasy.tbs.domain.EmployeeWorkingHoursDTO;
import com.fantasy.tbs.domain.TimeBookDTO;
import com.fantasy.tbs.service.TimeBookingCalculationService;
import com.fantasy.tbs.service.impl.TimeBookingServiceImpl;

@RestController
@RequestMapping("/api")
public class TimeBookingController
{

    private final TimeBookingServiceImpl timeBookingService;
    private final TimeBookingCalculationService timeBookingCalculationService;

    public TimeBookingController(TimeBookingServiceImpl timeBookingService, TimeBookingCalculationService timeBookingCalculationService)
    {
        this.timeBookingService = timeBookingService;
        this.timeBookingCalculationService = timeBookingCalculationService;
    }

    //Added validation to make sure inputs are correct
    @PostMapping("/book")
    public ResponseEntity<Void> addTimeBooking(@RequestBody @Valid TimeBookDTO timeBookDTO)
    {
        timeBookingService.bookTime(timeBookDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/employees/work-hours")
    public ResponseEntity<List<EmployeeWorkingHoursDTO>> getTotalWorkingHoursForAllEmployees(@RequestParam LocalDateTime startDate,
        @RequestParam LocalDateTime endDate)
    {
        List<EmployeeWorkingHoursDTO> allEmployeesWorkingHours = timeBookingCalculationService.calculateWorkingHours(startDate, endDate);
        return ResponseEntity.ok(allEmployeesWorkingHours);
    }

    @GetMapping("/employees/{personalNumber}/work-hours")
    public ResponseEntity<Double> getTotalWorkingHoursForEmployee(@PathVariable String personalNumber, @RequestParam LocalDateTime startDate,
        @RequestParam LocalDateTime endDate)
    {
        double workingHours = timeBookingCalculationService.calculateWorkingHoursForEmployee(personalNumber, startDate, endDate);
        //in case no bookings are found, response code should be 200 with 0 hours.
        return ResponseEntity.ok(workingHours);
    }
}
