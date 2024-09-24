package ru.neoflex.vacation_pay.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.neoflex.vacation_pay.ResolversCommonConfiguration;
import ru.neoflex.vacation_pay.model.VacationPayRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VacationPayApplicationTests extends ResolversCommonConfiguration {

    @Test
    @DisplayName("Test returns Bad Request when the salary is zero or less")
    void calculationOfVacationPayForEmployeeWithInvalidSalaryTest() throws Exception {
        var request = VacationPayRequest.builder()
                .averageSalaryPerYear(BigDecimal.ZERO)
                .vacationDays(TEST_VACATION_DAYS)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(VACATION_PAY_API)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Average salary must be greater than 0"));
    }

    @Test
    @DisplayName("Test returns Bad Request when the number of vacation days is zero or less")
    void calculationOfVacationPayForEmployeeWithInvalidVacationDaysTest() throws Exception {
        var request = VacationPayRequest.builder()
                .averageSalaryPerYear(TEST_AVERAGE_SALARY)
                .vacationDays(0)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(VACATION_PAY_API)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Vacation days must be greater than 0"));
    }

    @Test
    @DisplayName("Test returns Bad Request for invalid date format in vacation start date")
    void calculationOfVacationPayForEmployeeWithInvalidDateTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post(VACATION_PAY_API)
                        .contentType(APPLICATION_JSON)
                        .content("{\"averageSalaryPerYear\":30500.00,\"vacationDays\":30,\"startVacationDate\":\"invalid\"}")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid date format"));
    }

    @Test
    @DisplayName("Test calculate vacation pay correctly without vacation start date")
    void calculationOfVacationPayForEmployeeUsingSimpleQueryTest() throws Exception {
        var request = VacationPayRequest.builder()
                .averageSalaryPerYear(TEST_AVERAGE_SALARY)
                .vacationDays(TEST_VACATION_DAYS)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(VACATION_PAY_API)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(BigDecimal.valueOf(27168.80).stripTrailingZeros()));
    }

    @Test
    @DisplayName("Test calculate vacation pay correctly with vacation start date")
    void calculationOfVacationPayForEmployeeUsingQueryWithDateTest() throws Exception {
        var request = VacationPayRequest.builder()
                .averageSalaryPerYear(TEST_AVERAGE_SALARY)
                .vacationDays(TEST_VACATION_DAYS)
                .startVacationDate(LocalDate.parse(TEST_START_VACATION_DAY))
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(VACATION_PAY_API)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(BigDecimal.valueOf(18113.20).stripTrailingZeros()));
    }
}