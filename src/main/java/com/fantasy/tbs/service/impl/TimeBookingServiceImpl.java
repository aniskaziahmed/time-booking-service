package com.fantasy.tbs.service.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fantasy.tbs.domain.TimeBookDTO;
import com.fantasy.tbs.domain.TimeBooking;
import com.fantasy.tbs.repository.TimeBookingRepository;
import com.fantasy.tbs.service.TimeBookingService;
import com.fantasy.tbs.service.mapper.TimeBookMapper;

/**
 * Service Implementation for managing {@link TimeBooking}.
 */
@Service
@Transactional
public class TimeBookingServiceImpl implements TimeBookingService {

    private final Logger log = LoggerFactory.getLogger(TimeBookingServiceImpl.class);

    private final TimeBookingRepository timeBookingRepository;
    private final TimeBookMapper timeBookMapper;

    public TimeBookingServiceImpl(TimeBookingRepository timeBookingRepository, TimeBookMapper timeBookMapper) {
        this.timeBookingRepository = timeBookingRepository;
        this.timeBookMapper = timeBookMapper;
    }

    @Override
    public TimeBooking save(TimeBooking timeBooking) {
        log.debug("Request to save TimeBooking : {}", timeBooking);
        return timeBookingRepository.save(timeBooking);
    }

    @Override
    public Optional<TimeBooking> partialUpdate(TimeBooking timeBooking) {
        log.debug("Request to partially update TimeBooking : {}", timeBooking);

        return timeBookingRepository
            .findById(timeBooking.getId())
            .map(
                existingTimeBooking -> {
                    if (timeBooking.getBooking() != null) {
                        existingTimeBooking.setBooking(timeBooking.getBooking());
                    }
                    if (timeBooking.getPersonalNumber() != null) {
                        existingTimeBooking.setPersonalNumber(timeBooking.getPersonalNumber());
                    }

                    return existingTimeBooking;
                }
            )
            .map(timeBookingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeBooking> findAll() {
        log.debug("Request to get all TimeBookings");
        return timeBookingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimeBooking> findOne(Long id) {
        log.debug("Request to get TimeBooking : {}", id);
        return timeBookingRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TimeBooking> findTimeBookingsByEmployeeAndBetweenRange(String employeeNumber, ZonedDateTime startDate, ZonedDateTime endDate)
    {
        return timeBookingRepository.findTimeBookingsByEmployeeAndBetweenRange(employeeNumber, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TimeBooking> findTimeBookingsBetweenRange(ZonedDateTime startDate, ZonedDateTime endDate)
    {
        return timeBookingRepository.findTimeBookingsBetweenRange(startDate, endDate);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimeBooking : {}", id);
        timeBookingRepository.deleteById(id);
    }

    @Override
    public void bookTime(TimeBookDTO timeBookDTO) {
        timeBookingRepository.save(timeBookMapper.toTimeBooking(timeBookDTO));
    }
}
