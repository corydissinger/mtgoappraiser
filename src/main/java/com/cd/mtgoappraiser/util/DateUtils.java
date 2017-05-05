package com.cd.mtgoappraiser.util;

import com.cd.mtgoappraiser.Main;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by Cory on 4/22/2017.
 */
public class DateUtils {
    private static final Logger logger = Logger.getLogger(DateUtils.class);

    public static LocalDate getDate(String fullUrl) throws DateTimeParseException {
        return realBadRecursionBoys(fullUrl, 14);
    }

    private static LocalDate realBadRecursionBoys(String fullUrl, int i) {
        String datePart = getDatePart(fullUrl, i);

        datePart = datePart.endsWith("-") ? StringUtils.chop(datePart) : datePart;
        datePart = datePart.startsWith("-") ? datePart.substring(1) : datePart;

        try {
            return LocalDate.parse(datePart, DateTimeFormatter.ofPattern("M-d-yyyy"));
        } catch(DateTimeParseException dte) {
            try {
                return LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyy-M-d"));
            } catch(DateTimeParseException dte2) {
                return realBadRecursionBoys(fullUrl, i-1);
            }
        }
    }

    private static String getDatePart(String fullUrl, Integer startIndex) {
        String testDate = fullUrl.substring(fullUrl.length() - startIndex, fullUrl.length() - 4);

        return testDate;
    }
}
