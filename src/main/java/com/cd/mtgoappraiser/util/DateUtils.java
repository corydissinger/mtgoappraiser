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

    public static LocalDate getDate(String datePart, String urlAsString) {
        datePart = datePart.endsWith("-") ? StringUtils.chop(datePart) : datePart;
        datePart = datePart.startsWith("-") ? datePart.substring(1) : datePart;

        try {
            return LocalDate.parse(datePart, DateTimeFormatter.ofPattern("M-d-yyyy"));
        } catch(DateTimeParseException dte) {
            logger.warn("Found old style incorrect date for " + urlAsString);
            return LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyy-M-d"));
        }
    }
}
