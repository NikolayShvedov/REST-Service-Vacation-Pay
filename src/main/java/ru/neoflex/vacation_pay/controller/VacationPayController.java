package ru.neoflex.vacation_pay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.vacation_pay.model.CalculateResponse;
import ru.neoflex.vacation_pay.model.VacationPayRequest;
import ru.neoflex.vacation_pay.service.VacationDaysService;
import ru.neoflex.vacation_pay.service.VacationPayService;

import javax.validation.Valid;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
public class VacationPayController {

    private final VacationPayService vacationPayService;
    private final VacationDaysService vacationDaysService;

    @GetMapping("/calculacte")
    public ResponseEntity<CalculateResponse> calculate(@Valid VacationPayRequest request) {
        var vacationDays = calculateVacationDays(request.getVacationDays(), request.getStartVacationDate());
        var response = vacationPayService.getVacationPayCalculation(request.getAverageSalaryPerYear(), vacationDays);
        return ResponseEntity.ok(response);
    }

    private Integer calculateVacationDays(Integer vacationDays, LocalDate startVacationDate) {
        if (startVacationDate != null) {
            vacationDays = vacationDaysService.getAllPaidVacationDaysByPeriod(vacationDays, startVacationDate);
        }
        return vacationDays;
    }
}
