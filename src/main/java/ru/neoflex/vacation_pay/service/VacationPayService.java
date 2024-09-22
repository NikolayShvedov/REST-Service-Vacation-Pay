package ru.neoflex.vacation_pay.service;

import ru.neoflex.vacation_pay.model.CalculateResponse;

import java.math.BigDecimal;

public interface VacationPayService {

    CalculateResponse getVacationPayCalculation(BigDecimal averageSalaryPerYear,
                                                Integer vacationDays);
}
