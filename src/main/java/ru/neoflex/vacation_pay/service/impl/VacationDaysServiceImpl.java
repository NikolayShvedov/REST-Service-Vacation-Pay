package ru.neoflex.vacation_pay.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.vacation_pay.common.VacationDaysHelper;
import ru.neoflex.vacation_pay.service.VacationDaysService;

import java.time.LocalDate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

@Slf4j
@Service
public class VacationDaysServiceImpl implements VacationDaysService {

    /**
     * Calculates the total number of paid vacation days for a given period, excluding weekends
     * and holidays.
     *
     * @param vacationDays      The total number of vacation days requested.
     * @param startVacationDate The date when the vacation starts.
     * @return The number of paid vacation days that are not on weekends or holidays.
     */
    @Override
    public Integer getAllPaidVacationDaysByPeriod(Integer vacationDays,
                                                  LocalDate startVacationDate) {

        var vacationDatesStream = Stream.iterate(startVacationDate, nextVacationDate -> nextVacationDate.plusDays(1))
                .limit(vacationDays);

        Predicate<LocalDate> holidays = VacationDaysHelper.getHolidays()::contains;
        var listPaidVacationDate = vacationDatesStream
                .filter(vacationDate -> !isHolidayOrWeekend(vacationDate, holidays))
                .collect(Collectors.toList());

        return listPaidVacationDate.size();
    }

    private boolean isHolidayOrWeekend(LocalDate vacationDate, Predicate<LocalDate> holidays) {
        return holidays.test(vacationDate) || vacationDate.getDayOfWeek() == SATURDAY || vacationDate.getDayOfWeek() == SUNDAY;
    }
}
