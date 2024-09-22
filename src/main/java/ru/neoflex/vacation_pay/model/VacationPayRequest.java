package ru.neoflex.vacation_pay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VacationPayRequest {

    @DecimalMin(value = "0.0", inclusive = false, message = "Average salary must be greater than 0")
    private BigDecimal averageSalaryPerYear;

    @Min(value = 1, message = "Vacation days must be greater than 0")
    private Integer vacationDays;

    private LocalDate startVacationDate;
}
