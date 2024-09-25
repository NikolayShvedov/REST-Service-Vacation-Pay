package ru.neoflex.vacation_pay.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.neoflex.vacation_pay.ResolversCommonConfiguration;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VacationPayApplicationTests extends ResolversCommonConfiguration {

    @Test
    @DisplayName("Test returns Bad Request when the salary is zero or less")
    void calculationOfVacationPayForEmployeeWithInvalidSalaryTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalaryPerYear", "0")
                        .param("vacationDays", String.valueOf(TEST_VACATION_DAYS))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.averageSalaryPerYear").value("Average salary must be greater than 0"));
    }

    @Test
    @DisplayName("Test returns Bad Request when the number of vacation days is zero or less")
    void calculationOfVacationPayForEmployeeWithInvalidVacationDaysTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalaryPerYear", String.valueOf(TEST_AVERAGE_SALARY))
                        .param("vacationDays", "0")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.vacationDays").value("Vacation days must be greater than 0"));
    }

    @Test
    @DisplayName("Test returns Bad Request for invalid date format in vacation start date")
    void calculationOfVacationPayForEmployeeWithInvalidDateTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalaryPerYear", "30500.00")
                        .param("vacationDays", "30")
                        .param("startVacationDate", "invalid")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.startVacationDate").value("Invalid date format. Expected format: yyyy-MM-dd"));
    }

    @Test
    @DisplayName("Test returns multiple validation errors for invalid salary and vacation days")
    void calculationOfVacationPayForEmployeeWithMultipleInvalidParamsTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalaryPerYear", "0")
                        .param("vacationDays", "0")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.averageSalaryPerYear").value("Average salary must be greater than 0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.vacationDays").value("Vacation days must be greater than 0"));
    }

    @Test
    @DisplayName("Test calculate vacation pay correctly without vacation start date")
    void calculationOfVacationPayForEmployeeUsingSimpleQueryTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalaryPerYear", String.valueOf(TEST_AVERAGE_SALARY))
                        .param("vacationDays", String.valueOf(TEST_VACATION_DAYS))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(BigDecimal.valueOf(27168.80).stripTrailingZeros()));
    }

    @Test
    @DisplayName("Test calculate vacation pay correctly with vacation start date")
    void calculationOfVacationPayForEmployeeUsingQueryWithDateTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get(VACATION_PAY_API)
                        .param("averageSalaryPerYear", String.valueOf(TEST_AVERAGE_SALARY))
                        .param("vacationDays", String.valueOf(TEST_VACATION_DAYS))
                        .param("startVacationDate", TEST_START_VACATION_DAY)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(BigDecimal.valueOf(18113.20).stripTrailingZeros()));
    }
}
