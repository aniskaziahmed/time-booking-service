package com.fantasy.tbs.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateTimeUtils
{
    private static final ZoneOffset UTC_ZONE_OFFSET = ZoneOffset.UTC;

    public static ZonedDateTime getCurrentZonedDateTime()
    {
        return ZonedDateTime.now(UTC_ZONE_OFFSET);
    }

    public static ZonedDateTime getZonedDateTimeXDaysAgo(int daysAgo) {
        return getCurrentZonedDateTime().minusDays(daysAgo);
    }

    public static ZonedDateTime getZonedDateTimeStartOfDayXDaysAgo(int daysAgo) {
        return getZonedDateTimeXDaysAgo(daysAgo).toLocalDate().atStartOfDay(UTC_ZONE_OFFSET);
    }

    public static ZonedDateTime getZonedDateTimeEndOfDayXDaysAgo(int daysAgo) {
        return getZonedDateTimeXDaysAgo(daysAgo).toLocalDate().atTime(LocalTime.MAX).atZone(UTC_ZONE_OFFSET);
    }

    // Convert LocalDateTime to ZonedDateTime using UTC time zone
    public static ZonedDateTime convertToZonedDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(UTC_ZONE_OFFSET);
    }
}
