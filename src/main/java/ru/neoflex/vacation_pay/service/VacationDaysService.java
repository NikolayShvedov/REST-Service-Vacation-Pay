package ru.neoflex.vacation_pay.service;

import java.time.LocalDate;

public interface VacationDaysService {

    Integer getAllPaidVacationDaysByPeriod(Integer vacationDays,
                                           LocalDate startVacationDate);
}
