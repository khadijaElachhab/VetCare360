package com.vetcare360.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    // Formats pour la date et l'heure
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String TIME_PATTERN = "HH:mm";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    /**
     * Convertit une LocalDate en String selon le format défini.
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * Convertit un String en LocalDate selon le format défini.
     */
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Vérifie si une chaîne de date est valide.
     */
    public static boolean validDate(String dateString) {
        return parseDate(dateString) != null;
    }

    /**
     * Convertit un LocalTime en String selon le format défini.
     */
    public static String format(LocalTime time) {
        if (time == null) {
            return null;
        }
        return TIME_FORMATTER.format(time);
    }

    /**
     * Convertit un String en LocalTime selon le format défini.
     */
    public static LocalTime parseTime(String timeString) {
        try {
            return LocalTime.parse(timeString, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Vérifie si une chaîne d'heure est valide.
     */
    public static boolean validTime(String timeString) {
        return parseTime(timeString) != null;
    }
}
