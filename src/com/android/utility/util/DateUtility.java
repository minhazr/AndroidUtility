
package com.android.utility.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtility {

    public static long getDays(Date startDay, Date endDate) {
        final long MSPERDAY = 60 * 60 * 24 * 1000;
        final Calendar dateStartCal = Calendar.getInstance();
        dateStartCal.setTime(startDay);
        dateStartCal.set(Calendar.HOUR_OF_DAY, 0); // Crucial.
        dateStartCal.set(Calendar.MINUTE, 0);
        dateStartCal.set(Calendar.SECOND, 0);
        dateStartCal.set(Calendar.MILLISECOND, 0);
        final Calendar dateEndCal = Calendar.getInstance();
        dateEndCal.setTime(endDate);
        dateEndCal.set(Calendar.HOUR_OF_DAY, 0); // Crucial.
        dateEndCal.set(Calendar.MINUTE, 0);
        dateEndCal.set(Calendar.SECOND, 0);
        dateEndCal.set(Calendar.MILLISECOND, 0);
        final long dateDifferenceInDays = (dateStartCal.getTimeInMillis()
                - dateEndCal.getTimeInMillis()
                ) / MSPERDAY;
        return dateDifferenceInDays;
    }

}
