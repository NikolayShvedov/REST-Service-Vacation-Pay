package ru.neoflex.vacation_pay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.vacation_pay.model.CalculateResponse;
import ru.neoflex.vacation_pay.model.VacationPayRequest;
import ru.neoflex.vacation_pay.service.VacationDaysService;
import ru.neoflex.vacation_pay.service.VacationPayService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class VacationPayController {

    private final VacationPayService vacationPayService;
    private final VacationDaysService vacationDaysService;

    @GetMapping("/calculacte")
    public ResponseEntity<CalculateResponse> calculate(@Valid @RequestBody VacationPayRequest request) {
        var vacationDays = request.getVacationDays();
        if (request.getStartVacationDate() != null) {
            vacationDays = vacationDaysService.getAllPaidVacationDaysByPeriod(vacationDays, request.getStartVacationDate());
        }
        var response = vacationPayService.getVacationPayCalculation(request.getAverageSalaryPerYear(), vacationDays);
        return ResponseEntity.ok(response);
    }
}
