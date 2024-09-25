package ru.neoflex.vacation_pay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VacationPayRequest {

    @NotNull(message = "Average salary cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Average salary must be greater than 0")
    private BigDecimal averageSalaryPerYear;

    @NotNull(message = "Vacation days cannot be null")
    @Min(value = 1, message = "Vacation days must be greater than 0")
    private Integer vacationDays;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startVacationDate;
}
