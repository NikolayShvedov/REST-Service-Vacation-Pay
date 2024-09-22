package ru.neoflex.vacation_pay.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.vacation_pay.model.CalculateResponse;
import ru.neoflex.vacation_pay.service.VacationPayService;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.HALF_UP;
import static ru.neoflex.vacation_pay.common.VacationPayCalculateHelper.AVERAGE_NUMBER_DAYS_IN_MOUNT;
import static ru.neoflex.vacation_pay.common.VacationPayCalculateHelper.NDFL_PERCENT;

@Slf4j
@Service
public class VacationPayServiceImpl implements VacationPayService {

    /**
     * Calculates the vacation pay for an employee based on their average annual salary
     * and the number of vacation days, including NDFL.
     *
     * @param averageSalaryPerYear The employee's average salary for the year, used to calculate daily earnings
     * @param vacationDays         The number of vacation days the employee is taking
     * @return {@link CalculateResponse} object containing the final vacation pay after NDFL.
     */
    @Override
    public CalculateResponse getVacationPayCalculation(BigDecimal averageSalaryPerYear,
                                                       Integer vacationDays) {

        var averageEarningsPerDay = averageSalaryPerYear.divide(
                BigDecimal.valueOf(AVERAGE_NUMBER_DAYS_IN_MOUNT), 2, HALF_EVEN);
        log.info("Average daily wage = {} RUB", averageEarningsPerDay);

        var totalPayWithoutNDFL = averageEarningsPerDay.multiply(BigDecimal.valueOf(vacationDays));
        log.info("Vacation pay amount without deduction of NDFL = {} RUB", totalPayWithoutNDFL);

        var amountNDFL = totalPayWithoutNDFL
                .multiply(BigDecimal.valueOf(NDFL_PERCENT))
                .setScale(0, HALF_UP);
        log.info("Amount of NDFL = {} RUB", amountNDFL);

        var totalPay = totalPayWithoutNDFL.subtract(amountNDFL);
        log.info("To be paid with deduction of NDFL = {} RUB", totalPay);

        return CalculateResponse.builder()
                .vacationPay(totalPay)
                .build();
    }
}
