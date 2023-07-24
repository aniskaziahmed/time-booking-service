package com.fantasy.tbs.repository;

import java.time.ZonedDateTime;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fantasy.tbs.domain.TimeBooking;

/**
 * Spring Data SQL repository for the TimeBooking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeBookingRepository extends JpaRepository<TimeBooking, Long>
{
    String TABLE_NAME = "time_booking";

    @Query("SELECT tb FROM TimeBooking tb WHERE tb.personalNumber=:employeeNumber AND tb.booking >= :startDate AND tb.booking <= :endDate")
    Set<TimeBooking> findTimeBookingsByEmployeeAndBetweenRange(@Param("employeeNumber") String employeeNumber,
        @Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

    @Query("SELECT tb FROM TimeBooking tb WHERE tb.booking >= :startDate AND tb.booking <= :endDate")
    Set<TimeBooking> findTimeBookingsBetweenRange(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

}
